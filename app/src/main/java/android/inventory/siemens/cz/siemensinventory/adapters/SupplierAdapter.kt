package android.inventory.siemens.cz.siemensinventory.adapters

/**
 * Created by Stefan on 03-Mar-18.
 */

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Supplier
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SupplierAdapter(
        private val context: Context,
        private val items: List<Supplier>
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val item = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        //val idTextView = view?.findViewById(R.id.list_item_id) as TextView
        //idTextView.text = item.id.toString()
        val nameTextView = view?.findViewById(R.id.list_item_name) as TextView
        nameTextView.text = item.name

        return view
    }

    override fun getItem(position: Int): Supplier = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

}
