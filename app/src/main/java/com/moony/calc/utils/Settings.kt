package com.moony.calc.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class Settings (context: Context){

    private var preferences: SharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)


    private var mEditor: SharedPreferences.Editor? = null

    companion object {
        @Volatile
        private var instance: Settings? = null
        fun getInstance(context: Context): Settings{
            if (instance == null) {
                synchronized(Settings::class.java) {
                    if (instance == null) {
                        instance = SettingHelper.getSettings(context)
                    }
                }
            }
            return instance!!
        }
    }



    private object SettingHelper {
        fun getSettings(context: Context): Settings {
            return Settings(context.applicationContext)
        }
    }

    //Put String data to SharedPreferences xml file
    fun put(keys: SettingKey, values: String?) {
        doEdit()
        mEditor?.putString(keys.toString(), values)
        doApply()
    }

    //Put int data to SharedPreferences xml file
    fun put(key: SettingKey, value: Int) {
        doEdit()
        mEditor?.putInt(key.toString(), value)
        doApply()
    }

    //Put boolean data to SharedPreferences xml file
    fun put(key: SettingKey, value: Boolean) {
        doEdit()
        mEditor?.putBoolean(key.toString(), value)
        doApply()
    }

    //retrieve data of type int from SharedPreferences xml file
    fun getInt(key: SettingKey): Int {
        return preferences.getInt(key.toString(), 0)
    }

    //retrieve data of type boolean from SharedPreferences xml file
    fun getBoolean(key: SettingKey): Boolean {
        return preferences.getBoolean(key.toString(), false)
    }

    //retrieve data of type String from SharedPreferences xml file
    fun getString(key: SettingKey,defaultValue: String = ""): String? {
        return preferences.getString(key.toString(), defaultValue)
    }

    @SuppressLint("CommitPrefEdits")
    private  fun doEdit() {
        if (mEditor == null) {
            mEditor = preferences.edit()
        }
    }

    private  fun doApply() {
        mEditor?.apply()
    }

    enum class SettingKey{
        CURRENCY_UNIT
    }
}