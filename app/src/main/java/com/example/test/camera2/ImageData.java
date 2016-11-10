package com.example.test.camera2;

/**
 * Created by xuejing on 2016/11/3.
 */

public class ImageData {
    String picId;
    final private byte[] bytes;
    public ImageData(String name,byte[] bytes)
    {
        picId = name;
        this.bytes = bytes;
    }
    public byte[] getBytes() {
        return bytes;
    }

}
