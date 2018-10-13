package android.inventory.siemens.cz.siemensinventory.inventory

import android.app.Activity
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.InventoryState
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class InventoryListAdapter(
        private val context: Activity,
        private var devices: List<Device>
) : BaseAdapter() {

    fun updateList(devices: List<Device>) {
        this.devices = devices
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val device = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.inventory_list_item, parent, false)
        }

        val inventoryStateIv = view?.findViewById(R.id.inventory_item_inventory_state_iv) as ImageView
        inventoryStateIv.setImageDrawable(context.getDrawable(getInventoryStateIcon(device.inventoryRecord.inventoryState)))

        val deviceNameTv = view.findViewById(R.id.inventory_item_device_name) as TextView
        deviceNameTv.text = "${device.deviceType.getDeviceTypeAndVersion()} (${device.serialNumber})"

        val locationTv = view.findViewById(R.id.device_item_location_tv) as TextView
        locationTv.text = device.defaultLocation

        val departmentTv = view.findViewById(R.id.device_item_department_tv) as TextView
        departmentTv.text = device.getDepartmentName()

        return view
    }

    private fun getInventoryStateIcon(inventoryState: InventoryState): Int {
        return when (inventoryState.state) {
            "OK" -> R.drawable.ic_check_green_a700_24dp
            "False" -> R.drawable.ic_close_red_800_24dp
            "Unclear" -> R.drawable.ic_help_yellow_800_24dp
            else -> R.drawable.ic_help_yellow_800_24dp
        }
    }

    override fun getItem(position: Int): Device = devices[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = devices.size
}