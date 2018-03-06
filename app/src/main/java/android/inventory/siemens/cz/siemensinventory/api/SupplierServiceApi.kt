package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.Supplier
import android.inventory.siemens.cz.siemensinventory.api.entity.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface SupplierServiceApi {

    @GET("suppliers/")
    fun getSuppliers() : Call<List<Supplier>>

    @GET("suppliers/{supplier}")
    fun getSupplier(@Path("supplier") supplierId: Long) : Call<Supplier>

    object Factory {
        fun create(): SupplierServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl())
                    .build()
                    .create<SupplierServiceApi>(SupplierServiceApi::class.java)
        }
    }
}