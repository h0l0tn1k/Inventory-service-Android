package android.inventory.siemens.cz.siemensinventory.activities

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.SiemensServiceApi
import android.inventory.siemens.cz.siemensinventory.entity.ServiceSettings
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.preference.Preference
import android.preference.PreferenceFragment
import android.view.MenuItem
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, GeneralPreferenceFragment()).commit()
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class GeneralPreferenceFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

        private var serviceSettings : ServiceSettings? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
            setHasOptionsMenu(true)

            serviceSettings = ServiceSettings(this.context)

            initPreferences()
            setListeners()
        }

        override fun onResume() {
            super.onResume()
            preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            findPreference(key).summary = sharedPreferences?.getString(key, "")
            serviceSettings?.serviceUrl = serviceSettings?.getServiceUrlFormatted().toString()
            findPreference("serviceUrl").summary = serviceSettings?.getServiceUrlFormatted()
        }

        private fun initPreferences() {
            findPreference("httpMethod").summary = serviceSettings?.httpMethod
            findPreference("httpMethod").setDefaultValue(serviceSettings?.httpMethod)
            findPreference("baseUrl").summary = serviceSettings?.baseUrl
            findPreference("baseUrl").setDefaultValue(serviceSettings?.baseUrl)
            findPreference("port").summary = serviceSettings?.port.toString()
            findPreference("port").setDefaultValue(serviceSettings?.port)
            findPreference("path").summary = serviceSettings?.path
            findPreference("path").setDefaultValue(serviceSettings?.path)
            findPreference("serviceUrl").summary = serviceSettings?.getServiceUrlFormatted()
            findPreference("serviceUrl").setDefaultValue(serviceSettings?.getServiceUrlFormatted())
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }

        private fun setListeners() {
            findPreference("testConnectionToService").onPreferenceClickListener = Preference.OnPreferenceClickListener {
                //TODO:


                serviceSettings?.checkConnection()?.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if(response?.isSuccessful != null && response.isSuccessful) {
                            Toast.makeText(this@GeneralPreferenceFragment.context, "Connection is successful!", Toast.LENGTH_LONG ).show()
                        } else {
                            Toast.makeText(this@GeneralPreferenceFragment.context, "Connection is unsuccessful!", Toast.LENGTH_LONG ).show()
                        }

                    }

                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Toast.makeText(this@GeneralPreferenceFragment.context, "Connection is unsuccessful!", Toast.LENGTH_LONG ).show()
                    }
                })


                true
            }
        }
    }

}
