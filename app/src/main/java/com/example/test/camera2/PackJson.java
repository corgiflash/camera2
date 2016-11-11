package com.example.test.camera2;


import android.util.Base64;

import org.json.JSONObject;

/**
 * Created by xuejing on 2016/11/3.
 */

public class PackJson {
    private byte[] bytes;
    private String name,result;
    PackJson(String name, byte[] bytes) {
        this.bytes = bytes;
        this.name = name;
    }

    public JSONObject pack() {
        result=Base64.encodeToString(bytes, Base64.DEFAULT);
        JSONObject packed = new JSONObject();
        return packed;
    }
}
