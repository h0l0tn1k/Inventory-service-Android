package android.inventory.siemens.cz.siemensinventory.view

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.CompanyOwnerServiceApi
import android.inventory.siemens.cz.siemensinventory.api.DepartmentsServiceApi
import android.inventory.siemens.cz.siemensinventory.api.ProjectServiceApi
import android.inventory.siemens.cz.siemensinventory.api.SupplierServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.*
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewEntityDataProvider(
        private val context : Context,
        private val viewType : ViewType,
        private var adapter : ViewEntityAdapter
) {

    fun getData() {
        when(viewType) {
            ViewType.Projects -> getProjectsData()
            ViewType.Suppliers -> getSuppliersData()
            ViewType.CompanyOwners -> getCompanyOwnersData()
            ViewType.Departments -> getDepartmentsData()
        }
    }
    fun createData(genericEntity: GenericNameEntity) {
        when(viewType) {
            ViewType.Projects -> createProjectData(genericEntity)
            ViewType.Suppliers -> createSupplierData(genericEntity)
            ViewType.CompanyOwners -> createCompanyOwnerData(genericEntity)
            ViewType.Departments -> createDepartmentData(genericEntity)
        }
    }

    private fun getDepartmentsData() {
        DepartmentsServiceApi.Factory.create(context).getDepartments().enqueue(getCallback())
    }

    private fun getCompanyOwnersData() {
        CompanyOwnerServiceApi.Factory.create(context).getCompanyOwners().enqueue(getCallback())
    }

    private fun getProjectsData() {
        ProjectServiceApi.Factory.create(context).getProjects().enqueue(getCallback())
    }

    private fun getSuppliersData() {
        SupplierServiceApi.Factory.create(context).getSuppliers().enqueue(getCallback())
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

    private fun <T : ViewEntity> getCallback() : Callback<List<T>> {
        return object : Callback<List<T>> {
            override fun onResponse(call: Call<List<T>>?, response: Response<List<T>>?) {
                this@ViewEntityDataProvider.onSuccess(response as Response<List<T>>)
            }
            override fun onFailure(call: Call<List<T>>?, t: Throwable?) {
                this@ViewEntityDataProvider.onFailure(t)
            }
        }
    }

    private fun <T : ViewEntity> getCreateCallback() : Callback<T> {
        return object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                if(response?.isSuccessful == true) {
                    val items = mutableListOf<ViewEntity>()
                    items.addAll(adapter.getList())
                    items.add(response.body() as ViewEntity)
                    adapter.updateList(items)
                }
            }
            override fun onFailure(call: Call<T>?, t: Throwable?) {
                this@ViewEntityDataProvider.onFailure(t)
            }
        }
    }

    private fun onFailure(t: Throwable?) {
        Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
    }

    private fun <T : ViewEntity> onSuccess(response : Response<List<T>>) {
        if(response.isSuccessful) {
            adapter.updateList(response.body() as List<ViewEntity>)
        }
    }
}