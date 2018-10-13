package android.inventory.siemens.cz.siemensinventory.view

import android.databinding.DataBindingUtil
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceType
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.databinding.ActivityCreateEditNewEntityBinding
import android.inventory.siemens.cz.siemensinventory.tools.SnackbarNotifier
import android.inventory.siemens.cz.siemensinventory.tools.TextViewHelper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_edit_new_entity.*

class CreateEditNewEntityActivity : AppCompatActivity() {

    private var entityBinding: ActivityCreateEditNewEntityBinding? = null
    private var viewType: ViewType? = null
    private var snackbarNotifier: SnackbarNotifier? = null
    private var dataProvider: EditEntityDataProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_edit_new_entity)

        dataProvider = EditEntityDataProvider(this, getViewType())
        entityBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_edit_new_entity) as ActivityCreateEditNewEntityBinding
        entityBinding?.viewMode = ViewMode(isEditMode())
        snackbarNotifier = SnackbarNotifier(activity_create_edit_layout, this)

        initView()
    }

    private fun initView() {
        activity_create_edit_title.text = getTitleString()
        activity_create_edit_name.hint = getNameHintString()
        activity_create_edit_name.setText(if (isCreateMode()) "" else getEntity()?.name)
        activity_create_read_name.text = getEntity()?.name

        generic_entity_edit_btn.setOnClickListener { changeViewMode(true, getEntity()) }
        generic_entity_close_btn.setOnClickListener { finish() }
        generic_entity_save_btn.setOnClickListener {
            if(isCreateMode()) {
                if (isNewEntityNameValid()) {
                    dataProvider?.createData(GenericNameEntity(activity_create_edit_name.text.toString().trim()))
                }
            } else if (isEditMode()) {
                //todo validation
                val entity = getEntity() as ViewEntity
                entity.name = activity_create_edit_name.text.toString()
                dataProvider?.editData(entity)
            }
        }
        generic_entity_delete_btn.setOnClickListener {
            DeleteEntityDialog().showDialog(this, getEntity() as ViewEntity, dataProvider)
        }
        generic_entity_cancel_btn.setOnClickListener {
            if (isCreateMode()) {
                finish()
            } else if (isEditMode()) {
                changeViewMode(false, getEntity())
            }
        }
    }

    private fun isNewEntityNameValid(): Boolean {
        val newEntityName = activity_create_edit_name.text.toString().trim()
        val helper = TextViewHelper().withContext(this)
        helper.isNotEmpty(activity_create_edit_name, getString(R.string.name_cannot_be_empty))

        val alreadyExists = getExistingEntities()?.any { entityName -> entityName.trim().toLowerCase() == newEntityName.toLowerCase() }
        if (alreadyExists == true) {
            helper.setErrorAndShake(activity_create_edit_name, getString(R.string.entity_already_exists, newEntityName))
        }

        return helper.isValid
    }

    private fun changeViewMode(editMode: Boolean, entity: ViewEntity?) {
        intent.putExtra("viewType", getViewType().toString())
        intent.putExtra("entity", Gson().toJson(entity))
        intent.putExtra("editMode", editMode)
        recreate()
    }

    private fun getEntity() : ViewEntity? {
        return Gson().fromJson(intent.getStringExtra("entity"), ViewEntity::class.java)
    }

    private fun isEditMode(): Boolean {
        return intent.getBooleanExtra("editMode", false)
    }

    private fun isCreateMode(): Boolean {
        return intent.getBooleanExtra("createMode", false)
    }

    private fun getExistingEntities(): Array<String>? {
        return intent.getStringArrayExtra("existingEntityNames")
    }

    private fun getViewType(): ViewType {
        if (viewType == null) {
            try {
                viewType = ViewType.valueOf(intent.getStringExtra("viewType"))
            } catch (ex: IllegalArgumentException) {
                throw IllegalArgumentException("Variable 'viewtype' is not specified.")
            }
        }

        return viewType as ViewType
    }

    private fun getTitleString(): String {
        return when (getViewType()) {
            ViewType.Departments -> getString(R.string.department)
            ViewType.CompanyOwners -> getString(R.string.company_owner)
            ViewType.Suppliers -> getString(R.string.supplier)
            ViewType.Projects -> getString(R.string.project)
        }
    }

    private fun getNameHintString(): String {
        return when (getViewType()) {
            ViewType.Departments -> getString(R.string.new_department)
            ViewType.CompanyOwners -> getString(R.string.new_company_owner)
            ViewType.Suppliers -> getString(R.string.new_supplier)
            ViewType.Projects -> getString(R.string.new_project)
        }
    }
}
