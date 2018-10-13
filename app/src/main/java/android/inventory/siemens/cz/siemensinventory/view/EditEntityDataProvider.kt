package android.inventory.siemens.cz.siemensinventory.view

import android.app.Activity
import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.CompanyOwnerServiceApi
import android.inventory.siemens.cz.siemensinventory.api.DepartmentsServiceApi
import android.inventory.siemens.cz.siemensinventory.api.ProjectServiceApi
import android.inventory.siemens.cz.siemensinventory.api.SupplierServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditEntityDataProvider(
        private val context: Context,
        private val viewType: ViewType
) {

    fun createData(genericEntity: GenericNameEntity) {
        when (viewType) {
            ViewType.Projects -> createProjectData(genericEntity)
            ViewType.Suppliers -> createSupplierData(genericEntity)
            ViewType.CompanyOwners -> createCompanyOwnerData(genericEntity)
            ViewType.Departments -> createDepartmentData(genericEntity)
        }
    }

    fun editData(entity: ViewEntity) {
        when (viewType) {
            ViewType.Projects -> editProjectData(entity)
            ViewType.Suppliers -> editSupplierData(entity)
            ViewType.CompanyOwners -> editCompanyOwnerData(entity)
            ViewType.Departments -> editDepartmentData(entity)
        }
    }

    fun deleteData(id: Long) {
        when (viewType) {
            ViewType.Projects -> deleteProjectData(id)
            ViewType.Suppliers -> deleteSupplierData(id)
            ViewType.CompanyOwners -> deleteCompanyOwnerData(id)
            ViewType.Departments -> deleteDepartmentData(id)
        }
    }

    private fun deleteDepartmentData(id: Long) {
        DepartmentsServiceApi.Factory.create(context).deleteDepartment(id).enqueue(getDeleteCallback())
    }

    private fun deleteCompanyOwnerData(id: Long) {
        CompanyOwnerServiceApi.Factory.create(context).deleteCompanyOwner(id).enqueue(getDeleteCallback())
    }

    private fun deleteSupplierData(id: Long) {
        SupplierServiceApi.Factory.create(context).deleteSupplier(id).enqueue(getDeleteCallback())
    }

    private fun deleteProjectData(id: Long) {
        ProjectServiceApi.Factory.create(context).deleteProject(id).enqueue(getDeleteCallback())
    }

    private fun createDepartmentData(department: GenericNameEntity) {
        DepartmentsServiceApi.Factory.create(context).createDepartment(department).enqueue(getCreateCallback())
    }

    private fun createCompanyOwnerData(companyOwner: GenericNameEntity) {
        CompanyOwnerServiceApi.Factory.create(context).createCompanyOwner(companyOwner).enqueue(getCreateCallback())
    }

    private fun createProjectData(project: GenericNameEntity) {
        ProjectServiceApi.Factory.create(context).createProject(project).enqueue(getCreateCallback())
    }

    private fun createSupplierData(supplier: GenericNameEntity) {
        SupplierServiceApi.Factory.create(context).createSupplier(supplier).enqueue(getCreateCallback())
    }

    private fun editDepartmentData(department: ViewEntity) {
        DepartmentsServiceApi.Factory.create(context).updateDepartment(department.id, department).enqueue(getCreateCallback())
    }

    private fun editCompanyOwnerData(companyOwner: ViewEntity) {
        CompanyOwnerServiceApi.Factory.create(context).updateCompanyOwner(companyOwner.id, companyOwner).enqueue(getCreateCallback())
    }

    private fun editSupplierData(supplier: ViewEntity) {
        SupplierServiceApi.Factory.create(context).updateSupplier(supplier.id, supplier).enqueue(getCreateCallback())
    }

    private fun editProjectData(project: ViewEntity) {
        ProjectServiceApi.Factory.create(context).updateProject(project.id, project).enqueue(getCreateCallback())
    }

    private fun getDeleteCallback(): Callback<Void>? {
        return object : Callback<Void> {
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                if(response?.isSuccessful == true) {
                    Toast.makeText(context, context.getString(R.string.entity_was_deleted), Toast.LENGTH_SHORT).show()
                    ((context) as Activity).finish()
                } else {
                    Toast.makeText(context, context.getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                this@EditEntityDataProvider.onFailure(t)
            }
        }
    }

    private fun <T : ViewEntity> getCreateCallback(): Callback<T> {
        return object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                if (response?.isSuccessful == true) {
                    Toast.makeText(context, context.getString(R.string.able_to_save_changes), Toast.LENGTH_SHORT).show()
                    ((context) as Activity).finish()
                } else {
                    Toast.makeText(context, context.getString(R.string.unable_to_save_changes), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                this@EditEntityDataProvider.onFailure(t)
            }
        }
    }

    private fun onFailure(t: Throwable?) {
        Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
    }
}