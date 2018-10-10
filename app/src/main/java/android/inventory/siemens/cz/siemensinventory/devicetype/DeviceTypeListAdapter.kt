package android.inventory.siemens.cz.siemensinventory.devicetype

import android.app.Activity
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class DeviceTypeListAdapter(
        private val context: Activity,
        private var deviceTypes: List<DeviceType>
) : BaseAdapter() {

    fun updateList(deviceTypes: List<DeviceType>) {
        this.deviceTypes = deviceTypes
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val deviceType = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.device_type_list_item, parent, false)
        }

        val deviceTypeTv = view?.findViewById(R.id.device_type_list_item_device_type) as TextView
        deviceTypeTv.text = deviceType.objectTypeName

        val manufacturerTv = view.findViewById(R.id.device_type_list_item_manufacturer) as TextView
        manufacturerTv.text = deviceType.manufacturer

        val orderNumTv = view.findViewById(R.id.device_type_list_item_order_num) as TextView
        orderNumTv.text = deviceType.orderNumber

        val versionTv = view.findViewById(R.id.device_type_list_item_version) as TextView
        versionTv.text = deviceType.version

        val supplierTv = view.findViewById(R.id.device_type_list_item_supplier) as TextView
        supplierTv.text = deviceType.supplier?.name

        val priceTv = view.findViewById(R.id.device_type_list_item_price) as TextView
        priceTv.text = "${deviceType.price} CZK"

        return view
    }

    override fun getItem(position: Int): DeviceType = deviceTypes[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = deviceTypes.size
}