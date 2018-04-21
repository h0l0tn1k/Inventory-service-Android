package android.inventory.siemens.cz.siemensinventory.borrow

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.activities.ScanActivity
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.device.DeviceActivity
import android.inventory.siemens.cz.siemensinventory.device.DeviceIntent
import android.inventory.siemens.cz.siemensinventory.device.DeviceServiceApi
import android.inventory.siemens.cz.siemensinventory.tools.SnackbarNotifier
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_borrow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BorrowActivity : AppCompatActivity() {

    private val SCAN_ACTIVITY_REQUEST_CODE = 0
    private val parameterName = "device_barcode_id"
    private var deviceApi : DeviceServiceApi? = null
    private var adapter : BorrowedDevicesAdapter? = null
    private var snackbarNotifier: SnackbarNotifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrow)

        adapter = BorrowedDevicesAdapter(this, emptyList())
        borrow_borrowed_devices_lv.adapter = adapter
        snackbarNotifier = SnackbarNotifier(borrow_layout, this)
        deviceApi = DeviceServiceApi.Factory.create(this)
        borrow_scanBtn.setOnClickListener { startScan() }
        getDevicesMock()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SCAN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val deviceBarcodeId = data.getStringExtra(parameterName)
                if(deviceBarcodeId != null && deviceBarcodeId.isNotEmpty()) {
                    val queue = deviceApi?.getDeviceByBarcodeId(deviceBarcodeId)
                    queue?.enqueue(object : Callback<Device> {
                        override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                            this@BorrowActivity.onResponse(response)
                        }
                        override fun onFailure(call: Call<Device>?, t: Throwable?) {
                            this@BorrowActivity.onFailure()
                        }
                    })
                } else {
                    snackbarNotifier?.show(getString(R.string.unable_to_scan))
                }
            }
        }
    }

    private fun startDeviceActivity(device : Device) {
        val deviceIntent = Intent(this@BorrowActivity, DeviceActivity::class.java)
        deviceIntent.putExtra("device", Gson().toJson(device))
        deviceIntent.putExtra("intent", DeviceIntent.BORROW.toString())
        startActivity(deviceIntent)
    }

    private fun startDeviceActivity(response : Response<Device>) {
        val device = response.body()
        if(device != null) {
            startDeviceActivity(device)
        }
    }

    private fun startScan() {
        val scanIntent = Intent(this, ScanActivity::class.java )
        scanIntent.putExtra("parameterName", parameterName)

        startActivityForResult(scanIntent, SCAN_ACTIVITY_REQUEST_CODE)
    }

    private fun onFailure() {
        snackbarNotifier?.show(getString(R.string.error_cannot_connect_to_service))
    }

    private fun onResponse(response: Response<Device>?) {
        if (response?.isSuccessful == true) {
            startDeviceActivity(response)
        } else {
            snackbarNotifier?.show(getResponseMessage(response))
        }
    }

    private fun getResponseMessage(response : Response<Device>?) : String {
        return if (response?.message() != null) {
            response.message()
        } else {
            response.toString()
        }
    }

    //TODO: fix, this is only for testing
    //returning all devices instead of devices
    //that are currently borrowed by current user
    private fun getDevicesMock() {
        val queue = deviceApi?.getDevices()
        queue?.enqueue(object : Callback<List<Device>> {
            override fun onResponse(call: Call<List<Device>>?, response: Response<List<Device>>?) {
                if(response?.isSuccessful == true) {
                    adapter?.updateList(response.body() as List<Device>)
                }
            }
            override fun onFailure(call: Call<List<Device>>?, t: Throwable?) {
                this@BorrowActivity.onFailure()
            }
        })
    }
}
