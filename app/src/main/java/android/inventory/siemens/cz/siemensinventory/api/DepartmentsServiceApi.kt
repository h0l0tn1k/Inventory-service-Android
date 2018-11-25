package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.Department
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.view.ViewEntity
import retrofit2.Call
import retrofit2.http.*

interface DepartmentsServiceApi {

    @GET("departments/")
    fun getDepartments(): Call<List<Department>>

    @POST("departments/")
    fun createDepartment(@Body department: GenericNameEntity?): Call<Department>

    @PUT("departments/{departmentId}")
    fun updateDepartment(@Path("departmentId") departmentId: Long, @Body department: ViewEntity?): Call<Department>

    @DELETE("departments/{departmentId}")
    fun deleteDepartment(@Path("departmentId") departmentId: Long): Call<Void>
}