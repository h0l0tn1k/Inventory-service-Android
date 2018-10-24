package android.inventory.siemens.cz.siemensinventory.calibration

import android.app.Activity
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceCalibration
import android.inventory.siemens.cz.siemensinventory.api.entity.InventoryState
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CalibrationListAdapter(
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
            view = LayoutInflater.from(context).inflate(R.layout.calibration_list_item, parent, false)
        }

        val calibrationStateIv = view?.findViewById(R.id.calibration_item_state_iv) as ImageView
        calibrationStateIv.setImageDrawable(device.calibration?.getDaysLeftIcon(context))

        val deviceNameTv = view.findViewById(R.id.calibration_item_device_name) as TextView
        deviceNameTv.text = "${device.getDeviceTypeName()} (SN: ${device.serialNumber})"

        val daysLeftTv = view.findViewById(R.id.calibration_item_days_left_tv) as TextView
        val daysLeft = device.calibration?.getDaysLeft()
        daysLeftTv.text = daysLeft?.toString() ?: ""
        val daysLeftColor = device.calibration?.getDaysLeftColor(context)
        if(daysLeftColor != null) {
            daysLeftTv.setTextColor(daysLeftColor)
        }

        val lastCalibrationDateTv = view.findViewById(R.id.calibration_item_last_calibration_date_tv) as TextView
        lastCalibrationDateTv.text = device.calibration?.lastCalibrationDateString

        val nextCalibrationDateTv = view.findViewById(R.id.calibration_item_next_calibration_date_tv) as TextView
        nextCalibrationDateTv.text = device.calibration?.getNextCalibrationDateString()

        val calibrationPeriodTv = view.findViewById(R.id.calibration_item_period_tv) as TextView
        calibrationPeriodTv.text = device.calibration?.getPeriodString()

        return view
    }

    override fun getItem(position: Int): Device = devices[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = devices.size
}