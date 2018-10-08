package android.inventory.siemens.cz.siemensinventory.electricrevision

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

class ElectricRevisionListAdapter(
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
            view = LayoutInflater.from(context).inflate(R.layout.el_revision_list_item, parent, false)
        }

        val revisionStateIv = view?.findViewById(R.id.el_revision_item_state_iv) as ImageView
        revisionStateIv.setImageDrawable(device.revision.getDaysLeftIcon(context))

        val deviceNameTv = view.findViewById(R.id.el_revision_item_device_name) as TextView
        deviceNameTv.text = "${device.deviceType.getDeviceTypeAndVersion()} (${device.serialNumber})"

        val daysLeftTv = view.findViewById(R.id.el_revision_item_days_left_tv) as TextView
        val daysLeft = device.revision.getDaysLeft()
        daysLeftTv.text = if (daysLeft == null) "" else daysLeft.toString()
        daysLeftTv.setTextColor(device.revision.getDaysLeftColor(context))

        val lastRevisionDateTv = view.findViewById(R.id.el_revision_last_revision_date_tv) as TextView
        lastRevisionDateTv.text = device.revision.lastRevisionDateString

        val revisionPeriodTv = view.findViewById(R.id.el_revision_item_period_tv) as TextView
        revisionPeriodTv.text = device.revision.getPeriodString()

        return view
    }

    override fun getItem(position: Int): Device = devices[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = devices.size
}