package com.rupiee.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by ketan on 7/3/17.
 */
public class PreferenceManager {

    private String TAG = PreferenceManager.class.getSimpleName();

    private static PreferenceManager mPreferenceManager;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static Context mContext;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "com.rupiee";

    public static PreferenceManager getInstance(Context context) {
        if(context == null && mContext == null) {

        } else {
            mContext = context;
            mPreferenceManager = new PreferenceManager(mContext);
        }
        return mPreferenceManager;
    }
    // Constructor
    private PreferenceManager(Context context) {
        this.mContext = context;
        pref = this.mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }

    public float getFloat(String key) {
        return pref.getFloat(key, 0);
    }


    public void putFloat(String key, Float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void getBoolean(String key) {
        pref.getBoolean(key, false);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.commit();
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    public void clearPrefs() {
        editor.clear();
        editor.commit();
    }

    public boolean contains(String key) {
        return pref.contains(key);
    }
}
