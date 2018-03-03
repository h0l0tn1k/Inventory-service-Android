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

class ProjectsAdapter(
        private val context: Context,
        private val items: List<GenericEntity>
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val item = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        view?.findViewById<TextView>(R.id.list_item_id)?.text = item.id.toString()
        view?.findViewById<TextView>(R.id.list_item_name)?.text = item.name

        return view
    }

    override fun getItem(position: Int): GenericEntity = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

}
