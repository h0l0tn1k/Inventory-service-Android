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

    @GET("devices/barcodeNumber/{barcodeId}")
    fun getDeviceByBarcodeId(@Path("barcodeId") barcodeId: String) : Call<Device>

    @GET("devices/serialOrBarcodeNumber/like/{serialOrBarcodeNo}")
    fun getDevicesWithSerialOrBarcodeNumberLike(@Path("serialOrBarcodeNo") serialBarcodeNo: String) : Call<List<Device>>

    @GET("devices/borrowed-by/{scdId}")
    fun getBorrowedDevicesByUserId(@Path("scdId") scdId: Long?) : Call<List<Device>>

    @PUT("devices/{deviceId}")
    fun updateDevice(@Path("deviceId") deviceId: Long?, @Body device: Device?) : Call<Device>

    @POST("devices/")
    fun createDevice(@Body device: Device?) : Call<Device>
}