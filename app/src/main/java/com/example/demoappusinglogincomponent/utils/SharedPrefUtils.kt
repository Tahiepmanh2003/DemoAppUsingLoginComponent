package com.example.demoappusinglogincomponent.utils

import android.content.Context
import com.example.demoappusinglogincomponent.model.User
import com.google.gson.Gson

object SharedPrefUtils {
    private const val PREF_NAME = "demoappusinglogincomponentApp"
    private const val KEY_USER = "user"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveUser(context: Context, user: User?) {
        val editor = getSharedPreferences(context).edit()
        if (user != null) {
            val gson = Gson()
            val userJson = gson.toJson(user)
            editor.putString(KEY_USER, userJson)
            editor.putBoolean(KEY_IS_LOGGED_IN, true)
        } else {
            editor.remove(KEY_USER)
            editor.putBoolean(KEY_IS_LOGGED_IN, false)
        }
        editor.apply()
    }

    fun getUser(context: Context): User? {
        val prefs = getSharedPreferences(context)
        val userJson = prefs.getString(KEY_USER, null)
        return if (userJson != null) {
            val gson = Gson()
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearUser(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(KEY_USER)
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.apply()
    }
}