package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.CompanyOwner
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CompanyOwnerServiceApi {
    
    @GET("companyowners/")
    fun getCompanyOwners() : Call<List<CompanyOwner>>

    @GET("companyowners/{companyowner}")
    fun getCompanyOwner(@Path("companyowner") companyOwnerId: Long) : Call<CompanyOwner>

    object Factory {
        fun create(): CompanyOwnerServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl())
                    .build()
                    .create<CompanyOwnerServiceApi>(CompanyOwnerServiceApi::class.java)
        }
    }
}