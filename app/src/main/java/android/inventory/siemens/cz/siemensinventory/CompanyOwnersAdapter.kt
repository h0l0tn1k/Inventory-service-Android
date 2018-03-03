package android.inventory.siemens.cz.siemensinventory

/**
 * Created by Stefan on 03-Mar-18.
 */

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.entity.GenericEntity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CompanyOwnersAdapter(
        private val context: Context,
        private val companyOwners: List<GenericEntity>
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val companyOwner = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        view?.findViewById<TextView>(R.id.list_item_id)?.text = companyOwner.id.toString()
        view?.findViewById<TextView>(R.id.list_item_name)?.text = companyOwner.name

        return view
    }

    override fun getItem(position: Int): GenericEntity = companyOwners[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = companyOwners.size

}
