package android.inventory.siemens.cz.siemensinventory.calibration

import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceCalibration
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface CalibrationServiceApi {

    @PUT("calibrations/{calibrationId}")
    fun updateCalibration(@Path("calibrationId") calibrationId: Long?, @Body calibration: DeviceCalibration?): Call<DeviceCalibration>
}

