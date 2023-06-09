package com.capestone.facential.data.local

import android.content.Context
import android.content.SharedPreferences

    class SharedPreferencesManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(FACENTIAL_PREF, Context.MODE_PRIVATE)
    }

    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString(FACENTIAL_AUTH_TOKEN, token).apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(FACENTIAL_AUTH_TOKEN, null)
    }

    fun clearAuthToken() {
        sharedPreferences.edit().remove(FACENTIAL_AUTH_TOKEN).apply()
    }

    fun setOnboardingFinished() {
        sharedPreferences.edit().putBoolean(FACENTIAL_ONBOARDING_FINISHED, true).apply()
    }

    fun isOnboardingFinished() : Boolean {
        return sharedPreferences.getBoolean(FACENTIAL_ONBOARDING_FINISHED, false)
    }

    companion object {
        private const val FACENTIAL_PREF = "facential_preferences"
        private const val FACENTIAL_ONBOARDING_FINISHED = "facential_onboarding_finished"
        private const val FACENTIAL_AUTH_TOKEN = "facential_auth_token"
    }
}