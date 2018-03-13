package android.inventory.siemens.cz.siemensinventory.api

import retrofit2.Call
import retrofit2.http.GET

interface SiemensServiceApi {

    @GET("users/")
    fun getService() : Call<Void>

    companion object {
        //configurable from App Settings
        fun getBaseUrl(): String = "http://192.168.0.30:8080/rest/"
    }
}