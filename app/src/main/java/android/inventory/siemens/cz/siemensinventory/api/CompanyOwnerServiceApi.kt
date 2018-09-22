package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.CompanyOwner
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CompanyOwnerServiceApi {
    
    @GET("company-owners/")
    fun getCompanyOwners() : Call<List<CompanyOwner>>

    @GET("company-owners/{companyowner}")
    fun getCompanyOwner(@Path("companyowner") companyOwnerId: Long) : Call<CompanyOwner>

    object Factory {
        fun create(context : Context): CompanyOwnerServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<CompanyOwnerServiceApi>(CompanyOwnerServiceApi::class.java)
        }
    }
}