package android.inventory.siemens.cz.siemensinventory.activities

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.DeviceStatesServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceState
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.inventory.siemens.cz.siemensinventory.borrow.BorrowActivity
import android.inventory.siemens.cz.siemensinventory.calibration.CalibrationActivity
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.electricrevision.ElectricRevisionActivity
import android.inventory.siemens.cz.siemensinventory.entity.Permission
import android.inventory.siemens.cz.siemensinventory.inventory.InventoryActivity
import android.inventory.siemens.cz.siemensinventory.view.ViewEntityActivity
import android.inventory.siemens.cz.siemensinventory.view.ViewType
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val LOGIN_ACTIVITY_REQUEST_CODE = 0
    private var deviceStateApi: DeviceStatesServiceApi? = null
    private var dashboardAdapter: DashboardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (getUser() == null) {
            startLoginActivity()
        }
        val gridview: GridView = findViewById(R.id.main_dashboard)
        dashboardAdapter = DashboardAdapter(this)
        gridview.adapter = dashboardAdapter

        gridview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            startActivityBasedOnNavItem(dashboardAdapter?.getItem(position)?.navId)
        }
//        profileChangePasswordBtn.setOnClickListener { startChangePasswordActivity() }

        initStaticData()
    }

    private fun initStaticData() {
        deviceStateApi = DeviceStatesServiceApi.Factory.create(this)
        deviceStateApi?.getDeviceStates()?.enqueue(object : Callback<List<DeviceState>> {
            override fun onResponse(call: Call<List<DeviceState>>?, response: Response<List<DeviceState>>?) {
                if (response?.isSuccessful == true) {
                    AppData.deviceStates = response.body()
                }
            }

            override fun onFailure(call: Call<List<DeviceState>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Error while loading device states", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                AppData.loginUserScd = Gson().fromJson(data.getStringExtra("user"), LoginUserScd::class.java)
            }
        }
    }

    private fun startActivityBasedOnNavItem(navItemId: Int?) {
        val intent = when (navItemId) {
            //Activities
            R.id.nav_borrow -> Intent(this, BorrowActivity::class.java)
            R.id.nav_inventory -> Intent(this, InventoryActivity::class.java)
            R.id.nav_electric_revision -> Intent(this, ElectricRevisionActivity::class.java)
            R.id.nav_calibration -> Intent(this, CalibrationActivity::class.java)
            //R.id.nav_user_permissions -> Intent(this, EditUserPermissionsActivity::class.java)

            //Views
            R.id.nav_suppliers, R.id.nav_departments, R.id.nav_company_owners, R.id.nav_project -> {
                getViewEntityActivityIntent(navItemId)
            }

            //Others
            R.id.nav_settings -> Intent(this, SettingsActivity::class.java)
            R.id.nav_logout -> {
                AppData.loginUserScd = null
                Intent(this, LoginActivity::class.java)
            }
            //TODO add About
            else -> null
        }
        if (intent != null) startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        setUserDetails()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        //todo finish permissions
        menu?.findItem(R.id.nav_borrow_group)?.isVisible = getUser()?.flagBorrow == true
        menu?.findItem(R.id.nav_inventory_group)?.isVisible = getUser()?.flagInventory == true
        menu?.findItem(R.id.nav_electric_revision_group)?.isVisible = getUser()?.flagRevision == true
        menu?.findItem(R.id.nav_calibration_group)?.isVisible = getUser()?.flagRevision == true
        //menu?.findItem(R.id.nav_user_permissions_group)?.isVisible = getUser()?.flagAdmin == true
        return super.onPrepareOptionsMenu(menu)
    }

    private fun startChangePasswordActivity() {
        startActivity(Intent(this, ChangePasswordActivity::class.java))
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE)
    }

    private fun setUserDetails() {

        val permissions = listOf(
                Permission("Read-only", getUser()?.flagRead),
                Permission("Edit", getUser()?.flagWrite),
                Permission("Borrowing", getUser()?.flagBorrow),
                Permission("Inventory-making", getUser()?.flagInventory),
                Permission("Revision-making", getUser()?.flagRevision),
                Permission("Admin", getUser()?.flagAdmin)
        )

//        permissionsView.adapter = PermissionsAdapter(this, permissions)
    }

    private fun getUser(): LoginUserScd? {
        return AppData.loginUserScd
    }

    private fun getViewEntityActivityIntent(id: Int): Intent {
        val intent = Intent(this, ViewEntityActivity::class.java)
        val viewType = when (id) {
            R.id.nav_project -> ViewType.Projects
            R.id.nav_departments -> ViewType.Departments
            R.id.nav_company_owners -> ViewType.CompanyOwners
            R.id.nav_suppliers -> ViewType.Suppliers
            else -> {
                //FIX
                ViewType.Projects
            }
        }
        intent.putExtra("viewtype", viewType.toString())

        return intent
    }
}
