package android.inventory.siemens.cz.siemensinventory.device

import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.calibration.CalibrationResult
import android.inventory.siemens.cz.siemensinventory.calibration.CalibrationRevisionResultDialog
import android.inventory.siemens.cz.siemensinventory.electricrevision.ElectricRevisionResult
import android.os.Bundle
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_device.*
import android.inventory.siemens.cz.siemensinventory.electricrevision.FailedElectricRevisionDialog
import android.inventory.siemens.cz.siemensinventory.electricrevision.PassedElectricRevisionDialog
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.device_el_revision_confirmation.*

class DeviceActivity : DevActivity() {

    private var device : Device? = null
    private var deviceIntent : DeviceIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        deviceParameters.adapter = DeviceParametersAdapter(this, getDevice() as Device)
        handleIntent()
    }

    private fun handleIntent() {
        deviceIntent = DeviceIntent.valueOf(intent.getStringExtra("intent"))

        when(deviceIntent) {
            DeviceIntent.EL_REVISION -> { setElRevisionView() }
            DeviceIntent.CALIBRATION -> { setCalibrationView() }
            else -> { }
        }
    }

    private fun setCalibrationView() {
        CalibrationRevisionResultDialog().showDialog(this)
    }

    private fun setElRevisionView() {
        device_el_revision_result_section.visibility = View.VISIBLE
        layoutInflater.inflate(R.layout.device_el_revision_confirmation, null)

        device_passed_btn.setOnClickListener { PassedElectricRevisionDialog().showDialog(this) }
        device_failed_btn.setOnClickListener { FailedElectricRevisionDialog().showDialog(this) }
    }

    override fun setPassedRevisionParams(result: ElectricRevisionResult) {
        //TODO: update revision data local & db
        this.device?.lastRevisionDateString = result.revisionDate.toString()
    }

    override fun setCalibrationParams(result: CalibrationResult) {
        //TODO: add implementation
        Toast.makeText(this, "Date is: " + result.date, Toast.LENGTH_LONG).show()
    }

    override fun getDevice(): Device? {
        device = Gson().fromJson(intent.getStringExtra("device"), Device::class.java)
        return device
    }
}
