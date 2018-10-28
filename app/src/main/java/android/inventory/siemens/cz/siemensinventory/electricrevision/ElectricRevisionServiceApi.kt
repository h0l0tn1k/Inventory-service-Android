package android.inventory.siemens.cz.siemensinventory.electricrevision

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.SiemensServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceElectricRevision
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ElectricRevisionServiceApi {

    @PUT("electric-revisions/{revisionId}")
    fun updateRevision(@Path("revisionId") revisionId: Long?, @Body revision: DeviceElectricRevision?): Call<DeviceElectricRevision>

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