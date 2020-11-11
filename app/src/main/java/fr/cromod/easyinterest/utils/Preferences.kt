package fr.cromod.easyinterest.utils

import android.content.Context
import android.app.Activity
import android.content.SharedPreferences

class Preferences(activity: Activity?){

    private var preferences = activity?.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

    private var editor = preferences?.edit()

    fun getString(key: String): String
    {
        return preferences?.getString(key, "") ?: ""
    }

    fun setString(key: String, value: String)
    {
        editor?.putString(key, value)?.commit()
    }

    fun getBool(key: String): Boolean
    {
        return preferences?.getBoolean(key, false) ?: false
    }

    fun setBool(key: String, value: Boolean)
    {
        editor?.putBoolean(key, value)?.commit()
    }

    fun getFloat(key: String): Float
    {
        return preferences?.getFloat(key, 0f) ?: 0f
    }

    fun setFloat(key: String, value: Float)
    {
        editor?.putFloat(key, value)?.commit()
    }

    fun getInt(key: String): Int
    {
        return preferences?.getInt(key, 0) ?: 0
    }

    fun setInt(key: String, value: Int)
    {
        editor?.putInt(key, value)?.commit()
    }
}