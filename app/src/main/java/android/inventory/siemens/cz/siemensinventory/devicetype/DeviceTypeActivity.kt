package android.inventory.siemens.cz.siemensinventory.devicetype

import android.databinding.DataBindingUtil
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.DeviceTypeServiceApi
import android.inventory.siemens.cz.siemensinventory.api.SupplierServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceType
import android.inventory.siemens.cz.siemensinventory.api.entity.Supplier
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.databinding.ActivityDeviceType2Binding
import android.inventory.siemens.cz.siemensinventory.tools.SnackbarNotifier
import android.inventory.siemens.cz.siemensinventory.view.ViewMode
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_device_type2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceTypeActivity : AppCompatActivity() {

    private var deviceTypeBinding: ActivityDeviceType2Binding? = null
    private var deviceTypeApi: DeviceTypeServiceApi? = null
    private var supplierApi: SupplierServiceApi? = null
    private val suppliers = arrayListOf<Supplier>()
    private var snackbarNotifier: SnackbarNotifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_type2)

        deviceTypeBinding = DataBindingUtil.setContentView(this, R.layout.activity_device_type2) as ActivityDeviceType2Binding
        deviceTypeBinding?.deviceType = getDeviceTypeFromIntent()
        deviceTypeBinding?.viewMode = ViewMode(isEditMode())
        deviceTypeApi = DeviceTypeServiceApi.Factory.create(this)
        supplierApi = SupplierServiceApi.Factory.create(this)
        snackbarNotifier = SnackbarNotifier(activity_device_layout, this)

        loadSuppliers()
        initListeners()
    }

    private fun initListeners() {
        device_type_edit_btn.setOnClickListener {
            changeViewMode(true, getDeviceTypeFromIntent())
        }
        device_type_cancel_btn.setOnClickListener {
//            reset to previous values
            if(isCreateMode()) { finish() }
            changeViewMode(false, getDeviceTypeFromIntent())
        }
        device_type_save_btn.setOnClickListener {
            val deviceType = deviceTypeBinding?.deviceType as DeviceType
            deviceType.price = deviceTypeBinding?.deviceTypeEditPrice?.text.toString().toDouble()
            deviceType.supplier = device_type_edit_supplier.selectedItem as Supplier
            if (isCreateMode()) {
                deviceTypeApi?.createDeviceType(deviceType)?.enqueue(callback())
            } else {
                //update
                deviceTypeApi?.updateDeviceType(deviceType.id, deviceType)?.enqueue(callback())
            }
        }
        device_type_close_btn.setOnClickListener { finish() }
    }

    private fun callback() : Callback<DeviceType> {
        return object : Callback<DeviceType> {
            override fun onFailure(call: Call<DeviceType?>, t: Throwable?) {
                snackbarNotifier?.show(getString(R.string.unable_to_save_changes))
            }
            override fun onResponse(call: Call<DeviceType>?, response: Response<DeviceType>?) {
                if (response?.isSuccessful == true) {
                    changeViewMode(false, response.body())
                    snackbarNotifier?.show(getString(R.string.able_to_save_changes))
                } else {
                    snackbarNotifier?.show(getString(R.string.unable_to_save_changes))
                }
            }
        }
    }

    private fun loadSuppliers() {
        supplierApi?.getSuppliers()?.enqueue(object : Callback<List<Supplier>> {
            override fun onFailure(call: Call<List<Supplier>>?, t: Throwable?) {
                snackbarNotifier?.show(getString(R.string.error_loading_data))
            }
            override fun onResponse(call: Call<List<Supplier>>?, response: Response<List<Supplier>>?) {
                if (response?.isSuccessful == true) {
                    if (response.body() != null) {
                        suppliers.addAll(response.body() as List<Supplier>)
                    }
                    device_type_edit_supplier.adapter = ArrayAdapter<Supplier>(this@DeviceTypeActivity,
                            android.R.layout.simple_spinner_dropdown_item, suppliers)
                    device_type_edit_supplier.setSelection(suppliers.indexOf(deviceTypeBinding?.deviceType?.supplier))
                } else {
                    snackbarNotifier?.show(getString(R.string.error_loading_data))
                }
            }
        })
    }

    private fun getDeviceTypeFromIntent(): DeviceType? {
        return Gson().fromJson(intent.getStringExtra("deviceType"), DeviceType::class.java)
    }

    private fun isEditMode(): Boolean {
        return intent.getBooleanExtra("editMode", false)
    }

    private fun isCreateMode(): Boolean {
        return intent.getBooleanExtra("createMode", false)
    }

    private fun changeViewMode(editMode: Boolean, devType: DeviceType?) {
        intent.putExtra("deviceType", Gson().toJson(devType))
        intent.putExtra("editMode", editMode)
        recreate()
    }
}
