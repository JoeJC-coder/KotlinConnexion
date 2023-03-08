package com.example.kotlinconnexion

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    companion object {
        private const val PREF_NAME = "UserSession"
        private const val KEY_USERNAME = "username"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun createLoginSession(email: String) {
        editor.putString(KEY_USERNAME, email)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains(KEY_USERNAME)
    }

    fun isLoggedOut(): Boolean {
        return !sharedPreferences.contains(KEY_USERNAME)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}