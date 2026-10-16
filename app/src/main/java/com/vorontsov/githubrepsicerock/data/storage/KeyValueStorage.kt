package com.vorontsov.githubrepsicerock.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class KeyValueStorage(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var authToken: String?
        get() = preferences.getString(AUTH_TOKEN_KEY, null)
        set(value) = preferences.edit { putString(AUTH_TOKEN_KEY, value) }

    fun clearAuthToken() {
        preferences.edit { remove(AUTH_TOKEN_KEY) }
    }

    private companion object {
        const val PREFERENCES_NAME = "github_reps_storage"
        const val AUTH_TOKEN_KEY = "authToken"
    }
}