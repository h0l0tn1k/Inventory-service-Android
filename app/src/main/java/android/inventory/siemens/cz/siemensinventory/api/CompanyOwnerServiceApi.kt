package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.CompanyOwner
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.view.ViewEntity
import retrofit2.Call
import retrofit2.http.*

interface CompanyOwnerServiceApi {

    @GET("company-owners/")
    fun getCompanyOwners(): Call<List<CompanyOwner>>

    @POST("company-owners/")
    fun createCompanyOwner(@Body companyOwner: GenericNameEntity?): Call<CompanyOwner>

    @PUT("company-owners/{companyownerId}")
    fun updateCompanyOwner(@Path("companyownerId") companyOwnerId: Long, @Body companyOwner: ViewEntity?): Call<CompanyOwner>

    @DELETE("company-owners/{companyownerId}")
    fun deleteCompanyOwner(@Path("companyownerId") companyOwnerId: Long): Call<Void>
}