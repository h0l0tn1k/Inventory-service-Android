package android.inventory.siemens.cz.siemensinventory.activities

import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.adapters.DeviceParametersAdapter
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.entity.KeyValueParameters
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_device.*
import java.text.SimpleDateFormat
import java.util.*

class DeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val device = Gson().fromJson(intent.getStringExtra("device"), Device::class.java)

        val addDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(device.addDate)

        val parameters = listOf(
                KeyValueParameters("Barcode Number", device.barcodeNumber),
                KeyValueParameters("Device Type", device.objectTypeName),
                KeyValueParameters("Serial Number", device.serialNumber),
                KeyValueParameters("Owner", device.ownerName),
                KeyValueParameters("Department", device.departmentName),
                KeyValueParameters("Current Holder", device.holderName),
                KeyValueParameters("Project", device.projectName),
                KeyValueParameters("Company Owner", device.companyOwnerName),
                KeyValueParameters("Add Date", addDateString),
                KeyValueParameters("Status", device.deviceStateName)
        )

        deviceParameters.adapter = DeviceParametersAdapter(this, parameters)
    }
}
