package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import android.inventory.siemens.cz.siemensinventory.api.entity.Supplier
import android.inventory.siemens.cz.siemensinventory.view.ViewEntity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SupplierServiceApi {

    @GET("suppliers/")
    fun getSuppliers(): Call<List<Supplier>>

    @GET("suppliers/{supplier}")
    fun getSupplier(@Path("supplier") supplierId: Long): Call<Supplier>

    @POST("suppliers/")
    fun createSupplier(@Body supplier: GenericNameEntity?): Call<Supplier>

    @PUT("suppliers/{supplierId}")
    fun updateSupplier(@Path("supplierId") supplierId: Long, @Body supplier: ViewEntity?): Call<Supplier>

    @DELETE("suppliers/{supplierId}")
    fun deleteSupplier(@Path("supplierId") supplierId: Long) : Call<Void>

    object Factory {
        fun create(context: Context): SupplierServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<SupplierServiceApi>(SupplierServiceApi::class.java)
        }
    }
}