package android.inventory.siemens.cz.siemensinventory.view

import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.tools.SnackBarNotifier
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_view_entity.*


class ViewEntityActivity : AppCompatActivity() {

    private var adapter: ViewEntityAdapter? = null
    private var viewType: ViewType? = null
    private var dataProvider: ViewEntityDataProvider? = null
    private var snackBarNotifier: SnackBarNotifier? = null
    private val NEW_EDIT_ENTITY_ACTIVITY_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_entity)

        initView()

        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_EDIT_ENTITY_ACTIVITY_REQUEST_CODE) {
            loadData()
        }
    }

    private fun initView() {
        setView()
        snackBarNotifier = SnackBarNotifier(generic_entity_layout, this)
        view_generic_addNew.visibility = if (AppData.loginUserScd?.flagWrite == true) View.VISIBLE else View.GONE
        view_generic_addNew.setOnClickListener {
            startCreateEditEntity()
        }
        adapter = ViewEntityAdapter(this, emptyList())
        view_generic_list_view.adapter = getAdapter()
        view_generic_list_view.onItemClickListener = AdapterView.OnItemClickListener { adapter, _, position, _ ->
            startEntityActivity(adapter?.getItemAtPosition(position) as ViewEntity)
        }
        dataProvider = ViewEntityDataProvider(this, getViewType(), getAdapter())
        view_entity_layout.setOnRefreshListener { loadData() }
    }

    private fun startCreateEditEntity() {
        val newEditEntityIntent = Intent(this, CreateEditNewEntityActivity::class.java)
        newEditEntityIntent.putExtra("viewType", getViewType().toString())
        newEditEntityIntent.putExtra("editMode", true)
        newEditEntityIntent.putExtra("createMode", true)
        newEditEntityIntent.putExtra("existingEntityNames", adapter?.getList()?.map { x -> x.name }?.toTypedArray())
        startActivityForResult(newEditEntityIntent, NEW_EDIT_ENTITY_ACTIVITY_REQUEST_CODE)
    }

    private fun startEntityActivity(entity: ViewEntity) {
        val newEditEntityIntent = Intent(this, CreateEditNewEntityActivity::class.java)
        newEditEntityIntent.putExtra("viewType", getViewType().toString())
        newEditEntityIntent.putExtra("entity", Gson().toJson(entity))
        newEditEntityIntent.putExtra("existingEntityNames", adapter?.getList()?.map { x -> x.name }?.toTypedArray())
        startActivityForResult(newEditEntityIntent, NEW_EDIT_ENTITY_ACTIVITY_REQUEST_CODE)
    }

    fun hideProgressBar() {
        view_entity_layout.isRefreshing = false
    }

    private fun loadData() {
        dataProvider?.getData()
    }

    private fun setView() {
        when (getViewType()) {
            ViewType.Departments -> setTitle(R.string.departments)
            ViewType.CompanyOwners -> setTitle(R.string.company_owners)
            ViewType.Suppliers -> setTitle(R.string.suppliers)
            ViewType.Projects -> setTitle(R.string.projects)
        }
    }

    private fun getAdapter(): ViewEntityAdapter {
        return adapter as ViewEntityAdapter
    }

    private fun getViewType(): ViewType {
        if (viewType == null) {
            try {
                viewType = ViewType.valueOf(intent.getStringExtra("viewType"))
            } catch (ex: IllegalArgumentException) {
                throw IllegalArgumentException("Variable 'viewType' is not specified.")
            }
        }

        return viewType as ViewType
    }
}