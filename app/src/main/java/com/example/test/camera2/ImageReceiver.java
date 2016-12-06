package com.example.test.camera2;

import android.os.Environment;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageReceiver {
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private String name;
    
    public ImageReceiver(JSONObject jsonData, String name){
        this.jsonObject = jsonData;
        this.name = name;
        //writeToFile();
    }
    public byte[] getData() {
        String temp = "";
        JSONArray jsonArray;
        try {
            jsonArray = jsonObject.getJSONArray(name);
            for (int i = 0; i < jsonArray.length(); i++) {
                temp +=jsonArray.getJSONObject(i).get("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Base64.decode(temp, Base64.DEFAULT);
    }
}
