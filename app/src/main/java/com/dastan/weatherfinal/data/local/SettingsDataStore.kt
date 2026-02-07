package com.dastan.weatherfinal.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dastan.weatherfinal.domain.model.Units
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    private val UNITS_KEY = stringPreferencesKey("units")

    val unitsFlow: Flow<Units> = context.dataStore.data.map { prefs ->
        when (prefs[UNITS_KEY]) {
            "F" -> Units.F
            else -> Units.C
        }
    }

    suspend fun saveUnits(units: Units) {
        context.dataStore.edit { prefs ->
            prefs[UNITS_KEY] = if (units == Units.F) "F" else "C"
        }
    }
}
