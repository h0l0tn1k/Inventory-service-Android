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
            view = LayoutInflater.from(context).inflate(R.layout.borrowed_device_item, parent, false)
        }

        val deviceNameTv = view?.findViewById(R.id.borrowed_device_item_name_tv) as TextView
        deviceNameTv.text = device.deviceType.getDeviceTypeAndVersion()

        val serialNumberTv = view.findViewById(R.id.borrowed_device_item_serial_number_tv) as TextView
        serialNumberTv.text = device.serialNumber

        val projectNameTv = view.findViewById(R.id.borrowed_device_item_project_name_tv) as TextView
        projectNameTv.text = device.project.name

        val departmentNameTv = view.findViewById(R.id.borrowed_device_item_department_name_tv) as TextView
        departmentNameTv.text = device.department.name

        val deviceStateNameTv = view.findViewById(R.id.borrowed_device_item_device_state_tv) as TextView
        deviceStateNameTv.text = device.deviceState.name

        return view
    }

    override fun getItem(position: Int): Device = devices[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = devices.size
}