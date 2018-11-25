package android.inventory.siemens.cz.siemensinventory.view

import android.app.Activity
import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.*
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.data.AppData
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
        createService(DepartmentsServiceApi::class.java).deleteDepartment(id).enqueue(getDeleteCallback())
    }

    private fun deleteCompanyOwnerData(id: Long) {
        createService(CompanyOwnerServiceApi::class.java).deleteCompanyOwner(id).enqueue(getDeleteCallback())
    }

    private fun deleteSupplierData(id: Long) {
        createService(SupplierServiceApi::class.java).deleteSupplier(id).enqueue(getDeleteCallback())
    }

    private fun deleteProjectData(id: Long) {
        createService(ProjectServiceApi::class.java).deleteProject(id).enqueue(getDeleteCallback())
    }

    private fun createDepartmentData(department: GenericNameEntity) {
        createService(DepartmentsServiceApi::class.java).createDepartment(department).enqueue(getCreateCallback())
    }

    private fun createCompanyOwnerData(companyOwner: GenericNameEntity) {
        createService(CompanyOwnerServiceApi::class.java).createCompanyOwner(companyOwner).enqueue(getCreateCallback())
    }

    private fun createProjectData(project: GenericNameEntity) {
        createService(ProjectServiceApi::class.java).createProject(project).enqueue(getCreateCallback())
    }

    private fun createSupplierData(supplier: GenericNameEntity) {
        createService(SupplierServiceApi::class.java).createSupplier(supplier).enqueue(getCreateCallback())
    }

    private fun editDepartmentData(department: ViewEntity) {
        createService(DepartmentsServiceApi::class.java).updateDepartment(department.id, department).enqueue(getCreateCallback())
    }

    private fun editCompanyOwnerData(companyOwner: ViewEntity) {
        createService(CompanyOwnerServiceApi::class.java).updateCompanyOwner(companyOwner.id, companyOwner).enqueue(getCreateCallback())
    }

    private fun editSupplierData(supplier: ViewEntity) {
        createService(SupplierServiceApi::class.java).updateSupplier(supplier.id, supplier).enqueue(getCreateCallback())
    }

    private fun editProjectData(project: ViewEntity) {
        createService(ProjectServiceApi::class.java).updateProject(project.id, project).enqueue(getCreateCallback())
    }

    private fun getDeleteCallback(): Callback<Void> {
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

    private fun <T> createService(classType: Class<T>): T {
        return ServiceApiGenerator.Factory.createService(classType, AppData.accessToken, context)
    }
}