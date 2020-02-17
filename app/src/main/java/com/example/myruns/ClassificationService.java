package com.example.myruns;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class ClassificationService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private static ArrayBlockingQueue<Double> buffer;
    private SensorUpdateTask asyncTask;
    private Instances dataset;
    private Attribute classAttribute;

    private WekaWrapper model;

    public static final int ACCELEROMETER_BUFFER_CAPACITY = 2048;
    public static final int ACCELEROMETER_BLOCK_CAPACITY = 64;
    public static final int FEATURE_SET_CAPACITY = 10000;
    private static final int featureLength = ACCELEROMETER_BLOCK_CAPACITY + 2;
    public static final String FEAT_FFT_COEF_LABEL = "fft_coef_";
    public static final String FEAT_MAX_LABEL = "max";
    public static final String FEAT_SET_NAME = "accelerometer_features";
    public static final String STOP_SERVICE_ACTION = "stop service action for ML";
    public static final int NOTIFY_ID = 12;
    ClassificationServiceReceiver receiver;

    public static final String ACTION_NEW_CLASSIFICATION = "com.example.action.NEW_CLASSIFICATION";

    NotificationManager notificationManager;
    private Sensor accelerometer;

    @Override
    public void onCreate() {
        super.onCreate();
        model = new WekaWrapper();
        buffer = new ArrayBlockingQueue<Double>(ACCELEROMETER_BUFFER_CAPACITY);

        receiver = new ClassificationServiceReceiver();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d("johnmacdonald", "Hello from Classification Service!");

        IntentFilter filter = new IntentFilter();
        filter.addAction(STOP_SERVICE_ACTION);
        registerReceiver(receiver, filter);
    }

    public class ClassificationServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //stopSelf();
            //notificationManager.cancel(NOTIFY_ID);
            Log.d("johnmacdonald", "lol cancelling ml");
            //unregisterReceiver(receiver);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Log.d("johnmacdonald", "Starting Classification Sensor...");

        ArrayList<Attribute> attributes = new ArrayList<Attribute>();

        // Adding FFT coefficient attributes
        DecimalFormat decimalFormat = new DecimalFormat("0000");

        for (int i = 0; i < ACCELEROMETER_BLOCK_CAPACITY; i++) {
            attributes.add(new Attribute(FEAT_FFT_COEF_LABEL + decimalFormat.format(i)));
        }

        // Adding the max feature
        attributes.add(new Attribute(FEAT_MAX_LABEL));

        // Declare a nominal attribute along with its candidate values
        ArrayList<String> labelItems = new ArrayList<String>(3);
        labelItems.add("standing");
        labelItems.add("walking");
        labelItems.add("running");
        //labelItems.add(Globals.CLASS_LABEL_OTHER);
        classAttribute = new Attribute("label", labelItems);
        attributes.add(classAttribute);

        // Construct the dataset with the attributes specified as allAttr and
        // capacity 10000
        dataset = new Instances(FEAT_SET_NAME, attributes, FEATURE_SET_CAPACITY);

        // Set the last column/attribute (standing/walking/running) as the class
        // index for classification
        dataset.setClassIndex(dataset.numAttributes() - 1);

        asyncTask = new SensorUpdateTask();
        asyncTask.execute();

        return START_NOT_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            double x = event.values[0] / sensorManager.GRAVITY_EARTH;
            double y = event.values[1] / sensorManager.GRAVITY_EARTH;
            double z = event.values[2] / sensorManager.GRAVITY_EARTH;

            double magnitude = Math.sqrt(x * x + y * y + z * z);

            //Log.d("johnmacdonald", "mag: " + String.valueOf(magnitude));

            try {
                buffer.add(Double.valueOf(magnitude));
            } catch (IllegalStateException e) {
                ArrayBlockingQueue<Double> newBuffer = new ArrayBlockingQueue<Double>(this.buffer.size() * 2);
                buffer.drainTo(newBuffer);
                buffer = newBuffer;
                buffer.add(Double.valueOf(magnitude));
            }
        }
    }

    private class SensorUpdateTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            Instance instance = new DenseInstance(featureLength);
            instance.setDataset(dataset);

            int blockSize = 0;
            FFT fft = new FFT(ACCELEROMETER_BLOCK_CAPACITY);

            double[] accelerometerBlock = new double[ACCELEROMETER_BLOCK_CAPACITY];
            double[] accelerometerBlockCopy = accelerometerBlock;
            double[] image = new double[ACCELEROMETER_BLOCK_CAPACITY];
            double max = Double.MIN_VALUE;

            while(true) {
                try {
                    accelerometerBlock[blockSize++] = buffer.take().doubleValue();

                    if(blockSize == ACCELEROMETER_BLOCK_CAPACITY) {
                        blockSize = 0;
                        max = 0.0;

                        for(double value : accelerometerBlock) {
                            if (value > max) {
                                max = value;
                            }
                        }

                        fft.fft(accelerometerBlockCopy, image);

                        ArrayList<Double> featureVector = new ArrayList<Double>();

                        for(int i = 0; i < accelerometerBlockCopy.length; i++) {
                            double magnitude = Math.sqrt((accelerometerBlockCopy[i] * accelerometerBlockCopy[i]) + (image[i] * image[i]));
                            // featureVector.add(magnitude);
                            instance.setValue(i, magnitude);
                            image[i] = 0.0;
                        }

                        instance.setValue(ACCELEROMETER_BLOCK_CAPACITY, max);

                        dataset.add(instance);

                        double classification = model.classifyInstance(instance);
                        String activity = "Unknown";

                        if(classification == 0) {
                            activity = "Standing";
                        } else if (classification == 1) {
                            activity = "Walking";
                        } else if (classification == 2) {
                            activity = "Running";
                        }

                        Intent intent = new Intent(ACTION_NEW_CLASSIFICATION);
                        intent.putExtra("activity", activity);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
