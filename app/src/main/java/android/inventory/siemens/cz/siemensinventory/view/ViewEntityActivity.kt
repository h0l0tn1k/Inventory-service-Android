package android.inventory.siemens.cz.siemensinventory.view

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.inventory.siemens.cz.siemensinventory.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import kotlinx.android.synthetic.main.activity_view_entity.*

class ViewEntityActivity : AppCompatActivity() {

    private var adapter : ViewEntityAdapter? = null
    private var viewType : ViewType? = null
    private var dataProvider : ViewEntityDataProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_entity)

        initView()

        loadData()
    }

    private fun initView() {
        createSwipeMenu()
        setView()

        adapter = ViewEntityAdapter(this, emptyList())
        view_generic_list_view.adapter = getAdapter()
        dataProvider = ViewEntityDataProvider(this, getViewType(), getAdapter())
        view_entity_layout.setOnRefreshListener { loadData() }
    }

    fun hideProgressBar() {
        view_entity_layout.isRefreshing = false
    }

    private fun loadData() {
        dataProvider?.getData()
    }

    private fun setView() {
        when(getViewType()) {
            ViewType.Departments -> setTitle(R.string.departments)
            ViewType.CompanyOwners -> setTitle(R.string.company_owners)
            ViewType.Suppliers -> setTitle(R.string.suppliers)
            ViewType.Projects -> setTitle(R.string.projects)
        }
    }

    private fun getAdapter() : ViewEntityAdapter {
        return adapter as ViewEntityAdapter
    }

    private fun getViewType() : ViewType {
        if(viewType == null) {
            try {
                viewType = ViewType.valueOf(intent.getStringExtra("viewtype"))
            } catch (ex : IllegalArgumentException) {
                throw IllegalArgumentException("Variable 'viewtype' is not specified.")
            }
        }

        return viewType as ViewType
    }

    private fun createSwipeMenu() {
        val creator = SwipeMenuCreator { menu ->

            val editItem = SwipeMenuItem(applicationContext)
            editItem.background = ColorDrawable(Color.rgb(0x00, 0x66,0xff))
            editItem.width = dipToPixels(60)
            editItem.setIcon(R.drawable.ic_edit_black_24dp)

            val deleteItem = SwipeMenuItem(applicationContext)
            deleteItem.background = ColorDrawable(Color.rgb(0xF9,0x3F, 0x25))
            deleteItem.width =  dipToPixels(60)
            deleteItem.setIcon(R.drawable.ic_delete_black_24dp)

            // add to menu
            menu.addMenuItem(editItem)
            menu.addMenuItem(deleteItem)
        }

        view_generic_list_view.setMenuCreator(creator)

        view_generic_list_view.setOnMenuItemClickListener({ position, _, index ->
            when (index) {
                0 -> EditEntityDialog().showDialog(this, getAdapter().getItem(position))
                1 -> DeleteEntityDialog().showDialog(this, getAdapter().getItem(position))
            }
            // false : close the menu; true : not close the menu
            false
        })
    }

    private fun dipToPixels(dipValue: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue.toFloat(), Resources.getSystem().displayMetrics).toInt()
    }
}
