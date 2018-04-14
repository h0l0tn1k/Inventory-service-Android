package android.inventory.siemens.cz.siemensinventory.inventory

import android.app.Activity
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.InventoryRecord
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import java.util.*

class InventoryExpandableListAdapter(
        private val context : Activity,
        var inventoryRecords : HashMap<String, List<InventoryRecord>>
) : BaseExpandableListAdapter() {

    override fun getGroup(position: Int): List<InventoryRecord> {
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

    override fun getChild(groupPosition: Int, childPosititon: Int): InventoryRecord {
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

        val device = getChild(groupPosition, childPosition).deviceInventory

        val deviceNameTv = view?.findViewById(R.id.inventory_item_device_name) as TextView
        deviceNameTv.text = "${device.objectTypeName} (${device.serialNumber})"


        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return inventoryRecords.size
    }

    fun updateList(inventoryRecords : HashMap<String, List<InventoryRecord>>) {
        this.inventoryRecords = inventoryRecords
        notifyDataSetChanged()
    }

//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val inventoryRecord = getItem(position)
//
//        var view = convertView
//
//        if (view == null) {
//            view = LayoutInflater.from(context).inflate(R.layout.inventory_list_item, parent, false)
//        }
//
//        val deviceNameTv = view?.findViewById(R.id.inventory_item_device_name) as TextView
//
//        return view
//    }

    private fun getGroupKeyAtPosition(p : Int) : String {
        return this.inventoryRecords.keys.toTypedArray()[p]
    }

    private fun getGroupAtPosition(p : Int) : List<InventoryRecord> {
        return this.inventoryRecords[getGroupKeyAtPosition(p)] as List<InventoryRecord>
    }
}