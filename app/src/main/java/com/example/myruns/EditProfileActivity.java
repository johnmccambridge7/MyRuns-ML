package com.example.myruns;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import android.app.DialogFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private static final String PACKAGE = "com.example.myruns";
    private static final String DIALOG_ID_KEY = "dialog_id";
    private static final String PROFILE_IMAGE_URI_KEY = "profile_image_uri_key";

    private static final int CAMERA_REQUEST = 0;
    private static final int PICK_IMAGE = 1;

    ImageView profilePicture;
    TextView username;
    TextView phoneNumber;
    TextView emailAddress;
    RadioGroup gender;
    TextView major;
    TextView year;

    SharedPreferences.Editor editor;
    String selectedGender;
    Uri imageURI;
    Uri newImage;
    File imageFile;
    boolean capturedFromCamera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);

        this.profilePicture = (ImageView) findViewById(R.id.profilePicture);
        this.username = (TextView) findViewById(R.id.username);
        this.phoneNumber = (TextView) findViewById(R.id.phone);
        this.emailAddress = (TextView) findViewById(R.id.email);
        this.major = (TextView) findViewById(R.id.major);
        this.gender = (RadioGroup) findViewById(R.id.gender);
        this.year = (TextView) findViewById(R.id.year);

        // sends notification to user requesting permission for data storage and camera use
        CameraHandler.checkPermission(this);

        // instance variables for UI
        this.imageFile = new File(getExternalFilesDir(null), getString(R.string.profileImagePath));
        this.imageURI = FileProvider.getUriForFile(this, PACKAGE, this.imageFile);
        this.selectedGender = preferences.getString("gender", "");

        this.editor = preferences.edit();

        if(savedInstanceState != null) {
            this.newImage = savedInstanceState.getParcelable("imageData");
        }

        this.loadInformation();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable("imageData", this.newImage);
    }

    public void displayDialog(int id) {
        DialogFragment frag = ImageDialog.newInstance(id);
        frag.show(getFragmentManager(), "dialog_fragment_photo_picker");
    }

    public void dialogSelection(int item) {
        if(item == ImageDialog.ID_PHOTO_PICKER_FROM_CAMERA) {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, this.imageURI);
            startActivityForResult(camera, CAMERA_REQUEST);
            this.capturedFromCamera = true;
        } else if(item == ImageDialog.ID_PHOTO_PICKER_FROM_GALLERY) {
            Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
            gallery.setType("image/*");
            gallery.putExtra(MediaStore.EXTRA_OUTPUT, this.imageURI);
            gallery.putExtra("return-data", true);
            startActivityForResult(gallery, PICK_IMAGE);
            this.capturedFromCamera = false;
        }
    }

    public void captureImage(View view) {
        this.displayDialog(ImageDialog.DIALOG_ID_PHOTO_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) {
            // exit out of the current activity
            // cases include when a user exits the capture process.
            return;
        }

        File f = new File(getExternalFilesDir("my_images"), getString(R.string.profileImagePath));
        Uri destination = FileProvider.getUriForFile(getApplicationContext(), "com.example.myruns", f); //Uri.fromFile(new File(getCacheDir(), getString(R.string.profileImagePath)));

        switch(requestCode) {
            case CAMERA_REQUEST:
                Crop.of(this.imageURI, destination).asSquare().start(this);
                break;

            case PICK_IMAGE:
                Uri uri = data.getData();
                Crop.of(uri, destination).asSquare().start(this);
                break;

            case Crop.REQUEST_CROP:
                this.newImage = Crop.getOutput(data);
                this.profilePicture.setImageURI(this.newImage);
                break;
        }
    }

    public void saveImageToDevice() {
        FileOutputStream outputStream;

        try {
            BitmapDrawable drawable = (BitmapDrawable) this.profilePicture.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            outputStream = openFileOutput(getString(R.string.profileImagePath), MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void genderSelected(View view) {
        RadioButton button = (RadioButton) view;
        boolean checked = button.isChecked();

        switch(view.getId()) {
            case R.id.male:
                if(checked) {
                    this.selectedGender = "male";
                }
                break;
            case R.id.female:
                if(checked) {
                    this.selectedGender = "female";
                }
                break;
        }
    }

    private void loadProfilePicture() {
        FileInputStream inputStream;

        if(this.newImage == null) {
            try {
                inputStream = openFileInput(getString(R.string.profileImagePath));
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                this.profilePicture.setImageBitmap(bitmap);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.profilePicture.setImageURI(this.newImage);
        }
    }

    public void loadInformation() {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);

        // fetch in the stored user states
        this.username.setText(preferences.getString("username", ""));
        this.phoneNumber.setText(preferences.getString("phone", ""));
        this.emailAddress.setText(preferences.getString("email", ""));
        this.major.setText(preferences.getString("major", ""));
        this.year.setText(preferences.getString("year", ""));

        if(this.selectedGender.equals("male")) {
            this.gender.check(R.id.male);
        } else if (this.selectedGender.equals("female")){
            this.gender.check(R.id.female);
        }

        // load in the saved profile picture
        this.loadProfilePicture();
    }

    public void saveInformation(View view) {
        this.editor.putString("username", this.username.getText().toString());
        this.editor.putString("phone", this.phoneNumber.getText().toString());
        this.editor.putString("email", this.emailAddress.getText().toString());
        this.editor.putString("major", this.major.getText().toString());
        this.editor.putString("year", this.year.getText().toString());
        this.editor.putString("gender", this.selectedGender);

        this.editor.commit();
        this.saveImageToDevice();

        Toast.makeText(getApplicationContext(), "Information has been saved!", Toast.LENGTH_LONG).show();
    }
}