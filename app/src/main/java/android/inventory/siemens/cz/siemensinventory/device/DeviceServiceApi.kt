package android.inventory.siemens.cz.siemensinventory.device

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.SiemensServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DeviceServiceApi {
    
    @GET("devices/")
    fun getDevices() : Call<List<Device>>

    @GET("devices/{deviceId}")
    fun getDevice(@Path("deviceId") deviceId: Long) : Call<Device>

    @GET("devices/barcodeNumber/{barcodeId}")
    fun getDeviceByBarcodeId(@Path("barcodeId") barcodeId: String) : Call<Device>

    @GET("devices/serialNumber/{serialNo}")
    fun getDeviceBySerialNo(@Path("serialNo") serialNo: String) : Call<Device>

    @GET("devices/serialno/like/{serialNo}")
    fun getDevicesWithSerialNoLike(@Path("serialNo") serialNo: String) : Call<List<Device>>

    @GET("devices/borrowed-by/{scdId}")
    fun getBorrowedDevicesByUserId(@Path("scdId") scdId: Long?) : Call<List<Device>>

    @PUT("devices/{deviceId}")
    fun updateDevice(@Path("deviceId") deviceId: Long?, @Body device: Device?) : Call<Device>

    object Factory {
        fun create(context : Context): DeviceServiceApi {
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()

            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<DeviceServiceApi>(DeviceServiceApi::class.java)
        }
    }
}