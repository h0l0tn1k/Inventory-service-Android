package android.inventory.siemens.cz.siemensinventory.device

import android.content.DialogInterface
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.CalibrationServiceApi
import android.inventory.siemens.cz.siemensinventory.api.ElectricRevisionServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.*
import android.inventory.siemens.cz.siemensinventory.borrow.BorrowDialog
import android.inventory.siemens.cz.siemensinventory.calibration.CalibrationResult
import android.inventory.siemens.cz.siemensinventory.calibration.CalibrationRevisionResultDialog
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.electricrevision.ElectricRevisionResult
import android.os.Bundle
import com.google.gson.Gson
import android.inventory.siemens.cz.siemensinventory.electricrevision.FailedElectricRevisionDialog
import android.inventory.siemens.cz.siemensinventory.electricrevision.PassedElectricRevisionDialog
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_device.*
import kotlinx.android.synthetic.main.device_generic_confirmation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class DeviceActivity : DevActivity() {

    private var device : Device? = null
    private var deviceApi = DeviceServiceApi.Factory.create(this)
    private var electricRevisionApi = ElectricRevisionServiceApi.Factory.create(this)
    private var calibrationApi = CalibrationServiceApi.Factory.create(this)
    private var deviceIntent : DeviceIntent? = null
    private val resultParameterName : String = "result"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        deviceParameters.adapter = DeviceParametersAdapter(this, getDeviceFromIntent() as Device)
        device = getDeviceFromIntent()
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
        device_passed_btn.visibility = View.GONE
        device_failed_btn.visibility = View.GONE
        device_close_btn.visibility = View.GONE
        when {
            isBorrowedByCurrentUser() -> {
                device_passed_btn.visibility = View.VISIBLE
                displayGenericConfirmationLayout(getString(R.string.borrow_return_device_question))
                device_passed_btn.setOnClickListener {
                    val dialog = BorrowDialog(this)
                    dialog.buildDialog(device, false, DialogInterface.OnClickListener { _, _ ->
                        device?.comment = dialog.getComment()
                        device?.deviceState = dialog.getDeviceState()
                        returnDevice()
                    })
                    dialog.show()
                }
            }
            isBorrowedByNoOne() -> {
                device_passed_btn.visibility = View.VISIBLE
                displayGenericConfirmationLayout(getString(R.string.borrow_device_question))
                device_passed_btn.setOnClickListener {
                    val dialog = BorrowDialog(this)
                    dialog.buildDialog(device, true, DialogInterface.OnClickListener { _, _ ->
                        device?.comment = dialog.getComment()
                        device?.deviceState = dialog.getDeviceState()
                        borrowDevice()
                    })
                    dialog.show()
                }
            }
            else -> {
                device_close_btn.visibility = View.VISIBLE
                displayGenericConfirmationLayout(getString(R.string.device_borrowed_by_other_user))
                device_close_btn.setOnClickListener { finish() }
            }
        }
    }

    private fun setHolder(user : LoginUserScd?) {
        device?.holder = user
        deviceApi.updateDevice(device?.id, device).enqueue(object : Callback<Device> {
            override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                if(response?.isSuccessful == true) {
                    if(isBorrowedByCurrentUser()) {
                        Toast.makeText(this@DeviceActivity, "Device borrowed", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@DeviceActivity, "Device returned", Toast.LENGTH_LONG).show()
                    }
                    val dev = response.body() as Device
                    //todo refresh view
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this@DeviceActivity, "Error", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Device>?, t: Throwable?) {
                Toast.makeText(this@DeviceActivity, "Error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun returnDevice() {
        setHolder(null)
    }

    private fun borrowDevice() {
        setHolder(AppData.loginUserScd)
    }

    private fun setInventoryView() {
        displayGenericConfirmationLayout(getString(R.string.inventory_device_present))

        device_passed_btn.setOnClickListener { setInventoryResult(true) }
        device_failed_btn.setOnClickListener { setInventoryResult(false) }
    }

    private fun setInventoryResult(passed: Boolean) {
        val result = getDeviceFromIntent()?.inventoryRecord
        result?.inventoryState = if (passed) InventoryState.OK else InventoryState.False

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
        this.device?.revision?.revisionInterval = result.period

        electricRevisionApi?.createElectricRevision(this.device?.revision)?.enqueue(object : Callback<DeviceElectricRevision> {
            override fun onResponse(call: Call<DeviceElectricRevision>?, response: Response<DeviceElectricRevision>?) {
                if(response?.isSuccessful == true) {
                    Toast.makeText(this@DeviceActivity, "Electric Revision updated", Toast.LENGTH_LONG).show()
                    device?.revision = response.body() as DeviceElectricRevision
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this@DeviceActivity, "Error", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<DeviceElectricRevision>?, t: Throwable?) {
                Toast.makeText(this@DeviceActivity, "Error", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun setCalibrationParams(result: CalibrationResult) {
        device?.calibration?.lastCalibrationDateString = SimpleDateFormat("yyyy-MM-dd").format(result.date)

        calibrationApi.createCalibrations(device?.calibration).enqueue(object : Callback<DeviceCalibration> {
            override fun onResponse(call: Call<DeviceCalibration>?, response: Response<DeviceCalibration>?) {
                if(response?.isSuccessful == true) {
                    Toast.makeText(this@DeviceActivity, "Calibration updated", Toast.LENGTH_LONG).show()
                    device?.calibration = response.body() as DeviceCalibration
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this@DeviceActivity, "Error", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<DeviceCalibration>?, t: Throwable?) {
                Toast.makeText(this@DeviceActivity, "Error", Toast.LENGTH_LONG).show()
            }
        })

        intent.putExtra(resultParameterName, Gson().toJson(result))
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun isBorrowedByCurrentUser() : Boolean {
        return device?.holder?.id == AppData.loginUserScd?.id
    }

    private fun isBorrowedByNoOne() : Boolean {
        return device?.holder?.id == null
    }

    override fun getDeviceFromIntent(): Device? {
        return Gson().fromJson(intent.getStringExtra("device"), Device::class.java)
    }
}
