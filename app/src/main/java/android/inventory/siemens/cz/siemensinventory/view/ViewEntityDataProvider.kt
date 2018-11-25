package android.inventory.siemens.cz.siemensinventory.view

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.*
import android.inventory.siemens.cz.siemensinventory.api.entity.*
import android.inventory.siemens.cz.siemensinventory.data.AppData
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
        createService(DepartmentsServiceApi::class.java).getDepartments().enqueue(getCallback())
    }

    private fun getCompanyOwnersData() {
        createService(CompanyOwnerServiceApi::class.java).getCompanyOwners().enqueue(getCallback())
    }

    private fun getProjectsData() {
        createService(ProjectServiceApi::class.java).getProjects().enqueue(getCallback())
    }

    private fun getSuppliersData() {
        createService(SupplierServiceApi::class.java).getSuppliers().enqueue(getCallback())
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

    private fun <T> createService(classType: Class<T>): T {
        return ServiceApiGenerator.Factory.createService(classType, AppData.accessToken, context)
    }
}