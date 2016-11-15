package com.example.test.camera2;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by xuejing on 2016/11/3.
 */

public class ImageData {
    String picId;
    private JSONObject jsonData;
    private BufferedReader br;
    public ImageData(String name, JSONObject jsonData)
    {
        picId = name;
        this.jsonData = jsonData;
    }

}
