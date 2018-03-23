package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface DeviceServiceApi {
    
    @GET("devices/")
    fun getDevices() : Call<List<Device>>

    @GET("devices/{deviceId}")
    fun getDevice(@Path("deviceId") deviceId: Long) : Call<Device>

    @GET("devices/barcode/{barcodeId}")
    fun getDeviceByBarcodeId(@Path("barcodeId") barcodeId: String) : Call<Device>

    @GET("devices/serialno/{serialNo}")
    fun getDeviceBySerialNo(@Path("serialNo") serialNo: String) : Call<Device>

    object Factory {
        fun create(context : Context): DeviceServiceApi {
            //val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create()
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()

            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<DeviceServiceApi>(DeviceServiceApi::class.java)
        }
    }
}