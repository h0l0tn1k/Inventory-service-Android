package android.inventory.siemens.cz.siemensinventory.activities

import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.DeviceServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_calibration.*
import kotlinx.android.synthetic.main.activity_electric_revision.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalibrationActivity : AppCompatActivity() {

    private val SCAN_ACTIVITY_REQUEST_CODE = 1
    private val parameterName = "device_barcode_id"
    private var deviceApi : DeviceServiceApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibration)

        deviceApi = DeviceServiceApi.Factory.create(this)

        calibration_scanBtn.setOnClickListener { startScan() }
    }

    private fun startDeviceActivity(device : Device) {
        val deviceIntent = Intent(this@CalibrationActivity, DeviceActivity::class.java)
        deviceIntent.putExtra("device", Gson().toJson(device))
        startActivity(deviceIntent)
    }

//    private fun startManualScan() {
//        //TODO: add validations
//
//        val queue = deviceApi?.getDeviceBySerialNo(serialNoEditTxt.text.toString())
//        queue?.enqueue(object : Callback<Device> {
//            override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
//                val device = response?.body()
//                if(device != null) {
//                    startDeviceActivity(device)
//                }
//            }
//            override fun onFailure(call: Call<Device>?, t: Throwable?) {
//                Toast.makeText(this@CalibrationActivity, getText(R.string.error_cannot_connect_to_service), Toast.LENGTH_LONG).show()
//            }
//        })
//    }

    private fun startScan() {
        val scanIntent = Intent(this, ScanActivity::class.java )
        scanIntent.putExtra("parameterName", parameterName)

        startActivityForResult(scanIntent, SCAN_ACTIVITY_REQUEST_CODE)
    }
}
