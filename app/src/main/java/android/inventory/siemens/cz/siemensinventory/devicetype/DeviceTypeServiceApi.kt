package android.inventory.siemens.cz.siemensinventory.devicetype

import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceType
import retrofit2.Call
import retrofit2.http.*

interface DeviceTypeServiceApi {

    @GET("device-types/")
    fun getDeviceTypes(): Call<List<DeviceType>>

    @GET("device-types/name/like/{name}")
    fun getDeviceTypesByName(@Path("name") name: String): Call<List<DeviceType>>

    @PUT("device-types/{deviceTypeId}")
    fun updateDeviceType(@Path("deviceTypeId") deviceTypeId: Long?, @Body deviceType: DeviceType?): Call<DeviceType>

    @POST("device-types/")
    fun createDeviceType(@Body device: DeviceType?): Call<DeviceType>
}