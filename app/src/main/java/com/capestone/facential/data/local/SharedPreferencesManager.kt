package com.capestone.facential.data.local

import android.content.Context
import android.content.SharedPreferences
import java.util.*

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

    fun saveDaySwitchState(state: Boolean) {
        sharedPreferences.edit().putBoolean(FACENTIAL_DAY_SWITCH_STATE, state).apply()
    }

    fun getDaySwitchState(): Boolean {
        return sharedPreferences.getBoolean(FACENTIAL_DAY_SWITCH_STATE, false)
    }

    fun saveNightSwitchState(state: Boolean) {
        sharedPreferences.edit().putBoolean(FACENTIAL_NIGHT_SWITCH_STATE, state).apply()
    }

    fun getNightSwitchState(): Boolean {
        return sharedPreferences.getBoolean(FACENTIAL_NIGHT_SWITCH_STATE, false)
    }

    fun resetSwitchStateIfNewDay() {
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val storedDay = sharedPreferences.getInt(FACENTIAL_LAST_RESET_DAY, -1)

        if (currentDay != storedDay) {
            sharedPreferences.edit().putBoolean(FACENTIAL_DAY_SWITCH_STATE, false).apply()
            sharedPreferences.edit().putBoolean(FACENTIAL_NIGHT_SWITCH_STATE, false).apply()

            sharedPreferences.edit().putInt(FACENTIAL_LAST_RESET_DAY, currentDay).apply()
        }
    }

    fun setOnboardingFinished() {
        sharedPreferences.edit().putBoolean(FACENTIAL_ONBOARDING_FINISHED, true).apply()
    }

    fun isOnboardingFinished(): Boolean {
        return sharedPreferences.getBoolean(FACENTIAL_ONBOARDING_FINISHED, false)
    }

    companion object {
        private const val FACENTIAL_PREF = "facential_preferences"
        private const val FACENTIAL_ONBOARDING_FINISHED = "facential_onboarding_finished"
        private const val FACENTIAL_AUTH_TOKEN = "facential_auth_token"
        private const val FACENTIAL_DAY_SWITCH_STATE = "facential_day_switch_state"
        private const val FACENTIAL_NIGHT_SWITCH_STATE = "facential_night_switch_state"
        private const val FACENTIAL_LAST_RESET_DAY = "facential_last_reset_day"
    }
}