package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.Department
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.api.entity.Supplier
import android.inventory.siemens.cz.siemensinventory.view.ViewEntity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DepartmentsServiceApi {
    
    @GET("departments/")
    fun getDepartments() : Call<List<Department>>

    @GET("departments/{departmentId}")
    fun getDepartment(@Path("departmentId") departmentId: Long) : Call<Department>

    @POST("departments/")
    fun createDepartment(@Body department: GenericNameEntity?): Call<Department>

    @PUT("departments/{departmentId}")
    fun updateDepartment(@Path("departmentId") departmentId: Long, @Body department: ViewEntity?): Call<Department>

    @DELETE("departments/{departmentId}")
    fun deleteDepartment(@Path("departmentId") departmentId: Long) : Call<Void>

    object Factory {
        fun create(context : Context): DepartmentsServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<DepartmentsServiceApi>(DepartmentsServiceApi::class.java)
        }
    }
}