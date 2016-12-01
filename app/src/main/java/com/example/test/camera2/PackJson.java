package com.example.test.camera2;


import android.os.Environment;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

/**
 * Created by xuejing on 2016/11/3.
 */

public class PackJson {
    private byte[] bytes;
    private String name;
    private Date date = new Date();
    //private int count = 1;
    JSONObject JsonData = new JSONObject();
    // read the json
    private ImageData imageData;
    private Writer writer;

    PackJson(byte[] bytes) {
        this.name = "Image";
        this.bytes = bytes;
        try {
            writer = new Writer(9876);
            InetAddress ipaddress = InetAddress.getByName("172.28.184.169");
            writer.addAddress(ipaddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // Add a JSON Object
    // JPEG or RAW imagebytes code convert to ASCII base64
    public void insertJsonObject() {
        try {
            JsonData.put(name, Base64Encoding(bytes));

            //JsonData.put(date.toString(), Base64Encoding(bytes));
            //JsonData.put(name+"_"+count, Base64Encoding(bytes));
            // count++;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }// end of insertJsonObject

    public JSONObject jsonObject() {
        return JsonData;
    }

    // base64 encoding
    private String Base64Encoding(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private byte[] readJson() {
        imageData = new ImageData(JsonData);
        String base64ImageData = imageData.getImageData();
        byte[] readBytes = null;
        return readBytes;
    }

}
