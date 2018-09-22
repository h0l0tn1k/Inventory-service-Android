package android.inventory.siemens.cz.siemensinventory.device

import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.InventoryState
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
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

class DeviceActivity : DevActivity() {

    private var device : Device? = null
    private var deviceApi : DeviceServiceApi? = null
    private var deviceIntent : DeviceIntent? = null
    private val resultParameterName : String = "result"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        deviceApi = DeviceServiceApi.Factory.create(this)
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
        if(isBorrowedByCurrentUser()) {
            displayGenericConfirmationLayout(getString(R.string.borrow_return_device_question))
            device_passed_btn.setOnClickListener { returnDevice() }
        } else {
            displayGenericConfirmationLayout(getString(R.string.borrow_device_question))
            device_passed_btn.setOnClickListener { borrowDevice() }
        }
        device_failed_btn.visibility = View.GONE
    }

    private fun setHolder(user : LoginUserScd?) {
        device?.holder = user
        deviceApi?.updateDevice(device?.id, device)?.enqueue(object : Callback<Device> {
            override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                if(response?.isSuccessful == true) {
                    if(isBorrowedByCurrentUser()) {
                        Toast.makeText(this@DeviceActivity, "Device returned", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@DeviceActivity, "Device borrowed", Toast.LENGTH_LONG).show()
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
        val result = getDevice()?.inventoryRecord
        result?.inventoryState = if (passed)  InventoryState.OK else InventoryState.False

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

    private fun isBorrowedByCurrentUser() : Boolean {
        return getDevice()?.holder?.id == AppData.loginUserScd?.id
    }

    override fun getDevice(): Device? {
        device = Gson().fromJson(intent.getStringExtra("device"), Device::class.java)
        return device
    }
}
