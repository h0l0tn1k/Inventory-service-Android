package android.inventory.siemens.cz.siemensinventory.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.adapters.ProjectsAdapter
import android.inventory.siemens.cz.siemensinventory.api.DeviceServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import android.inventory.siemens.cz.siemensinventory.tools.ProgressIndicator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.res.TypedArrayUtils
import android.support.v4.content.res.TypedArrayUtils.getText
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bluehomestudio.progresswindow.ProgressWindow
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_electric_revision.*
import kotlinx.android.synthetic.main.activity_projects.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ElectricRevisionActivity : AppCompatActivity() {

    private val SCAN_ACTIVITY_REQUEST_CODE = 0
    private val parameterName = "device_barcode_id"
    private var deviceApi : DeviceServiceApi? = null
    private var progressIndicator : ProgressWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_electric_revision)

        deviceApi = DeviceServiceApi.Factory.create(this)

        scanBtn.setOnClickListener { startScan() }
        manualEntryBtn.setOnClickListener { startManualScan() }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    private fun startManualScan() {
        getProgressIndicator().showProgress()
        //TODO: add validations

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
                    showSnackbar(getString(R.string.unable_to_scan))
                }
            } else {
                showSnackbar(getString(R.string.unable_to_scan))
            }
        }
    }

    private fun startDeviceActivity(device : Device) {
        val deviceIntent = Intent(this@ElectricRevisionActivity, DeviceActivity::class.java)
        deviceIntent.putExtra("device", Gson().toJson(device))
        startActivity(deviceIntent)
    }

    private fun onFailure() {
        showSnackbar(getString(R.string.error_cannot_connect_to_service))
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
            showSnackbar(responseMessage)
        }
    }

    private fun showSnackbar(text : String) {
        hideKeyboard()
        Snackbar.make(electric_revision_layout, text, Snackbar.LENGTH_LONG).show()
    }
}


