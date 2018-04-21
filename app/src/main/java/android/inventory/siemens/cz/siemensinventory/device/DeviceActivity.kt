package android.inventory.siemens.cz.siemensinventory.device

import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.InventoryRecord
import android.inventory.siemens.cz.siemensinventory.calibration.CalibrationResult
import android.inventory.siemens.cz.siemensinventory.calibration.CalibrationRevisionResultDialog
import android.inventory.siemens.cz.siemensinventory.electricrevision.ElectricRevisionResult
import android.os.Bundle
import com.google.gson.Gson
import android.inventory.siemens.cz.siemensinventory.electricrevision.FailedElectricRevisionDialog
import android.inventory.siemens.cz.siemensinventory.electricrevision.PassedElectricRevisionDialog
import android.inventory.siemens.cz.siemensinventory.inventory.InventoryResult
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_device.*
import kotlinx.android.synthetic.main.device_generic_confirmation.*

class DeviceActivity : DevActivity() {

    private var device : Device? = null
    private var deviceIntent : DeviceIntent? = null
    private val resultParameterName : String = "result"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        deviceParameters.adapter = DeviceParametersAdapter(this, getDevice() as Device)
        handleIntent()
    }

    private fun handleIntent() {
        deviceIntent = DeviceIntent.valueOf(intent.getStringExtra("intent"))

        when(deviceIntent) {
            DeviceIntent.BORROW -> { setBorrowView() }
            DeviceIntent.CALIBRATION -> { setCalibrationView() }
            DeviceIntent.EL_REVISION -> { setElRevisionView() }
            DeviceIntent.INVENTORY -> { setInventoryView() }
            else -> { }
        }
    }

    private fun setBorrowView() {
        displayGenericConfirmationLayout(getString(R.string.borrow_device_question))
        device_failed_btn.visibility = View.GONE
        device_passed_btn.setOnClickListener { borrowDevice() }
    }

    private fun borrowDevice() {

    }

    private fun setInventoryView() {
        displayGenericConfirmationLayout(getString(R.string.inventory_device_present))

        device_passed_btn.setOnClickListener { setInventoryResult(true) }
        device_failed_btn.setOnClickListener { setInventoryResult(false) }
    }

    private fun setInventoryResult(passed: Boolean) {
        val result = getDevice()?.inventoryRecord
        result?.registered = passed

        intent.putExtra(resultParameterName, Gson().toJson(result))
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun setCalibrationView() {
        CalibrationRevisionResultDialog().showDialog(this)
    }

    private fun setElRevisionView() {
        displayGenericConfirmationLayout(getString(R.string.passed_electric_revision))

        device_passed_btn.setOnClickListener { PassedElectricRevisionDialog().showDialog(this) }
        device_failed_btn.setOnClickListener { FailedElectricRevisionDialog().showDialog(this) }
    }

    private fun displayGenericConfirmationLayout(title : String) {
        device_generic_result_section.visibility = View.VISIBLE
        layoutInflater.inflate(R.layout.device_generic_confirmation, null)
        device_generic_question_box.text = title
    }

    override fun setPassedRevisionParams(result: ElectricRevisionResult) {
        //TODO: update revision data local & db
        this.device?.lastRevisionDateString = result.revisionDate.toString()
    }

    override fun setCalibrationParams(result: CalibrationResult) {
        //TODO: add implementation
        Toast.makeText(this, "Date is: " + result.date, Toast.LENGTH_LONG).show()

        intent.putExtra(resultParameterName, Gson().toJson(result))
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun getDevice(): Device? {
        device = Gson().fromJson(intent.getStringExtra("device"), Device::class.java)
        return device
    }
}
