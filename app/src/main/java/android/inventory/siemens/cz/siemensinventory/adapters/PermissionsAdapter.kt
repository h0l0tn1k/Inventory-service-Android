package android.inventory.siemens.cz.siemensinventory.adapters

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.entity.Permission
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/**
 * Created by Stefan on 06-Mar-18.
 */
class PermissionsAdapter(
        private val context: Context,
        private val permissions: List<Permission>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val permission = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.permission_item, parent, false)
        }

        val permissionNameView = view?.findViewById(R.id.permissionName) as TextView
        permissionNameView.text = permission.name

        val permissionValueView = view?.findViewById(R.id.permissionValue) as TextView
        permissionValueView.text = permission.value.toString()

        return view
    }

    override fun getItem(position: Int): Permission = permissions[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = permissions.size

}