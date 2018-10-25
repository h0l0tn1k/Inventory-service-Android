package android.inventory.siemens.cz.siemensinventory.calibration

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.activities.ScanActivity
import android.inventory.siemens.cz.siemensinventory.api.CalibrationServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceCalibration
import android.inventory.siemens.cz.siemensinventory.device.DeviceActivity
import android.inventory.siemens.cz.siemensinventory.device.DeviceIntent
import android.inventory.siemens.cz.siemensinventory.device.DeviceServiceApi
import android.inventory.siemens.cz.siemensinventory.tools.SnackBarNotifier
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.activity_calibration.*
import kotlinx.android.synthetic.main.activity_inventory.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalibrationActivity : AppCompatActivity(),
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private val SCAN_ACTIVITY_REQUEST_CODE = 0
    private val DEVICE_ACTIVITY_REQUEST_CODE = 1
    private var snackBarNotifier: SnackBarNotifier? = null
    private val scanParameterName = "device"
    private var deviceApi: DeviceServiceApi? = null
    private var calibrationApi: CalibrationServiceApi? = null
    private var adapter: CalibrationListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibration)

        initLayoutElements()

        snackBarNotifier = SnackBarNotifier(calibration_layout, this)
        deviceApi = DeviceServiceApi.Factory.create(this)
        calibrationApi = CalibrationServiceApi.Factory.create(this)

        calibration_scanBtn.setOnClickListener { startScan() }
    }

    private fun initLayoutElements() {
        adapter = CalibrationListAdapter(this, emptyList())
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

        if (isSerialNumberValid(query)) {
            val queue = deviceApi?.getDevicesWithSerialOrBarcodeNumberLike(query.toString().trim())
            showProgressBar()
            queue?.enqueue(object : Callback<List<Device>> {
                override fun onResponse(call: Call<List<Device>>?, response: Response<List<Device>>?) {
                    if (response?.isSuccessful == true) {
                        val devices = response.body() as List<Device>
                        updateResultsList(devices, queryIsEmpty)
                    }
                }

                override fun onFailure(call: Call<List<Device>>?, t: Throwable?) {
                    this@CalibrationActivity.onFailure()
                    hideProgressBar()
                }
            })
        } else {
            updateResultsList(emptyList(), queryIsEmpty)
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SCAN_ACTIVITY_REQUEST_CODE -> handleScanActivityResult(resultCode, data)
            DEVICE_ACTIVITY_REQUEST_CODE -> handleDeviceActivityResult(resultCode, data)
        }
    }

    private fun handleScanActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            try {
                val device = Gson().fromJson(data.getStringExtra(scanParameterName), Device::class.java)
                if (device == null) {
                    val barcode = data.getStringExtra("barcode")
                    snackBarNotifier?.show(getString(R.string.device_with_barcode_not_found, barcode))
                } else {
                    startDeviceActivity(device)
                }
            } catch (ex: JsonSyntaxException) {
                snackBarNotifier?.show(getString(R.string.device_doesnt_exist))
            }
        }
    }

    private fun handleDeviceActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val deviceIntent = DeviceIntent.valueOf(data.getStringExtra("intent"))
            val device = Gson().fromJson(data.getStringExtra("result"), Device::class.java)
            if (deviceIntent == DeviceIntent.CREATE) {
                inventory_search_box.setQuery(device.serialNumber, true)
            } else if (deviceIntent == DeviceIntent.CALIBRATION) {
                val calibration = device.calibration as DeviceCalibration
                calibrationApi?.updateCalibration(calibration.id, calibration)?.enqueue(object : Callback<DeviceCalibration> {
                    override fun onFailure(call: Call<DeviceCalibration>?, t: Throwable?) {
                        this@CalibrationActivity.onFailure()
                    }
                    override fun onResponse(call: Call<DeviceCalibration>?, response: Response<DeviceCalibration>?) {
                        if (response?.isSuccessful == true) {
                            snackBarNotifier?.show(getString(R.string.able_to_save_changes))
                            onQueryTextChange(calibration_search_box.query.toString())
                        } else {
                            this@CalibrationActivity.onFailure()
                        }
                    }
                })
                deviceApi?.updateDevice(device.id, device)?.enqueue(object : Callback<Device> {
                    override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                        if (response?.isSuccessful != true) {
                            this@CalibrationActivity.onFailure()
                        }
                    }
                    override fun onFailure(call: Call<Device>?, t: Throwable?) {
                        this@CalibrationActivity.onFailure()
                    }
                })
            }
        }
    }

    private fun updateResultsList(devices: List<Device>, queryEmpty: Boolean) {
        if (queryEmpty) {
            //calibration_results_text.visibility = View.GONE
            calibration_search_results.visibility = View.GONE
            calibration_no_results_text.visibility = View.GONE
            adapter?.updateList(emptyList())
            return
        }

        //calibration_results_text.visibility = View.VISIBLE

        if (devices.isEmpty()) {
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
        snackBarNotifier?.show(getString(R.string.error_cannot_connect_to_service))
        hideProgressBar()
    }

    private fun startDeviceActivity(device: Device) {
        val deviceIntent = Intent(this@CalibrationActivity, DeviceActivity::class.java)
        deviceIntent.putExtra("device", Gson().toJson(device))
        deviceIntent.putExtra("intent", DeviceIntent.CALIBRATION.toString())
        startActivityForResult(deviceIntent, DEVICE_ACTIVITY_REQUEST_CODE)
    }

    private fun startScan() {
        val scanIntent = Intent(this, ScanActivity::class.java)
        scanIntent.putExtra("parameterName", scanParameterName)

        startActivityForResult(scanIntent, SCAN_ACTIVITY_REQUEST_CODE)
    }

    private fun isSerialNumberValid(query: String?): Boolean {
        return query?.isNotEmpty() == true
    }
}