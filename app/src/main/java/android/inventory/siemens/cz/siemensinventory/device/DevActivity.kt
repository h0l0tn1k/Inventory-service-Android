package android.inventory.siemens.cz.siemensinventory.device

import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.calibration.CalibrationResult
import android.inventory.siemens.cz.siemensinventory.electricrevision.ElectricRevisionResult
import android.support.v7.app.AppCompatActivity

abstract class DevActivity : AppCompatActivity() {
    abstract fun setPassedRevisionParams(result: ElectricRevisionResult)
    abstract fun setCalibrationParams(result : CalibrationResult)
    abstract fun getDeviceFromIntent(): Device?
}