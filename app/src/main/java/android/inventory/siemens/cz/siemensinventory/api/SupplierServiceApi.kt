package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.api.entity.Supplier
import android.inventory.siemens.cz.siemensinventory.view.ViewEntity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SupplierServiceApi {

    @GET("suppliers/")
    fun getSuppliers(): Call<List<Supplier>>

    @POST("suppliers/")
    fun createSupplier(@Body supplier: GenericNameEntity?): Call<Supplier>

    @PUT("suppliers/{supplierId}")
    fun updateSupplier(@Path("supplierId") supplierId: Long, @Body supplier: ViewEntity?): Call<Supplier>

    @DELETE("suppliers/{supplierId}")
    fun deleteSupplier(@Path("supplierId") supplierId: Long) : Call<Void>
}