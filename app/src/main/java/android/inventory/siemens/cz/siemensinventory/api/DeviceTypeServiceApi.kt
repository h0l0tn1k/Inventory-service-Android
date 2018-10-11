package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.SiemensServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceType
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DeviceTypeServiceApi {
    
    @GET("device-types/")
    fun getDeviceTypes() : Call<List<DeviceType>>

    @GET("device-types/{deviceTypeId}")
    fun getDeviceType(@Path("deviceTypeId") deviceTypeId: Long) : Call<DeviceType>

    @PUT("device-types/{deviceTypeId}")
    fun updateDeviceType(@Path("deviceTypeId") deviceTypeId: Long?, @Body deviceType: DeviceType?) : Call<DeviceType>

    @POST("device-types/")
    fun createDeviceType(@Body device: DeviceType?) : Call<DeviceType>

    object Factory {
        fun create(context : Context): DeviceTypeServiceApi {
            val gson = GsonBuilder().create()

            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<DeviceTypeServiceApi>(DeviceTypeServiceApi::class.java)
        }
    }
}