package android.inventory.siemens.cz.siemensinventory.activities

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.DeviceServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.tools.ProgressIndicator
import android.inventory.siemens.cz.siemensinventory.tools.SnackbarNotifier
import android.inventory.siemens.cz.siemensinventory.tools.TextViewHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bluehomestudio.progresswindow.ProgressWindow
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_electric_revision.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ElectricRevisionActivity : AppCompatActivity() {

    private val SCAN_ACTIVITY_REQUEST_CODE = 0
    private val parameterName = "device_barcode_id"
    private var deviceApi : DeviceServiceApi? = null
    private var progressIndicator : ProgressWindow? = null
    private var snackbarNotifier: SnackbarNotifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_electric_revision)

        snackbarNotifier = SnackbarNotifier(electric_revision_layout, this)
        deviceApi = DeviceServiceApi.Factory.create(this)

        scanBtn.setOnClickListener { startScan() }
        manualEntryBtn.setOnClickListener { startManualScan() }
    }

    private fun startManualScan() {

        if(isSerialNumberValid()) {
            getProgressIndicator().showProgress()

            //TODO: CHANGE -> NOT SERIAL NUMBER BUT EVIDENCE NUMBER !!!!!!!!!!!!!!
            val queue = deviceApi?.getDeviceBySerialNo(serialNoEditTxt.text.toString())
            queue?.enqueue( object : Callback<Device> {
                override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                    this@ElectricRevisionActivity.onResponse(response)
                }
                override fun onFailure(call: Call<Device>?, t: Throwable?) {
                    this@ElectricRevisionActivity.onFailure()
                }
            } )
        }
    }

    private fun getProgressIndicator() : ProgressWindow {
        if (progressIndicator == null) {
            progressIndicator = ProgressIndicator.Builder.create(this)
        }

        return progressIndicator as ProgressWindow
    }

    private fun startScan() {
        val scanIntent = Intent(this, ScanActivity::class.java )
        scanIntent.putExtra("parameterName", parameterName)

        startActivityForResult(scanIntent, SCAN_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SCAN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val deviceBarcodeId = data.getStringExtra(parameterName)
                if(deviceBarcodeId != null && deviceBarcodeId.isNotEmpty()) {

                    val queue = deviceApi?.getDeviceByBarcodeId(deviceBarcodeId)
                    getProgressIndicator().showProgress()
                    queue?.enqueue(object : Callback<Device> {
                        override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                            this@ElectricRevisionActivity.onResponse(response)
                        }
                        override fun onFailure(call: Call<Device>?, t: Throwable?) {
                            this@ElectricRevisionActivity.onFailure()
                        }
                    })
                } else {
                    snackbarNotifier?.show(getString(R.string.unable_to_scan))
                }
            } else {
                snackbarNotifier?.show(getString(R.string.unable_to_scan))
            }
        }
    }

    private fun startDeviceActivity(device : Device) {
        val deviceIntent = Intent(this@ElectricRevisionActivity, DeviceActivity::class.java)
        deviceIntent.putExtra("device", Gson().toJson(device))
        startActivity(deviceIntent)
    }

    private fun onFailure() {
        snackbarNotifier?.show(getString(R.string.error_cannot_connect_to_service))
        getProgressIndicator().hideProgress()
    }

    private fun onResponse(response: Response<Device>?) {
        getProgressIndicator().hideProgress()
        if (response?.isSuccessful == true) {
            val device = response.body()
            if(device != null) {
                startDeviceActivity(device)
            }
        } else {
            val responseMessage: String = if (response?.message() != null) {
                            response.message()
                        } else {
                            response.toString()
                        }
            snackbarNotifier?.show(responseMessage)
        }
    }

    private fun isSerialNumberValid() : Boolean {
        return TextViewHelper().withContext(this).isNotEmpty(serialNoEditTxt, getString(R.string.serial_number_empty)).isValid
    }
}


