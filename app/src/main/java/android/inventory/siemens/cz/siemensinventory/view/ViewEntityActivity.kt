package android.inventory.siemens.cz.siemensinventory.view

import android.inventory.siemens.cz.siemensinventory.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_view_entity.*

class ViewEntityActivity : AppCompatActivity() {

    private var adapter: ViewEntityAdapter? = null
    private var viewType: ViewType? = null
    private var dataProvider: ViewEntityDataProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_entity)

        initView()

        loadData()
    }

    private fun initView() {
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
                viewType = ViewType.valueOf(intent.getStringExtra("viewtype"))
            } catch (ex: IllegalArgumentException) {
                throw IllegalArgumentException("Variable 'viewtype' is not specified.")
            }
        }

        return viewType as ViewType
    }
}