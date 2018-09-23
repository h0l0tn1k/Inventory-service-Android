package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.CompanyOwner
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceElectricRevision
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ElectricRevisionServiceApi {

    @POST("electric-revisions/")
    fun createElectricRevision(@Body revision: DeviceElectricRevision?) : Call<DeviceElectricRevision>

    object Factory {
        fun create(context : Context): ElectricRevisionServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<ElectricRevisionServiceApi>(ElectricRevisionServiceApi::class.java)
        }
    }
}