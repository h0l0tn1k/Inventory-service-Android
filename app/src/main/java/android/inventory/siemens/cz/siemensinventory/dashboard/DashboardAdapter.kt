package android.inventory.siemens.cz.siemensinventory.dashboard

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


class DashboardItem(val navId: Int, val stringId: Int, val icon: Int, var visible: Boolean)

class DashboardAdapter(private val mContext: Context) : BaseAdapter() {

    private var dashItemBorrow = DashboardItem(R.id.nav_borrow, R.string.borrow_device, R.drawable.borrow_480, false)
    private var dashItemInventory = DashboardItem(R.id.nav_inventory, R.string.inventory, R.drawable.eye_480, false)
    private var dashItemElRevision = DashboardItem(R.id.nav_electric_revision, R.string.electric_revision, R.drawable.flash_480, false)
    private var dashItemCalibration = DashboardItem(R.id.nav_calibration, R.string.calibration, R.drawable.calibration_480, false)
    private var dashItemDeviceType = DashboardItem(R.id.nav_device_type, R.string.device_types, R.drawable.devices_480, false)
    private var dashItemSuppliers = DashboardItem(R.id.nav_suppliers, R.string.suppliers, R.drawable.suppliers_480, false)
    private var dashItemDevices = DashboardItem(R.id.nav_devices, R.string.devices, R.drawable.electrical_480, false)
    private var dashItemDepartment = DashboardItem(R.id.nav_departments, R.string.departments, R.drawable.department_480, false)
    private var dashItemCompanyOwner = DashboardItem(R.id.nav_company_owners, R.string.company_owners, R.drawable.team_480, false)
    private var dashItemProject = DashboardItem(R.id.nav_project, R.string.projects, R.drawable.project_480, false)

    private val mThumbIds = arrayListOf(
            DashboardItem(R.id.nav_home, R.string.profile, R.drawable.user_480, true),
            dashItemBorrow, dashItemInventory, dashItemElRevision, dashItemCalibration,
            dashItemDevices, dashItemDeviceType,
            dashItemSuppliers, dashItemDepartment, dashItemCompanyOwner, dashItemProject,
            DashboardItem(R.id.nav_settings, R.string.settings, R.drawable.settings_480, true),
            DashboardItem(R.id.nav_logout, R.string.logout, R.drawable.logout_480, true)
    )

    init {
        setVisibility()
    }

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
        name.text = mContext.getString(item.stringId)
        name.setBackgroundResource(R.color.colorPrimary)
        return view
    }

    private fun setVisibility() {
        val user = AppData.loginUserScd as LoginUserScd
        dashItemBorrow.visible = user.flagBorrow
        dashItemInventory.visible = user.flagInventory
        dashItemElRevision.visible = user.flagRevision
        dashItemCalibration.visible = user.flagRevision
        dashItemDevices.visible = user.flagRead
        dashItemDeviceType.visible = user.flagRead
        dashItemSuppliers.visible = user.flagRead
        dashItemDepartment.visible = user.flagRead
        dashItemCompanyOwner.visible = user.flagRead
        dashItemProject.visible = user.flagRead
    }
}