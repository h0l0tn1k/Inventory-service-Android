package android.inventory.siemens.cz.siemensinventory.view

import android.inventory.siemens.cz.siemensinventory.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ViewEntityAdapter(
        private val context: ViewEntityActivity,
        private var items: List<ViewEntity>
) : BaseAdapter() {

    fun updateList(items : List<ViewEntity>) {
        this.items = items
        notifyDataSetChanged()
        context.hideProgressBar()
    }

    fun getList() : List<ViewEntity> {
        return this.items
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val item = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val nameTextView = view?.findViewById(R.id.list_item_name) as TextView
        nameTextView.text = item.name

        return view
    }

    override fun getItem(position: Int): ViewEntity = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

}