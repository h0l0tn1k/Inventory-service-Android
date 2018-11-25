package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceState
import retrofit2.Call
import retrofit2.http.GET

interface DeviceStatesServiceApi {

    @GET("device-states/")
    fun getDeviceStates(): Call<List<DeviceState>>
}