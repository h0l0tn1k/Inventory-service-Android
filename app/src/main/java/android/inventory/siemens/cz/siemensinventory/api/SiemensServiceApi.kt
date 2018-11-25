package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.entity.ServiceSettings
import retrofit2.Call
import retrofit2.http.GET

interface SiemensServiceApi {

    @GET("/actuator/health/")
    fun getServiceStatus() : Call<Void>

    companion object {

        private var settings : ServiceSettings? = null

        fun getBaseUrl(context: Context): String  {
            if(settings == null) {
                settings = ServiceSettings(context)
            }

            return settings?.getServiceBaseAddress().toString()
        }

        fun getServiceUrl(context: Context): String {
            if(settings == null) {
                settings = ServiceSettings(context)
            }

            return settings?.getServiceUrlFormatted().toString()
        }

        fun getClientBaseAuthorization(context: Context): String {
            if(settings == null) {
                settings = ServiceSettings(context)
            }

            return settings?.getClientBaseAuthorization().toString()
        }
    }
}