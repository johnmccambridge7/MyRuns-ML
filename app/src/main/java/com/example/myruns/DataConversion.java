package com.example.myruns;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class DataConversion {
    public static String toJSON(ArrayList<LatLng> coords) throws JSONException {
        JSONObject main = new JSONObject();

        int i = 0;
        for(LatLng coord : coords) {
            JSONObject c = new JSONObject();
            c.put("lat", coord.latitude);
            c.put("lng", coord.longitude);

            main.put(String.valueOf(i), c);
            i += 1;
        }

        return main.toString();
    }

    public static ArrayList<LatLng> toArrayList(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        ArrayList<LatLng> coords = new ArrayList<LatLng>();

        Iterator<String> keys = obj.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            JSONObject coord = (JSONObject) obj.get(key);

            LatLng c = new LatLng(coord.getDouble("lat"), coord.getDouble("lng"));
            coords.add(c);
        }

        return coords;
    }
}
