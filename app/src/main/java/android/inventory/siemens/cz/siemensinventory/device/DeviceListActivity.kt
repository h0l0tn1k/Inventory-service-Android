package android.inventory.siemens.cz.siemensinventory.device

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.activities.ScanActivity
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.tools.SnackBarNotifier
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.activity_device_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceListActivity : AppCompatActivity(), SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private val SCAN_ACTIVITY_REQUEST_CODE = 0
    private val DEVICE_ACTIVITY_REQUEST_CODE = 1
    private val scanParameterName = "device"
    private var deviceApi: DeviceServiceApi? = null
    private var snackBarNotifier: SnackBarNotifier? = null
    private var adapter: DeviceListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)

        snackBarNotifier = SnackBarNotifier(device_list_layout, this)
        deviceApi = DeviceServiceApi.Factory.create(this)
        initLayoutElements()

        loadDevices()

        device_scanBtn.setOnClickListener { startScan() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SCAN_ACTIVITY_REQUEST_CODE -> handleScanActivityResult(resultCode, data)
            DEVICE_ACTIVITY_REQUEST_CODE -> handleDeviceActivityResult(resultCode, data)
        }
    }

    override fun onClose(): Boolean = false

    override fun onQueryTextSubmit(p0: String?): Boolean = false

    override fun onQueryTextChange(p0: String?): Boolean {
        val query = device_search_box.query.toString()
        loadDevices(query)
        return false
    }

    private fun loadDevices(query: String = "") {
        val queue = if(query.trim().isEmpty()) {
            deviceApi?.getDevices()
        } else {
            deviceApi?.getDevicesWithSerialOrBarcodeNumberLike(query.trim())
        }
        showProgressBar()
        queue?.enqueue(object : Callback<List<Device>> {
            override fun onResponse(call: Call<List<Device>>?, response: Response<List<Device>>?) {
                if (response?.isSuccessful == true) {
                    val devices = response.body() as List<Device>

                    updateResultsList(devices)
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<List<Device>>?, t: Throwable?) {
                this@DeviceListActivity.onFailure()
                hideProgressBar()
            }
        })
    }

    private fun updateResultsList(devices: List<Device>) {
        adapter?.updateList(devices)
        //todo
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
        //todo
    }

    private fun initLayoutElements() {
        adapter = DeviceListAdapter(this, emptyList())
        device_search_box.setOnQueryTextListener(this)
        device_search_results.adapter = adapter
        device_search_results.onItemClickListener = AdapterView.OnItemClickListener { adapter, _, position, _ ->
            startDeviceActivity(adapter.getItemAtPosition(position) as Device)
        }
    }

    private fun startDeviceActivity(device: Device) {
        val deviceIntent = Intent(this@DeviceListActivity, DeviceActivity::class.java)
        deviceIntent.putExtra("device", Gson().toJson(device))
        deviceIntent.putExtra("intent", DeviceIntent.DISPLAY.toString())
        startActivityForResult(deviceIntent, DEVICE_ACTIVITY_REQUEST_CODE)
    }

    private fun startScan() {
        val scanIntent = Intent(this, ScanActivity::class.java)
        scanIntent.putExtra("parameterName", scanParameterName)
        startActivityForResult(scanIntent, SCAN_ACTIVITY_REQUEST_CODE)
    }

    private fun onFailure() {
        snackBarNotifier?.show(getString(R.string.error_loading_data))
        hideProgressBar()
    }

    private fun showProgressBar() {
        device_progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        device_progress_bar.visibility = View.GONE
    }
}
