package android.inventory.siemens.cz.siemensinventory.activities

import android.annotation.TargetApi
import android.content.Intent
import android.content.SharedPreferences
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.entity.ServiceSettings
import android.inventory.siemens.cz.siemensinventory.tools.SnackBarNotifier
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsActivity : AppCompatActivity() {

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
        private var snackbarNotifier : SnackBarNotifier? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
            setHasOptionsMenu(true)

            serviceSettings = ServiceSettings(this.context)
            snackbarNotifier = SnackBarNotifier(activity.findViewById(android.R.id.content), this.activity)

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

            val clientId = serviceSettings?.clientId
            val clientIdPreference = findPreference("clientId")
            if(clientId == null || clientId.toString().isBlank()) {
                clientIdPreference.icon = context.getDrawable(R.drawable.ic_close_red_800_24dp)
            } else {
                clientIdPreference.icon = context.getDrawable(R.drawable.ic_check_green_a700_24dp)
            }
            clientIdPreference.setDefaultValue(serviceSettings?.clientId)

            val clientSecret = serviceSettings?.clientSecret
            val clientSecretPreference = findPreference("clientSecret")
            if(clientSecret == null || clientSecret.toString().isBlank()) {
                clientSecretPreference.icon = context.getDrawable(R.drawable.ic_close_red_800_24dp)
            } else {
                clientSecretPreference.icon = context.getDrawable(R.drawable.ic_check_green_a700_24dp)
            }
            clientSecretPreference.setDefaultValue(serviceSettings?.clientSecret)
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

                if(serviceSettings?.isUrlWellFormatted() == true) {
                    serviceSettings?.checkConnection()?.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                            if(response?.isSuccessful == true) {
                                snackbarNotifier?.show(context.getString(R.string.connection_successful))
                            } else {
                                snackbarNotifier?.show(context.getString(R.string.connection_not_successful))
                            }
                        }
                        override fun onFailure(call: Call<Void>?, t: Throwable?) {
                            snackbarNotifier?.show(context.getString(R.string.connection_not_successful))
                        }
                    })
                } else {
                    snackbarNotifier?.show(getString(R.string.service_url_not_valid))
                }
                true
            }
        }
    }

}
