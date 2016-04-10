package com.balkarov.yamusic.Utilities;


public class Prefix {

    //formatting string with prefix (i.e 25 альбомОВ, 4 альбомА, 1 трек, 89 трекОВ)
    public static String getStringPrefix(int count) {
        switch (count % 10) {
            case 1:
                return "";
            case 2:case 3:case 4:
                return "а";
            default:
                return "ов";
        }
    }
}
