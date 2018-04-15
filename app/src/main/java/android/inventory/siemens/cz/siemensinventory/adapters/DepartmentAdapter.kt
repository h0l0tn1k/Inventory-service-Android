package android.inventory.siemens.cz.siemensinventory.adapters

/**
 * Created by Stefan Matta on 03-Mar-18.
 */

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Department
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class DepartmentAdapter(
        private val context: Context,
        private val departments: List<Department>
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val department = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val nameTextView = view?.findViewById(R.id.list_item_name) as TextView
        nameTextView.text = department.name

        return view
    }

    override fun getItem(position: Int): Department = departments[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = departments.size

}
