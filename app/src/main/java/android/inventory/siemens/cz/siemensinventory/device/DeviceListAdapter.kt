package android.inventory.siemens.cz.siemensinventory.device

import android.app.Activity
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class DeviceListAdapter(
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
            view = LayoutInflater.from(context).inflate(R.layout.device_list_item, parent, false)
        }

        val deviceTypeTv = view?.findViewById(R.id.device_list_item_device_type) as TextView
        deviceTypeTv.text = device.getDeviceTypeName()

        val barcodeTv = view.findViewById(R.id.device_list_item_barcode_tv) as TextView
        barcodeTv.text = device.barcodeNumber

        val serialNumTv = view.findViewById(R.id.device_list_item_serial_number_tv) as TextView
        serialNumTv.text = device.serialNumber

        val locationTv = view.findViewById(R.id.device_list_item_location_tv) as TextView
        locationTv.text = device.defaultLocation

        val ownerTv = view.findViewById(R.id.device_list_item_owner_tv) as TextView
        ownerTv.text = device.getOwnerFullName()

        val holderTv = view.findViewById(R.id.device_list_item_holder_tv) as TextView
        holderTv.text = device.getHolderFullName()

        return view
    }

    override fun getItem(position: Int): Device = devices[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = devices.size
}