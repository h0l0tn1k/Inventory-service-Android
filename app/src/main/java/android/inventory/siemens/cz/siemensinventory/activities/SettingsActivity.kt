package android.inventory.siemens.cz.siemensinventory.activities

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.view.MenuItem
import android.widget.Toast


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
    class GeneralPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
            setHasOptionsMenu(true)

            setListeners()
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
                Toast.makeText(this.context, "Check connection is successful TODO!!!!!", Toast.LENGTH_LONG ).show()
                true
            }
        }
    }

}
