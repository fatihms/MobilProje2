package com.example.takvimuygulamasi;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedP {

    static final String PREF_NAME = "settings";

    public void putStringValue(Context context , String key , String value){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key,value).apply();
    }

    public String getStringValue(Context context,String key,String defaultv){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        String text = pref.getString(key,defaultv);
        return text;
    }

    public void putBoolValue(Context context , String key , Boolean value){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(key,value).apply();
    }

    public boolean getBoolValue(Context context , String key , Boolean defaultval){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        boolean bl = pref.getBoolean(key,defaultval);
        return bl;
    }

    public void putIntValue(Context context,String key , int value){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(key,value).apply();
    }

    public int getIntValue(Context context,String key,int def){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        int val = pref.getInt(key,def);
        return  val;
    }

    public void removePreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,0);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear().apply();
    }
}

