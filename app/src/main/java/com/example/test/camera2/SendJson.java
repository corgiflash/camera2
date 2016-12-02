package com.example.test.camera2;


import android.os.Environment;
import android.util.Base64;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendJson {
    private byte[] bytes;
    private String type, name;
    private JSONObject jsonData;

    SendJson(byte[] bytes) {
        this.type = "Image";
        this.name =new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.bytes = bytes;
        jsonData = new JSONObject();
        insertJsonObject();
    }
    public void insertJsonObject() {
        String encryptData=Base64Encoding(bytes);
        JSONArray jsonArray = new JSONArray();
        int sequence = 0;
        int start = 0;
        int length = encryptData.length();
        do {
            JSONObject partialData = new JSONObject();
            try {
                partialData.put("sequence", sequence);
                partialData.put("data", encryptData.substring(start,start + 15000));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(partialData);
            length -= 15000;
            start += 15000;
            sequence++;
            }
        while(length - 15000 > 0);
        JSONObject partialData = new JSONObject();
        try {
            partialData.put("sequence", sequence);
            partialData.put("data", encryptData.substring(start));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(partialData);

        try {
            jsonData.put(name, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }// end of insertJsonObject

    // base64 encoding
    private String Base64Encoding(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    public JSONObject getJsonData()
    {
        return jsonData;
    }
    public String getName()
    {
        return name;
    }
}
