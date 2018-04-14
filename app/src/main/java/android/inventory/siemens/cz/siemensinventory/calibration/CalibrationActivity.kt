package android.inventory.siemens.cz.siemensinventory.calibration

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.activities.ScanActivity
import android.inventory.siemens.cz.siemensinventory.device.DeviceServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.device.DeviceActivity
import android.inventory.siemens.cz.siemensinventory.device.DeviceIntent
import android.inventory.siemens.cz.siemensinventory.electricrevision.ElectricRevisionResultsAdapter
import android.inventory.siemens.cz.siemensinventory.tools.SnackbarNotifier
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_calibration.*
import kotlinx.android.synthetic.main.activity_electric_revision.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalibrationActivity : AppCompatActivity(), SearchView.OnQueryTextListener, SearchView.OnCloseListener  {

    private val SCAN_ACTIVITY_REQUEST_CODE = 1
    private var snackbarNotifier: SnackbarNotifier? = null
    private val parameterName = "device_barcode_id"
    private var deviceApi : DeviceServiceApi? = null
    private var adapter : ElectricRevisionResultsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibration)

        initLayoutElements()

        snackbarNotifier = SnackbarNotifier(calibration_layout, this)
        deviceApi = DeviceServiceApi.Factory.create(this)

        calibration_scanBtn.setOnClickListener { startScan() }
    }

    private fun initLayoutElements() {
        adapter = ElectricRevisionResultsAdapter(this, emptyList())
        calibration_search_box.setOnQueryTextListener(this)
        calibration_search_results.adapter = adapter
        calibration_search_results.onItemClickListener = AdapterView.OnItemClickListener { adapter, _, position, _ ->
            startDeviceActivity(adapter.getItemAtPosition(position) as Device)
        }
    }

    override fun onClose(): Boolean {
        return false
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        val queryIsEmpty = query?.isEmpty() == true

        if(isSerialNumberValid(query)) {
            val queue = deviceApi?.getDevicesWithSerialNoLike(query.toString().trim())
            showProgressBar()
            queue?.enqueue( object : Callback<List<Device>> {
                override fun onResponse(call: Call<List<Device>>?, response: Response<List<Device>>?) {
                    if(response?.isSuccessful == true) {
                        val devices = response.body() as List<Device>
                        updateResultsList(devices, queryIsEmpty)
                    }
                }
                override fun onFailure(call: Call<List<Device>>?, t: Throwable?) {
                    this@CalibrationActivity.onFailure()
                    hideProgressBar()
                }
            } )
        } else {
            updateResultsList(emptyList(), queryIsEmpty)
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SCAN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val deviceBarcodeId = data.getStringExtra(parameterName)
                if(deviceBarcodeId != null && deviceBarcodeId.isNotEmpty()) {
                    val queue = deviceApi?.getDeviceByBarcodeId(deviceBarcodeId)
                    showProgressBar()
                    queue?.enqueue(object : Callback<Device> {
                        override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                            this@CalibrationActivity.onResponse(response)
                        }
                        override fun onFailure(call: Call<Device>?, t: Throwable?) {
                            this@CalibrationActivity.onFailure()
                        }
                    })
                } else {
                    snackbarNotifier?.show(getString(R.string.unable_to_scan))
                }
            }
        }
    }


    private fun updateResultsList(devices : List<Device>, queryEmpty : Boolean) {
        if(queryEmpty) {
            //calibration_results_text.visibility = View.GONE
            calibration_search_results.visibility = View.GONE
            calibration_no_results_text.visibility = View.GONE
            adapter?.updateList(emptyList())
            return
        }

        //calibration_results_text.visibility = View.VISIBLE

        if(devices.isEmpty()) {
            calibration_no_results_text.visibility = View.VISIBLE
            calibration_search_results.visibility = View.GONE
        } else {
            adapter?.updateList(devices)
            calibration_search_results.visibility = View.VISIBLE
            calibration_no_results_text.visibility = View.GONE
        }

        hideProgressBar()
    }

    private fun showProgressBar() {
        calibration_progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        calibration_progress_bar.visibility = View.GONE
    }

    private fun onFailure() {
        snackbarNotifier?.show(getString(R.string.error_cannot_connect_to_service))
        hideProgressBar()
    }

    private fun onResponse(response: Response<Device>?) {
        hideProgressBar()
        if (response?.isSuccessful == true) {
            if(response.body() != null) {
                startDeviceActivity(response.body() as Device)
            }
        } else {
            snackbarNotifier?.show(getResponseMessage(response))
        }
    }

    private fun startDeviceActivity(device : Device) {
        val deviceIntent = Intent(this@CalibrationActivity, DeviceActivity::class.java)
        deviceIntent.putExtra("device", Gson().toJson(device))
        deviceIntent.putExtra("intent", DeviceIntent.CALIBRATION.toString())
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

    private fun getResponseMessage(response : Response<Device>?) : String {
        return if (response?.message() != null) {
            response.message()
        } else {
            response.toString()
        }
    }

    private fun isSerialNumberValid(query : String?) : Boolean {
        return query?.isNotEmpty() == true
    }
}
