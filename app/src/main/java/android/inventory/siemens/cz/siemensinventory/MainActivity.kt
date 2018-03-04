package android.inventory.siemens.cz.siemensinventory

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.widget.TextView
import com.google.gson.Gson


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val LOGIN_ACTIVITY_REQUEST_CODE = 0
    private var user : LoginUserScd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        nav_view.setNavigationItemSelectedListener(this)

        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE)
    }

    // This method is called when the second activity finishes
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.user = Gson().fromJson(data.getStringExtra("user"), LoginUserScd::class.java)
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        setUserDetails()

        return true
    }

    private fun setUserDetails() {
        val nav_email = findViewById(R.id.nav_signed_in_email) as TextView
        nav_email.text = this.user?.email
        val nav_name = findViewById(R.id.nav_signed_in_name) as TextView
        nav_name.text = this.user?.getFullName()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        var intent = when (item.itemId) {
            R.id.nav_company_owners -> Intent(this, CompanyOwnersActivity::class.java)
            R.id.nav_project -> Intent(this, ProjectsActivity::class.java)
            else -> null
        }
        if(intent != null) startActivity(intent)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
