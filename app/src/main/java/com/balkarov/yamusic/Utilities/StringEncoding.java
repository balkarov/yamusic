package com.balkarov.yamusic.Utilities;

import java.io.UnsupportedEncodingException;


public class StringEncoding {

    public static String getStringFromByte(byte[] byteInput) {
        String byteString = null;
        if (byteInput != null) {
            try {
                byteString = new String(byteInput, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return byteString;
    }

}
