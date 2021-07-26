package desoft.studio.webpoint.sharedStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.io.IOException
import java.util.concurrent.Flow

class KusAdsManager(val ctx : Context) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "interAdsPref");
    companion object{
        val interAdsKey  = booleanPreferencesKey("showInterAds");
    }

    suspend fun SetInterAds(value: Boolean){
        ctx.dataStore.edit { pref ->
            pref[interAdsKey] = value;
        }
    }
    //
    val showInterAds : kotlinx.coroutines.flow.Flow<Boolean> = ctx.dataStore.data
        .catch { exception ->
            if(exception is IOException)
            {
                exception.printStackTrace();
                emit(emptyPreferences());
            } else {
                throw exception;
            }
        }
        .map { pref ->
            val showrenot =  pref[interAdsKey] ?: false ;
            showrenot;
        }
/*    suspend fun showInterAds() : Boolean? {
        val pref = ctx.dataStore.data.first();
        return pref[interAdsKey];
    }*/
}