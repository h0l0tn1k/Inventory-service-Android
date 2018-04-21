package android.inventory.siemens.cz.siemensinventory.borrow

import android.app.Activity
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class BorrowedDevicesAdapter(
        private val context : Activity,
        private var devices : List<Device>
) : BaseAdapter (){

    fun updateList(devices : List<Device>) {
        this.devices = devices
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val device = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val nameTextView = view?.findViewById(R.id.list_item_name) as TextView
        nameTextView.text = device.objectTypeName + " " + device.objectTypeVersion

        return view
    }

    override fun getItem(position: Int): Device = devices[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = devices.size
}