package android.inventory.siemens.cz.siemensinventory.activities

import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_device.*
import retrofit2.converter.gson.GsonConverterFactory

class DeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        var device = Gson().fromJson(intent.getStringExtra("device"), Device::class.java)
        fillData(device)
    }

    private fun fillData(device : Device) {
        //TODO: deviceInventoryStateTxtView
        deviceOwnerTxtView.text = device.ownerName
        deviceTypeTxtView.text = device.objectTypeName
        deviceVersionTxtView.text = device.objectTypeVersion
        deviceBarcodeTxtView.text = device.barcodeNumber
        deviceSerialNoTxtView.text = device.serialNumber
    }
}
