package android.inventory.siemens.cz.siemensinventory.calibration

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.SiemensServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceCalibration
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface CalibrationServiceApi {

    @PUT("calibrations/{calibrationId}")
    fun updateCalibration(@Path("calibrationId") calibrationId: Long?, @Body calibration: DeviceCalibration?): Call<DeviceCalibration>

    object Factory {
        fun create(context: Context): CalibrationServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<CalibrationServiceApi>(CalibrationServiceApi::class.java)
        }
    }
}

