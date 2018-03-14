package android.inventory.siemens.cz.siemensinventory.activities

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.adapters.ProjectsAdapter
import android.inventory.siemens.cz.siemensinventory.api.DeviceServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_electric_revision)

        deviceApi = DeviceServiceApi.Factory.create(this)

        scanBtn.setOnClickListener { startScan() }
        manualEntryBtn.setOnClickListener { startManualScan() }
    }

    private fun startManualScan() {
        //TODO: validations
        val queue = deviceApi?.getDeviceBySerialNo(serialNoEditTxt.text.toString())
        queue?.enqueue(object : Callback<Device> {
            override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                val device = response?.body()
                if(device != null) {
                    startDeviceActivity(device)
                }
            }
            override fun onFailure(call: Call<Device>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun startScan() {
        val scanIntent = Intent(this, ScanActivity::class.java )
        scanIntent.putExtra("parameterName", parameterName)

        startActivityForResult(scanIntent, SCAN_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SCAN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val deviceBarcodeId = data.getStringExtra(parameterName)
                if(deviceBarcodeId != null && deviceBarcodeId.isNotEmpty()) {
                    //Toast.makeText(this, "Barcode is : '$deviceBarcodeId'", Toast.LENGTH_LONG).show()

                    val queue = deviceApi?.getDeviceByBarcodeId(deviceBarcodeId)
                    queue?.enqueue(object : Callback<Device> {
                        override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                            val device = response?.body() as Device?
                            if(device != null) {
                                startDeviceActivity(device)
                            }
                        }
                        override fun onFailure(call: Call<Device>?, t: Throwable?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                } else {
                    //TODO: ERROR
                }
            } else {
                //TODO: error try manually
            }
        }
    }

    private fun startDeviceActivity(device : Device) {
//        Toast.makeText(this@ElectricRevisionActivity,
//                "Device by barcode ID : '" + device.objectTypeName + " " + device.objectTypeVersion,
//                Toast.LENGTH_LONG).show()
        val deviceIntent = Intent(this@ElectricRevisionActivity, DeviceActivity::class.java)
        deviceIntent.putExtra("device", Gson().toJson(device))
        startActivity(deviceIntent)
    }
}
