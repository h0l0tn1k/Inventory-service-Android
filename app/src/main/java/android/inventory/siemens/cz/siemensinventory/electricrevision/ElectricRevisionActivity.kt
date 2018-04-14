package android.inventory.siemens.cz.siemensinventory.electricrevision

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.device.DeviceActivity
import android.inventory.siemens.cz.siemensinventory.activities.ScanActivity
import android.inventory.siemens.cz.siemensinventory.device.DeviceServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.device.DeviceIntent
import android.inventory.siemens.cz.siemensinventory.tools.SnackbarNotifier
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_electric_revision.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ElectricRevisionActivity : AppCompatActivity(), SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private val SCAN_ACTIVITY_REQUEST_CODE = 0
    private val parameterName = "device_barcode_id"
    private var deviceApi : DeviceServiceApi? = null
    private var snackbarNotifier: SnackbarNotifier? = null
    private var adapter : ElectricRevisionResultsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_electric_revision)

        initLayoutElements()

        snackbarNotifier = SnackbarNotifier(electric_revision_layout, this)
        deviceApi = DeviceServiceApi.Factory.create(this)

        el_revision_scanBtn.setOnClickListener { startScan() }
    }

    private fun initLayoutElements() {
        adapter = ElectricRevisionResultsAdapter(this, emptyList())
        el_revision_search_box.setOnQueryTextListener(this)
        el_revision_search_results.adapter = adapter
        el_revision_search_results.onItemClickListener = AdapterView.OnItemClickListener { adapter, _, position, _ ->
            startDeviceActivity(adapter.getItemAtPosition(position) as Device)
        }
    }

    private fun showProgressBar() {
        el_revision_progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        el_revision_progress_bar.visibility = View.GONE
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onClose(): Boolean {
        return false
    }

    private fun updateResultsList(devices : List<Device>, queryEmpty : Boolean) {
        if(queryEmpty) {
            //el_revision_results_text.visibility = View.GONE
            el_revision_search_results.visibility = View.GONE
            el_revision_no_results_text.visibility = View.GONE
            adapter?.updateList(emptyList())
            return
        }

        //el_revision_results_text.visibility = View.VISIBLE

        if(devices.isEmpty()) {
            el_revision_no_results_text.visibility = View.VISIBLE
            el_revision_search_results.visibility = View.GONE
        } else {
            adapter?.updateList(devices)
            el_revision_search_results.visibility = View.VISIBLE
            el_revision_no_results_text.visibility = View.GONE
        }

        hideProgressBar()
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
                    this@ElectricRevisionActivity.onFailure()
                    hideProgressBar()
                }
            } )
        } else {
            updateResultsList(emptyList(), queryIsEmpty)
        }

        return false
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
                    showProgressBar()
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
            }
        }
    }

    private fun startDeviceActivity(device : Device) {
        val deviceIntent = Intent(this@ElectricRevisionActivity, DeviceActivity::class.java)
        deviceIntent.putExtra("device", Gson().toJson(device))
        deviceIntent.putExtra("intent", DeviceIntent.EL_REVISION.toString())
        startActivity(deviceIntent)
    }

    private fun startDeviceActivity(response : Response<Device>) {
        val device = response.body()
        if(device != null) {
            startDeviceActivity(device)
        }
    }

    private fun onFailure() {
        snackbarNotifier?.show(getString(R.string.error_cannot_connect_to_service))
        hideProgressBar()
    }

    private fun onResponse(response: Response<Device>?) {
        hideProgressBar()
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

    private fun isSerialNumberValid(query : String?) : Boolean {
        return query?.isNotEmpty() == true
    }
}


