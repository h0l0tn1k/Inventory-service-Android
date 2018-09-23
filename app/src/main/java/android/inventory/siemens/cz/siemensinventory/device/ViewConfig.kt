package android.inventory.siemens.cz.siemensinventory.device

import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.entity.KeyValueParameters

interface ViewConfig {

    fun getViewConfig(device: Device, edit: Boolean) : List<KeyValueParameters>
}