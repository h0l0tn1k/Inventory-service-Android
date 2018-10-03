package android.inventory.siemens.cz.siemensinventory.device

import android.app.DatePickerDialog
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
import kotlinx.android.synthetic.main.activity_device_create.*
import kotlinx.android.synthetic.main.device_generic_confirmation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import android.databinding.DataBindingUtil
import android.inventory.siemens.cz.siemensinventory.databinding.ActivityDeviceCreateBinding
import android.widget.EditText
import java.util.*


class DeviceActivity : DevActivity() {

    private var device : Device? = null
    private var deviceApi = DeviceServiceApi.Factory.create(this)
    private var electricRevisionApi = ElectricRevisionServiceApi.Factory.create(this)
    private var calibrationApi = CalibrationServiceApi.Factory.create(this)
    private var deviceIntent : DeviceIntent? = null
    private val resultParameterName : String = "result"
    private var deviceBinding: ActivityDeviceCreateBinding? = null
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_create)
        deviceBinding = DataBindingUtil.setContentView(this, R.layout.activity_device_create) as ActivityDeviceCreateBinding

        device = getDeviceFromIntent()
        deviceBinding?.device = device
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
//        device_passed_btn.visibility = View.GONE
//        device_failed_btn.visibility = View.GONE
//        device_close_btn.visibility = View.GONE
//        when {
//            isBorrowedByCurrentUser() -> {
//                device_passed_btn.visibility = View.VISIBLE
//                displayGenericConfirmationLayout(getString(R.string.borrow_return_device_question))
//                device_passed_btn.setOnClickListener {
//                    val dialog = BorrowDialog(this)
//                    dialog.buildDialog(device, false, DialogInterface.OnClickListener { _, _ ->
//                        device?.comment = dialog.getComment()
//                        device?.deviceState = dialog.getDeviceState()
//                        returnDevice()
//                    })
//                    dialog.show()
//                }
//            }
//            isBorrowedByNoOne() -> {
//                device_passed_btn.visibility = View.VISIBLE
//                displayGenericConfirmationLayout(getString(R.string.borrow_device_question))
//                device_passed_btn.setOnClickListener {
//                    val dialog = BorrowDialog(this)
//                    dialog.buildDialog(device, true, DialogInterface.OnClickListener { _, _ ->
//                        device?.comment = dialog.getComment()
//                        device?.deviceState = dialog.getDeviceState()
//                        borrowDevice()
//                    })
//                    dialog.show()
//                }
//            }
//            else -> {
//                device_close_btn.visibility = View.VISIBLE
//                displayGenericConfirmationLayout(getString(R.string.device_borrowed_by_other_user))
//                device_close_btn.setOnClickListener { finish() }
//            }
//        }
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
        displayGenericConfirmationLayout()

        device_save_btn.setOnClickListener { setInventoryResult(true) }
        device_edit_btn.setOnClickListener { setInventoryResult(false) }

        //layouts
        device_layout_department.visibility = View.GONE
        device_layout_nst.visibility = View.VISIBLE
        device_layout_inventory_number.visibility = View.VISIBLE
        device_layout_inventory_comment.visibility = View.VISIBLE
        device_layout_inventory_result.visibility = View.VISIBLE

        //fields
        device_read_qr_code.visibility = View.VISIBLE
        device_read_device_type.visibility = View.VISIBLE
        device_read_serial_number.visibility = View.VISIBLE
        device_edit_owner.visibility = View.VISIBLE
        device_edit_default_location.visibility = View.VISIBLE
        device_edit_nst.visibility = View.VISIBLE
        //inventory
        device_edit_inventory_number.visibility = View.VISIBLE
        device_edit_inventory_comment.visibility = View.VISIBLE
        device_edit_inventory_result.visibility = View.VISIBLE
        initInventoryResultRadioButtons(device?.inventoryRecord?.inventoryState)
    }

    private fun initInventoryResultRadioButtons(inventoryState: InventoryState?) {
        if(inventoryState?.state != null) {
            when(inventoryState.state.toLowerCase()) {
                "ok" -> inventory_result_radio_ok.isChecked = true
                "false" -> inventory_result_radio_false.isChecked = true
                "unclear" -> inventory_result_radio_unclear.isChecked = true
            }
        } else {
            inventory_result_radio_unclear.isChecked = true
        }
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
        displayGenericConfirmationLayout()

//        device_passed_btn.setOnClickListener { PassedElectricRevisionDialog().showDialog(this) }
//        device_failed_btn.setOnClickListener { FailedElectricRevisionDialog().showDialog(this) }

        device_read_qr_code.visibility = View.VISIBLE
        device_read_device_type.visibility = View.VISIBLE
        device_read_serial_number.visibility = View.VISIBLE
        device_edit_owner.visibility = View.VISIBLE
        device_edit_default_location.visibility = View.VISIBLE
        device_edit_nst.visibility = View.VISIBLE
        device_edit_inventory_number.visibility = View.VISIBLE

        device_layout_department.visibility = View.GONE
        device_layout_nst.visibility = View.VISIBLE
        device_layout_inventory_number.visibility = View.VISIBLE
        //electric revision
        device_layout_last_el_revision_date.visibility = View.VISIBLE
            device_read_last_el_revision_date.visibility = View.VISIBLE
        device_layout_electric_revision_period.visibility = View.VISIBLE
            device_edit_electric_revision_period.visibility = View.VISIBLE
        // new rev date
        device_layout_electric_revision_new_date.visibility = View.VISIBLE
        device_edit_electric_revision_new_date.visibility = View.VISIBLE
        device_edit_electric_revision_new_date.setOnClickListener { view -> newElectricRevisionDateListener(view) }
    }

    private fun newElectricRevisionDateListener(it: View?) {
        val et = it as EditText
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            et.setText(SimpleDateFormat("dd.MM.yyyy",
                    Locale.getDefault()).format(calendar.time))
        }, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun displayGenericConfirmationLayout() {
        layoutInflater.inflate(R.layout.device_generic_confirmation, null)
        device_close_btn.setOnClickListener { finish() }
    }

    override fun setPassedRevisionParams(result: ElectricRevisionResult) {
        this.device?.revision?.revisionInterval = result.period

        electricRevisionApi.createElectricRevision(this.device?.revision).enqueue(object : Callback<DeviceElectricRevision> {
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
