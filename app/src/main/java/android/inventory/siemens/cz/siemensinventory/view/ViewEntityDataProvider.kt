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

    private fun onFailure(t: Throwable?) {
        Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
    }

    private fun <T : ViewEntity> onSuccess(response : Response<List<T>>) {
        if(response.isSuccessful) {
            adapter.updateList(response.body() as List<ViewEntity>)
        }
    }
}