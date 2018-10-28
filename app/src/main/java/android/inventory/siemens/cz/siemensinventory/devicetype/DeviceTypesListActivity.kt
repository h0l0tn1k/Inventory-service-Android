package android.inventory.siemens.cz.siemensinventory.devicetype

import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceType
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.tools.SnackBarNotifier
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_device_type_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceTypesListActivity : AppCompatActivity(), SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private var deviceTypeApi : DeviceTypeServiceApi? = null
    private var snackBarNotifier: SnackBarNotifier? = null
    private var adapter : DeviceTypeListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_type_list)

        initLayoutElements()

        snackBarNotifier = SnackBarNotifier(device_type_list_layout, this)
        deviceTypeApi = DeviceTypeServiceApi.Factory.create(this)
    }

    private fun initLayoutElements() {
        adapter = DeviceTypeListAdapter(this, emptyList())
        device_type_search_box.setOnQueryTextListener(this)
        device_type_search_results.adapter = adapter
        device_type_search_results.onItemClickListener = AdapterView.OnItemClickListener { adapter, _, position, _ ->
            startDeviceTypeActivity(adapter.getItemAtPosition(position) as DeviceType)
        }
        device_type_addNew.visibility = if (AppData.loginUserScd?.flagWrite == true) View.VISIBLE else View.GONE
        device_type_addNew.setOnClickListener { startCreateDeviceTypeActivity() }
    }

    private fun showProgressBar() {
        device_type_progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        device_type_progress_bar.visibility = View.GONE
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onClose(): Boolean {
        return false
    }

    private fun updateResultsList(deviceTypes : List<DeviceType>) {
//        if(queryEmpty) {
//            //el_revision_results_text.visibility = View.GONE
//            device_type_search_results.visibility = View.GONE
//            device_type_no_results_text.visibility = View.GONE
//            adapter?.updateList(emptyList())
//            return
//        }

        //el_revision_results_text.visibility = View.VISIBLE

        if(deviceTypes.isEmpty()) {
            device_type_no_results_text.visibility = View.VISIBLE
            device_type_search_results.visibility = View.GONE
        } else {
            adapter?.updateList(deviceTypes)
            device_type_search_results.visibility = View.VISIBLE
            device_type_no_results_text.visibility = View.GONE
        }

        hideProgressBar()
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query.toString().isBlank()) {
            return false
        }
        val queue = deviceTypeApi?.getDeviceTypesByName(query.toString())
        showProgressBar()
        queue?.enqueue(object : Callback<List<DeviceType>> {
            override fun onResponse(call: Call<List<DeviceType>>?, response: Response<List<DeviceType>>?) {
                if(response?.isSuccessful == true) {
                    val deviceTypes = response.body() as List<DeviceType>
                    updateResultsList(deviceTypes)
                }
            }
            override fun onFailure(call: Call<List<DeviceType>>?, t: Throwable?) {
                this@DeviceTypesListActivity.onFailure()
                hideProgressBar()
            }
        })

        return false
    }

    private fun startDeviceTypeActivity(deviceType : DeviceType) {
        val deviceTypeIntent = Intent(this@DeviceTypesListActivity, DeviceTypeActivity::class.java)
        deviceTypeIntent.putExtra("deviceType", Gson().toJson(deviceType))
        deviceTypeIntent.putExtra("editMode", false)
        startActivity(deviceTypeIntent)
    }

    private fun startCreateDeviceTypeActivity() {
        val deviceTypeIntent = Intent(this@DeviceTypesListActivity, DeviceTypeActivity::class.java)
        deviceTypeIntent.putExtra("deviceType", Gson().toJson(DeviceType()))
        deviceTypeIntent.putExtra("editMode", true)
        deviceTypeIntent.putExtra("createMode", true)
        startActivity(deviceTypeIntent)
    }

    private fun onFailure() {
        snackBarNotifier?.show(getString(R.string.error_cannot_connect_to_service))
        hideProgressBar()
    }
}


