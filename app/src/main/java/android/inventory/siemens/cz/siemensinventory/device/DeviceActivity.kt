package android.inventory.siemens.cz.siemensinventory.device

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.CalibrationServiceApi
import android.inventory.siemens.cz.siemensinventory.api.ElectricRevisionServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.*
import android.inventory.siemens.cz.siemensinventory.borrow.BorrowDialog
import android.inventory.siemens.cz.siemensinventory.calibration.CalibrationResult
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.electricrevision.ElectricRevisionResult
import android.os.Bundle
import com.google.gson.Gson
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
import android.inventory.siemens.cz.siemensinventory.tools.DateParser
import android.widget.EditText
import android.widget.RadioButton
import kotlinx.android.synthetic.main.device_borrow_section.*
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
        device_close_btn.setOnClickListener { finish() }
    }

    private fun setBorrowView() {
        // display borrow confirmation layout
        device_create_generic_result_section.visibility = View.GONE
        device_borrow_section_toolbar.visibility = View.VISIBLE
        device_borrow_close_btn.visibility = View.VISIBLE
        device_borrow_close_btn.setOnClickListener { finish() }
        //fields
        device_read_qr_code.visibility = View.VISIBLE
        device_read_device_type.visibility = View.VISIBLE
        device_read_serial_number.visibility = View.VISIBLE
        device_read_default_location.visibility = View.VISIBLE
        device_read_department.visibility = View.VISIBLE
        device_read_comment.visibility = View.VISIBLE
        device_read_status.visibility = View.VISIBLE
        device_read_holder.visibility = View.VISIBLE

        device_layout_comment.visibility = View.VISIBLE
        device_layout_status.visibility = View.VISIBLE

        device_return_btn.visibility = View.GONE
        device_borrow_btn.visibility = View.GONE
        when {
            isBorrowedByCurrentUser() -> {
                device_return_btn.visibility = View.VISIBLE
                device_return_btn.setOnClickListener {
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
                device_borrow_btn.visibility = View.VISIBLE
                device_borrow_btn.setOnClickListener {
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
                //todo display borrowed by other user
                Toast.makeText(this@DeviceActivity, getString(R.string.device_borrowed_by_other_user), Toast.LENGTH_SHORT).show()
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
        displayGenericConfirmationLayout()

        device_save_btn.setOnClickListener { saveInventoryState() }
        device_edit_btn.setOnClickListener {
            //TODO go to edit device mode
        }

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
        device_read_holder.visibility = View.VISIBLE
        device_edit_owner.visibility = View.VISIBLE
        device_edit_default_location.visibility = View.VISIBLE
        device_edit_nst.visibility = View.VISIBLE
        //inventory
        device_edit_inventory_number.visibility = View.VISIBLE
        device_edit_inventory_comment.visibility = View.VISIBLE
        device_edit_inventory_result.visibility = View.VISIBLE
    }

    private fun saveInventoryState() {
        val checkedInventoryState = findViewById<RadioButton>(device_edit_inventory_result.checkedRadioButtonId)
        device?.inventoryRecord?.inventoryState = InventoryState.valueOf(checkedInventoryState.text.toString())

        intent.putExtra(resultParameterName, Gson().toJson(device))
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun setCalibrationView() {
        //layouts
        device_layout_holder.visibility = View.GONE
        device_layout_department.visibility = View.GONE
        device_layout_nst.visibility = View.VISIBLE
        device_layout_inventory_number.visibility = View.VISIBLE
        device_layout_last_calibration_date.visibility = View.VISIBLE
        device_layout_calibration_period.visibility = View.VISIBLE
        device_layout_calibration_new_date.visibility = View.VISIBLE

        //fields
        device_read_qr_code.visibility = View.VISIBLE
        device_read_device_type.visibility = View.VISIBLE
        device_read_serial_number.visibility = View.VISIBLE
        device_read_holder.visibility = View.VISIBLE
        device_edit_owner.visibility = View.VISIBLE
        device_edit_default_location.visibility = View.VISIBLE
        device_edit_nst.visibility = View.VISIBLE
        device_edit_inventory_number.visibility = View.VISIBLE
        device_edit_calibration_new_date.setOnClickListener { view -> newDateListener(view) }
    }

    private fun setElRevisionView() {
        displayGenericConfirmationLayout()

//        device_passed_btn.setOnClickListener { PassedElectricRevisionDialog().showDialog(this) }
//        device_failed_btn.setOnClickListener { FailedElectricRevisionDialog().showDialog(this) }

        device_read_qr_code.visibility = View.VISIBLE
        device_read_device_type.visibility = View.VISIBLE
        device_read_serial_number.visibility = View.VISIBLE
        device_read_holder.visibility = View.VISIBLE
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
        device_edit_electric_revision_new_date.setOnClickListener { view -> newDateListener(view) }
    }

    private fun newDateListener(it: View?) {
        val et = it as EditText
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            et.setText(SimpleDateFormat(DateParser.datePattern,
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
