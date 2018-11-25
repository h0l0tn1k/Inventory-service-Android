package android.inventory.siemens.cz.siemensinventory.borrow

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.activities.ScanActivity
import android.inventory.siemens.cz.siemensinventory.api.LoginUsersScdApi
import android.inventory.siemens.cz.siemensinventory.api.ServiceApiGenerator
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.device.DeviceActivity
import android.inventory.siemens.cz.siemensinventory.device.DeviceIntent
import android.inventory.siemens.cz.siemensinventory.device.DeviceServiceApi
import android.inventory.siemens.cz.siemensinventory.tools.SnackBarNotifier
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.activity_borrow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BorrowActivity : AppCompatActivity() {

    private val SCAN_ACTIVITY_REQUEST_CODE = 0
    private val DEVICE_ACTIVITY_REQUEST_CODE = 1
    private val parameterName = "device_barcode_id"
    private var deviceApi: DeviceServiceApi? = null
    private var adapter = BorrowedDevicesAdapter(this, emptyList())
    private var snackBarNotifier : SnackBarNotifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrow)

        borrow_borrowed_devices_lv.adapter = adapter
        borrow_borrowed_devices_lv.setOnItemClickListener { adapterView, _, position, _ ->
            startDeviceActivity(adapterView.getItemAtPosition(position) as Device)
        }
        snackBarNotifier = SnackBarNotifier(borrow_device_swipe_refresh_layout, this)
        borrow_scanBtn.setOnClickListener { startScan() }

        deviceApi = ServiceApiGenerator.Factory.createService(DeviceServiceApi::class.java, AppData.accessToken, this)

        borrow_device_swipe_refresh_layout.setOnRefreshListener { loadBorrowedDevices() }
        loadBorrowedDevices()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SCAN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {
                    val device = Gson().fromJson(data.getStringExtra(parameterName), Device::class.java)
                    if (device == null) {
                        val barcode = data.getStringExtra("barcode")
                        snackBarNotifier?.show(getString(R.string.device_with_barcode_not_found, barcode))
                        return
                    }
                    startDeviceActivity(device)
                } catch(ex : JsonSyntaxException) {
                    displayDeviceNotFound()
                }
            }
        } else if (requestCode == DEVICE_ACTIVITY_REQUEST_CODE) {
            loadBorrowedDevices()
        }
    }

    private fun displayDeviceNotFound() {
        snackBarNotifier?.show(getString(R.string.no_device_found))
    }

    private fun startDeviceActivity(device : Device) {
        val deviceIntent = Intent(this@BorrowActivity, DeviceActivity::class.java)
        deviceIntent.putExtra("device", Gson().toJson(device))
        deviceIntent.putExtra("intent", DeviceIntent.BORROW.toString())
        startActivityForResult(deviceIntent, DEVICE_ACTIVITY_REQUEST_CODE)
    }

    private fun startDeviceActivity(response : Response<Device>) {
        val device = response.body()
        if(device != null) {
            startDeviceActivity(device)
        }
    }

    private fun hideProgressBar() {
        borrow_device_swipe_refresh_layout.isRefreshing = false
    }

    private fun showProgressBar() {
        borrow_device_swipe_refresh_layout.isRefreshing = true
    }

    private fun startScan() {
        val scanIntent = Intent(this, ScanActivity::class.java )
        scanIntent.putExtra("parameterName", parameterName)

        startActivityForResult(scanIntent, SCAN_ACTIVITY_REQUEST_CODE)
    }

    private fun onFailure() {
        hideProgressBar()
        snackBarNotifier?.show(getString(R.string.error_cannot_connect_to_service))
    }

    private fun onResponse(response: Response<Device>?) {
        if (response?.isSuccessful == true) {
            startDeviceActivity(response)
        } else {
            snackBarNotifier?.show(getResponseMessage(response))
        }
    }

    private fun getResponseMessage(response : Response<Device>?) : String {
        return if (response?.message() != null) {
            response.message()
        } else {
            response.toString()
        }
    }

    private fun loadBorrowedDevices() {
        showProgressBar()
        deviceApi?.getBorrowedDevicesByUserId(AppData.loginUserScd?.id)
                ?.enqueue(object : Callback<List<Device>> {
            override fun onResponse(call: Call<List<Device>>?, response: Response<List<Device>>?) {
                hideProgressBar()
                if(response?.isSuccessful == true) {
                    adapter.updateList(response.body() as List<Device>)
                }
            }
            override fun onFailure(call: Call<List<Device>>?, t: Throwable?) {
                this@BorrowActivity.onFailure()
            }
        })
    }
}
