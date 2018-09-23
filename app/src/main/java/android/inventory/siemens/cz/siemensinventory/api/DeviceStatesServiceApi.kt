package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.CompanyOwner
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceState
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface DeviceStatesServiceApi {
    
    @GET("device-states/")
    fun getDeviceStates() : Call<List<DeviceState>>

    object Factory {
        fun create(context : Context): DeviceStatesServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<DeviceStatesServiceApi>(DeviceStatesServiceApi::class.java)
        }
    }
}