package com.example.test.camera2;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;

/**
 * Created by xuejing on 2016/11/3.
 */

public class ImageData {
    String picId;
    private JSONObject jsonData;
    private BufferedReader br;
    private String temp;
    public ImageData(JSONObject jsonData)
    {
        //picId = name;
        this.jsonData = jsonData;
    }
    public String getImageData(){
        temp = null;
        try {
            parseJson(jsonData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }
    private void parseJson(JSONObject jsonObject) throws ParseException {

    }

}
