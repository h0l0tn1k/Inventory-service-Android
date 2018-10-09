package android.inventory.siemens.cz.siemensinventory.activities

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.content.ClipData.Item
import android.graphics.Color
import android.widget.TextView


class DashboardItem(val navId: Int,
                    val stringId : Int,
                    val icon: Int,
                    val permission: String) {

}
// references to our images
private val mThumbIds = arrayOf<DashboardItem>(
        DashboardItem(R.id.nav_home, R.string.profile, R.drawable.ic_person_black_24dp, ""),
        DashboardItem(R.id.nav_borrow, R.string.borrow_device, R.drawable.ic_devices_black_24dp, ""),
        DashboardItem(R.id.nav_inventory, R.string.inventory, R.drawable.ic_visibility_black_24dp, ""),
        DashboardItem(R.id.nav_electric_revision, R.string.electric_revision, R.drawable.ic_flash_on_black_24dp, ""),
        DashboardItem(R.id.nav_calibration, R.string.calibration, R.drawable.ic_phonelink_setup_black_24dp, ""),
        DashboardItem(R.id.nav_suppliers, R.string.suppliers, R.drawable.ic_menu_share, ""),
        DashboardItem(R.id.nav_departments, R.string.departments, R.drawable.ic_menu_share, ""),
        DashboardItem(R.id.nav_company_owners, R.string.company_owners, R.drawable.ic_menu_share, ""),
        DashboardItem(R.id.nav_project, R.string.projects, R.drawable.ic_menu_share, ""),
        DashboardItem(R.id.nav_settings, R.string.settings, R.drawable.ic_settings_black_24dp, ""),
        DashboardItem(R.id.nav_logout, R.string.logout, R.drawable.ic_power_settings_new_black_24dp, ""),
        DashboardItem(R.id.nav_about, R.string.about, R.drawable.ic_info_black_24dp, "")
)

class DashboardAdapter(private val mContext: Context) : BaseAdapter() {

    override fun getCount(): Int = mThumbIds.size

    override fun getItem(position: Int): DashboardItem = mThumbIds[position]

    override fun getItemId(position: Int): Long = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val picture: ImageView
        val name: TextView
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.dashboard_item, parent, false)
            view.setTag(R.id.picture, view.findViewById(R.id.picture))
            view.setTag(R.id.text, view.findViewById(R.id.text))
        }

        picture = view?.getTag(R.id.picture) as ImageView
        name = view.getTag(R.id.text) as TextView

        val item = getItem(position)

        picture.setImageResource(item.icon)
//        picture.setBackgroundColor(Color.parseColor("#64B5F6"))
        name.text = mContext.getString(item.stringId)
        name.setBackgroundResource(R.color.colorPrimary)
        return view
    }
}