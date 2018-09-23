package android.inventory.siemens.cz.siemensinventory.device

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.entity.KeyValueParameters

class InventoryViewConfiguration(var context: Context) : ViewConfig {

    override fun getViewConfig(device: Device, edit : Boolean): List<KeyValueParameters> {
        return listOf(
//            KeyValueParameters(context.getString(R.string.qr_code_value), device.barcodeNumber, false),
//            KeyValueParameters(context.getString(R.string.device_type_and_version), device.deviceType.getDeviceTypeAndVersion(), false),
//            KeyValueParameters(context.getString(R.string.serial_number), device.serialNumber, false),
//            KeyValueParameters(context.getString(R.string.owner), device.getOwnerFullName()),
        )
    }
}