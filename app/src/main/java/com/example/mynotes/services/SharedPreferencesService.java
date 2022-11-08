package com.example.mynotes.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.mynotes.MainActivity;
import com.google.gson.Gson;

import java.util.Locale;

public class SharedPreferencesService extends MainActivity {

    SharedPreferences sharedPreferences;
    Context appContext;

    public void saveToSharedPreferences(String fileAndAttrName, Object object) {
        appContext = getApplicationContext();
        String fileName = "com.example." + fileAndAttrName.toLowerCase(Locale.ROOT);
        sharedPreferences = getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        sharedPreferences.edit().putString(fileAndAttrName, json).apply();
    }

    public String readFromSharedPreferences(String fileAndAttrName) {
        String fileName = "com.example." + fileAndAttrName.toLowerCase(Locale.ROOT);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);

        String data = sharedPreferences.getString(fileAndAttrName, null);
        return data;
    }



}
