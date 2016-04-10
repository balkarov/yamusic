package com.balkarov.yamusic.Views;

import android.app.Activity;

public class AlertView {

    // Show error when there is no internet connection
    public static void internetConnectionError(Activity activity){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(activity);
        alert.setTitle("Ошибка подключения");
        alert.setMessage("Проверьте подключение к интернету и попробуйте снова");
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    // Show unknown error (e.g. when parsing JSON response throw exception)
    public static void unknownError(Activity activity){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(activity);
        alert.setTitle("Неизвестная ошибка");
        alert.setMessage("Произошла неизвестная ошибка, мы уже работаем над этим");
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
