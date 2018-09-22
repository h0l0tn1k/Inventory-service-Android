package android.inventory.siemens.cz.siemensinventory.inventory

import android.app.Activity
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import java.util.*

class InventoryExpandableListAdapter(
        private val context : Activity,
        var devices : HashMap<String, List<Device>>
) : BaseExpandableListAdapter() {

    override fun getGroup(position: Int): List<Device> {
        return getGroupAtPosition(position)
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if(view == null) {
            view = context.layoutInflater.inflate(R.layout.expandable_list_group, null)
        }

        val groupNameTv = view?.findViewById(R.id.expandable_list_header) as TextView
        groupNameTv.text = "${getGroupKeyAtPosition(groupPosition)} (${getGroup(groupPosition).size})"

        return view
    }

    override fun getChildrenCount(position: Int): Int {
        return getGroupAtPosition(position).size
    }

    override fun getChild(groupPosition: Int, childPosititon: Int): Device {
        return getGroupAtPosition(groupPosition)[childPosititon]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if(view == null) {
            view = context.layoutInflater.inflate(R.layout.inventory_list_item, null)
        }

        val device = getChild(groupPosition, childPosition)

        val deviceNameTv = view?.findViewById(R.id.inventory_item_device_name) as TextView
        deviceNameTv.text = "${device.deviceType.objectTypeName} (${device.serialNumber})"

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return devices.size
    }

    fun updateList(devices : HashMap<String, List<Device>>) {
        this.devices = devices
        notifyDataSetChanged()
    }

    private fun getGroupKeyAtPosition(p : Int) : String {
        return this.devices.keys.toTypedArray()[p]
    }

    private fun getGroupAtPosition(p : Int) : List<Device> {
        return this.devices[getGroupKeyAtPosition(p)] as List<Device>
    }
}