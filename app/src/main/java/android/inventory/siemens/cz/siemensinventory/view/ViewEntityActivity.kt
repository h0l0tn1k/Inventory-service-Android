package android.inventory.siemens.cz.siemensinventory.view

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_view_entity.*
import android.widget.EditText
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.DialogInterface
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


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
        view_generic_addNew.visibility = if (AppData.loginUserScd?.flagWrite == true) View.VISIBLE else View.GONE
        view_generic_addNew.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getDialogTitleString())
            val viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_new_generic_entity, null)
            val input = viewInflated.findViewById(R.id.input) as EditText
            input.hint = getDialogInputHintString()
            input.requestFocus()
            builder.setView(viewInflated)
            builder.setPositiveButton(getString(R.string.create)) { _, _ ->
                //todo validation
                dataProvider?.createData(GenericNameEntity(input.text.toString()))
            }
            builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
            builder.show()
        }
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

    private fun getDialogTitleString() : String {
        return when (getViewType()) {
            ViewType.Departments -> getString(R.string.new_department)
            ViewType.CompanyOwners -> getString(R.string.new_company_owner)
            ViewType.Suppliers -> getString(R.string.new_supplier)
            ViewType.Projects -> getString(R.string.new_project)
        }
    }

    private fun getDialogInputHintString() : String {
        return when (getViewType()) {
            ViewType.Departments -> getString(R.string.department)
            ViewType.CompanyOwners -> getString(R.string.company_owner)
            ViewType.Suppliers -> getString(R.string.supplier)
            ViewType.Projects -> getString(R.string.project)
        }
    }
}