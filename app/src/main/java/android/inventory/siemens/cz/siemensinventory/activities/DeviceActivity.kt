package android.inventory.siemens.cz.siemensinventory.activities

import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.adapters.DeviceParametersAdapter
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.entity.KeyValueParameters
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import kotlinx.android.synthetic.main.activity_device.*
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import android.inventory.siemens.cz.siemensinventory.dialogs.FailedElectricRevisionDialog
import android.inventory.siemens.cz.siemensinventory.dialogs.PassedElectricRevisionDialog

class DeviceActivity : AppCompatActivity() {

    private var rfaContent : RapidFloatingActionContentLabelList? = null
    private var rfabHelper : RapidFloatingActionHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val device = Gson().fromJson(intent.getStringExtra("device"), Device::class.java)

        device_passed_btn.setOnClickListener { PassedElectricRevisionDialog().showDialog(this) }
        device_failed_btn.setOnClickListener { FailedElectricRevisionDialog().showDialog(this) }

        //val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        //val addDateString = dateFormatter.format(device.addDate)
        //val lastRevDateString = dateFormatter.format(device.lastRevisionDateString)

        val parameters = listOf(
                KeyValueParameters("Barcode Number", device.barcodeNumber),
                KeyValueParameters("Device Type", device.objectTypeName),
                KeyValueParameters("Serial Number", device.serialNumber),
                KeyValueParameters("Owner", device.ownerName),
                KeyValueParameters("Department", device.departmentName),
                KeyValueParameters("Current Holder", device.holderName),
                KeyValueParameters("Project", device.projectName),
                KeyValueParameters("Company Owner", device.companyOwnerName),
                //KeyValueParameters("Add Date", addDateString),
                KeyValueParameters("Status", device.deviceStateName),
                KeyValueParameters("Last Revision Date", device.lastRevisionDateString)
        )

        deviceParameters.adapter = DeviceParametersAdapter(this, parameters)

    }
}
