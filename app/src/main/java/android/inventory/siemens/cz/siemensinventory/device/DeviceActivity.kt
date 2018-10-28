package android.inventory.siemens.cz.siemensinventory.device

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.activities.ScanActivity
import android.inventory.siemens.cz.siemensinventory.api.*
import android.inventory.siemens.cz.siemensinventory.api.entity.*
import android.inventory.siemens.cz.siemensinventory.borrow.BorrowDialog
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.databinding.ActivityDeviceCreateBinding
import android.inventory.siemens.cz.siemensinventory.entity.enums.ScanIntent
import android.inventory.siemens.cz.siemensinventory.inventory.InventoryRecord
import android.inventory.siemens.cz.siemensinventory.tools.DateParser
import android.inventory.siemens.cz.siemensinventory.tools.SnackBarNotifier
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.activity_device_create.*
import kotlinx.android.synthetic.main.device_borrow_section.*
import kotlinx.android.synthetic.main.device_generic_confirmation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class DeviceActivity : AppCompatActivity() {

    private val EDIT_DEVICE_ACTIVITY_REQUEST_CODE = 0
    private val SCAN_ACTIVITY_REQUEST_CODE = 1
    private val barcode = "barcode"
    private var device: Device? = null
    private var deviceIntent: DeviceIntent? = null
    private val resultParameterName: String = "result"
    private var deviceBinding: ActivityDeviceCreateBinding? = null
    private val calendar = Calendar.getInstance()
    private var snackbarNotifier: SnackBarNotifier? = null

    //apis
    private var deviceApi = DeviceServiceApi.Factory.create(this)
    private var deviceTypeApi = DeviceTypeServiceApi.Factory.create(this)
    private var loginUserApi = LoginUsersScdApi.Factory.create(this)
    private var departmentApi = DepartmentsServiceApi.Factory.create(this)
    private var companyOwnerApi = CompanyOwnerServiceApi.Factory.create(this)
    private var projectApi = ProjectServiceApi.Factory.create(this)
    private var deviceStateApi = DeviceStatesServiceApi.Factory.create(this)

    //arrays
    private val deviceTypes = arrayListOf<DeviceType>()
    private val departments = arrayListOf<Department>()
    private val companyOwners = arrayListOf<CompanyOwner>()
    private val projects = arrayListOf<Project>()
    private val deviceStates = arrayListOf<DeviceState>()
    private val users = arrayListOf<LoginUserScd>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_create)
        deviceBinding = DataBindingUtil.setContentView(this, R.layout.activity_device_create) as ActivityDeviceCreateBinding
        snackbarNotifier = SnackBarNotifier(activity_device_layout, this)

        device = getDeviceFromIntent()
        deviceBinding?.device = device
        handleIntent()
        loadSpinnerValues()
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

    private fun setHolder(user: LoginUserScd?) {
        device?.holder = user
        deviceApi.updateDevice(device?.id, device).enqueue(object : Callback<Device> {
            override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                if (response?.isSuccessful == true) {
                    if (isBorrowedByCurrentUser()) {
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

    private fun loadDeviceTypes() {
        deviceTypeApi.getDeviceTypes().enqueue(object : Callback<List<DeviceType>> {
            override fun onFailure(call: Call<List<DeviceType>>?, t: Throwable?) {
                snackbarNotifier?.show(getString(R.string.error_loading_data))
            }

            override fun onResponse(call: Call<List<DeviceType>>?, response: Response<List<DeviceType>>?) {
                if (response?.isSuccessful == true) {
                    if (response.body() != null) {
                        deviceTypes.addAll(response.body() as List<DeviceType>)
                    }
                    deviceTypes.add(0, DeviceType())
                    device_edit_device_type.adapter = ArrayAdapter<DeviceType>(this@DeviceActivity,
                            android.R.layout.simple_spinner_dropdown_item, deviceTypes)
                    device_edit_device_type.setSelection(deviceTypes.indexOf(deviceBinding?.device?.deviceType))
                } else {
                    snackbarNotifier?.show(getString(R.string.error_loading_data))
                }
            }
        })
    }

    private fun loadUsers() {
        loginUserApi.getUsers().enqueue(object : Callback<List<LoginUserScd>> {
            override fun onFailure(call: Call<List<LoginUserScd>>?, t: Throwable?) {
                snackbarNotifier?.show(getString(R.string.error_loading_data))
            }

            override fun onResponse(call: Call<List<LoginUserScd>>?, response: Response<List<LoginUserScd>>?) {
                if (response?.isSuccessful == true) {
                    if (response.body() != null) {
                        users.addAll(response.body() as List<LoginUserScd>)
                    }
                    //add empty user option
                    users.add(0, LoginUserScd(0, 0))
                    device_edit_owner.adapter = ArrayAdapter<LoginUserScd>(this@DeviceActivity,
                            android.R.layout.simple_spinner_dropdown_item, users)
                    device_edit_owner.setSelection(users.indexOf(deviceBinding?.device?.owner))
                    device_edit_holder.adapter = ArrayAdapter<LoginUserScd>(this@DeviceActivity,
                            android.R.layout.simple_spinner_dropdown_item, users)
                    device_edit_holder.setSelection(users.indexOf(deviceBinding?.device?.holder))
                } else {
                    snackbarNotifier?.show(getString(R.string.error_loading_data))
                }
            }
        })
    }

    private fun loadDepartments() {
        departmentApi.getDepartments().enqueue(object : Callback<List<Department>> {
            override fun onFailure(call: Call<List<Department>>?, t: Throwable?) {
                snackbarNotifier?.show(getString(R.string.error_loading_data))
            }

            override fun onResponse(call: Call<List<Department>>?, response: Response<List<Department>>?) {
                if (response?.isSuccessful == true) {
                    if (response.body() != null) {
                        departments.addAll(response.body() as List<Department>)
                    }
                    departments.add(0, Department(-1L, ""))
                    device_edit_department.adapter = ArrayAdapter<Department>(this@DeviceActivity,
                            android.R.layout.simple_spinner_dropdown_item, departments)
                    device_edit_department.setSelection(departments.indexOf(deviceBinding?.device?.department))
                } else {
                    snackbarNotifier?.show(getString(R.string.error_loading_data))
                }
            }
        })
    }

    private fun loadCompanyOwners() {
        companyOwnerApi.getCompanyOwners().enqueue(object : Callback<List<CompanyOwner>> {
            override fun onFailure(call: Call<List<CompanyOwner>>?, t: Throwable?) {
                snackbarNotifier?.show(getString(R.string.error_loading_data))
            }

            override fun onResponse(call: Call<List<CompanyOwner>>?, response: Response<List<CompanyOwner>>?) {
                if (response?.isSuccessful == true) {
                    if (response.body() != null) {
                        companyOwners.addAll(response.body() as List<CompanyOwner>)
                    }
                    companyOwners.add(0, CompanyOwner(-1L, ""))
                    device_edit_company_owner.adapter = ArrayAdapter<CompanyOwner>(this@DeviceActivity,
                            android.R.layout.simple_spinner_dropdown_item, companyOwners)
                    device_edit_company_owner.setSelection(companyOwners.indexOf(deviceBinding?.device?.companyOwner))
                } else {
                    snackbarNotifier?.show(getString(R.string.error_loading_data))
                }
            }
        })
    }

    private fun loadProjects() {
        projectApi.getProjects().enqueue(object : Callback<List<Project>> {
            override fun onFailure(call: Call<List<Project>>?, t: Throwable?) {
                snackbarNotifier?.show(getString(R.string.error_loading_data))
            }

            override fun onResponse(call: Call<List<Project>>?, response: Response<List<Project>>?) {
                if (response?.isSuccessful == true) {
                    if (response.body() != null) {
                        projects.addAll(response.body() as List<Project>)
                    }
                    projects.add(0, Project(-1L, ""))
                    device_edit_project.adapter = ArrayAdapter<Project>(this@DeviceActivity,
                            android.R.layout.simple_spinner_dropdown_item, projects)
                    device_edit_project.setSelection(projects.indexOf(deviceBinding?.device?.project))
                } else {
                    snackbarNotifier?.show(getString(R.string.error_loading_data))
                }
            }
        })
    }

    private fun loadDeviceStates() {
        deviceStateApi.getDeviceStates().enqueue(object : Callback<List<DeviceState>> {
            override fun onFailure(call: Call<List<DeviceState>>?, t: Throwable?) {
                snackbarNotifier?.show(getString(R.string.error_loading_data))
            }

            override fun onResponse(call: Call<List<DeviceState>>?, response: Response<List<DeviceState>>?) {
                if (response?.isSuccessful == true) {
                    if (response.body() != null) {
                        deviceStates.addAll(response.body() as List<DeviceState>)
                    }
                    device_edit_status.adapter = ArrayAdapter<DeviceState>(this@DeviceActivity,
                            android.R.layout.simple_spinner_dropdown_item, deviceStates)
                    device_edit_status.setSelection(deviceStates.indexOf(deviceBinding?.device?.deviceState))
                } else {
                    snackbarNotifier?.show(getString(R.string.error_loading_data))
                }
            }
        })
    }

    private fun loadSpinnerValues() {
        loadDeviceTypes()
        loadDepartments()
        loadCompanyOwners()
        loadProjects()
        loadDeviceStates()
        loadUsers()
    }

    private fun setCreateView() {
        setEditView()
        device_layout_edit_qr_code.visibility = View.VISIBLE
        device_edit_btn.visibility = View.GONE
        device_edit_add_date.setText(SimpleDateFormat(DateParser.datePattern, Locale.getDefault()).format(calendar.time))
        val barcode = intent.getStringExtra("barcode")
        deviceBinding?.device = Device(id = 0, barcodeNumber = barcode, calibration = DeviceCalibration(0),
                revision = DeviceElectricRevision(0), inventoryRecord = InventoryRecord(0))
    }

    private fun setEditView() {
        device_save_btn.setOnClickListener {
            //todo validation?
            val device = deviceBinding?.device as Device

            val deviceType = device_edit_device_type.selectedItem as DeviceType
            device.deviceType = if (deviceType.isEmpty()) null else deviceType

            val department = device_edit_department.selectedItem as Department
            device.department = if (department.isEmpty()) null else department

            val companyOwner = device_edit_company_owner.selectedItem as CompanyOwner
            device.companyOwner = if (companyOwner.isEmpty()) null else companyOwner

            val project = device_edit_project.selectedItem as Project
            device.project = if (project.isEmpty()) null else project

            val owner = device_edit_owner.selectedItem as LoginUserScd
            device.owner = if (owner.isEmpty()) null else owner

            val holder = device_edit_holder.selectedItem as LoginUserScd
            device.holder = if (holder.isEmpty()) null else holder

            device.deviceState = device_edit_status.selectedItem as DeviceState

            if (deviceIntent == DeviceIntent.EDIT) {
                deviceApi.updateDevice(device.id, device).enqueue(object : Callback<Device> {
                    override fun onFailure(call: Call<Device>?, t: Throwable?) {
                        snackbarNotifier?.show(getString(R.string.unable_to_save_changes))
                    }

                    override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                        if (response?.isSuccessful == true) {
                            intent.putExtra("device", Gson().toJson(response.body()))
                            snackbarNotifier?.show(getString(R.string.able_to_save_changes))
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else {
                            processUnsuccessfulResponse<Device>(response)
                        }
                    }
                })
            } else if (deviceIntent == DeviceIntent.CREATE) {
                deviceApi.createDevice(device).enqueue(object : Callback<Device> {
                    override fun onFailure(call: Call<Device>?, t: Throwable?) {
                        snackbarNotifier?.show(getString(R.string.unable_to_save_changes))
                    }

                    override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                        if (response?.isSuccessful == true) {
                            intent.putExtra("result", Gson().toJson(response.body()))
                            intent.putExtra("intent", DeviceIntent.CREATE.toString())
                            snackbarNotifier?.show(getString(R.string.able_to_save_changes))
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else {
                            processUnsuccessfulResponse<Device>(response)
                        }
                    }
                })
            }
        }
        device_edit_btn.visibility = View.GONE
        device_layout_edit_qr_code.visibility = View.VISIBLE
        device_edit_device_type.visibility = View.VISIBLE
        device_edit_serial_number.visibility = View.VISIBLE
        device_edit_owner.visibility = View.VISIBLE
        device_edit_holder.visibility = View.VISIBLE
        device_edit_default_location.visibility = View.VISIBLE
        device_edit_department.visibility = View.VISIBLE

        device_layout_company_owner.visibility = View.VISIBLE
        device_edit_company_owner.visibility = View.VISIBLE

        device_layout_project.visibility = View.VISIBLE
        device_edit_project.visibility = View.VISIBLE

        device_layout_nst.visibility = View.VISIBLE
        device_edit_nst.visibility = View.VISIBLE

        device_edit_add_date.visibility = View.VISIBLE
        device_edit_add_date.setOnClickListener { view -> newDateListener(view) }

        device_layout_status.visibility = View.VISIBLE
        device_edit_status.visibility = View.VISIBLE

        device_layout_comment.visibility = View.VISIBLE
        device_edit_comment.visibility = View.VISIBLE

        device_edit_scan_barcode_number.visibility = View.VISIBLE
        device_edit_scan_barcode_number.setOnClickListener {
            startScanQrCodeValue()
        }
    }

    private fun setDisplayView() {
        displayGenericConfirmationLayout()
        device_save_btn.visibility = View.GONE

        device_layout_department.visibility = View.VISIBLE
        device_layout_add_date.visibility = View.VISIBLE
        device_layout_nst.visibility = View.VISIBLE
        device_layout_inventory_number.visibility = View.VISIBLE
        device_layout_status.visibility = View.VISIBLE
        device_layout_project.visibility = View.VISIBLE
        device_layout_company_owner.visibility = View.VISIBLE
        device_layout_comment.visibility = View.VISIBLE
        device_layout_nst.visibility = View.VISIBLE

        device_read_qr_code.visibility = View.VISIBLE
        device_read_device_type.visibility = View.VISIBLE
        device_read_serial_number.visibility = View.VISIBLE
        device_read_inventory_number.visibility = View.VISIBLE
        device_read_owner.visibility = View.VISIBLE
        device_read_holder.visibility = View.VISIBLE
        device_read_default_location.visibility = View.VISIBLE
        device_read_status.visibility = View.VISIBLE
        device_read_department.visibility = View.VISIBLE
        device_read_project.visibility = View.VISIBLE
        device_read_company_owner.visibility = View.VISIBLE
        device_read_add_date.visibility = View.VISIBLE
        device_read_comment.visibility = View.VISIBLE
        device_read_nst.visibility = View.VISIBLE
    }

    private fun setInventoryView() {
        displayGenericConfirmationLayout()

        device_save_btn.setOnClickListener { saveInventoryState() }

        //layouts
        device_layout_department.visibility = View.GONE
        device_layout_add_date.visibility = View.GONE
        device_layout_nst.visibility = View.VISIBLE
        device_layout_inventory_number.visibility = View.VISIBLE
        device_layout_inventory_comment.visibility = View.VISIBLE
        device_layout_inventory_result.visibility = View.VISIBLE
        device_layout_edit_qr_code.visibility = View.GONE

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
        if (device?.inventoryRecord == null) {
            device?.inventoryRecord = InventoryRecord(device?.id!!)
        }
        val checkedInventoryState = findViewById<RadioButton>(device_edit_inventory_result.checkedRadioButtonId)
        val inventoryState = InventoryState.valueOf(checkedInventoryState.text.toString())
        if (inventoryState != InventoryState.OK) {
            if (device_edit_inventory_comment.text.isBlank()) {
                snackbarNotifier?.show(getString(R.string.inventory_comment_cannot_be_empty))
                return
            }
        }
        device?.inventoryRecord?.inventoryState = inventoryState
        finishActivityWithResult(device, DeviceIntent.INVENTORY)
    }

    private fun setCalibrationView() {
        displayGenericConfirmationLayout()

        device_save_btn.setOnClickListener { saveCalibration() }
        //layouts
        device_layout_holder.visibility = View.GONE
        device_layout_department.visibility = View.GONE
        device_layout_nst.visibility = View.VISIBLE
        device_layout_inventory_number.visibility = View.VISIBLE
        device_layout_last_calibration_date.visibility = View.VISIBLE
        device_layout_calibration_period.visibility = View.VISIBLE
        device_layout_calibration_new_date.visibility = View.VISIBLE
        device_layout_edit_qr_code.visibility = View.GONE

        //fields
        device_read_qr_code.visibility = View.VISIBLE
        device_read_device_type.visibility = View.VISIBLE
        device_read_serial_number.visibility = View.VISIBLE
        device_read_holder.visibility = View.VISIBLE
        device_edit_owner.visibility = View.VISIBLE
        device_edit_default_location.visibility = View.VISIBLE
        device_edit_nst.visibility = View.VISIBLE
        device_edit_inventory_number.visibility = View.VISIBLE
        device_edit_calibration_new_date.setText(DateParser.toString(Date()))
        device_edit_calibration_new_date.setOnClickListener { view -> newDateListener(view) }
    }

    private fun saveCalibration() {
        if (device?.calibration == null) {
            device?.calibration = DeviceCalibration(device?.id!!)
        }
        device?.calibration?.calibrationInterval = device_edit_calibration_period.selectedItemPosition
        device?.calibration?.lastCalibrationDateString = device_edit_calibration_new_date.text.toString()
        finishActivityWithResult(device, DeviceIntent.CALIBRATION)
    }

    private fun setElRevisionView() {
        displayGenericConfirmationLayout()

        device_save_btn.setOnClickListener { saveRevisionState() }

        device_read_qr_code.visibility = View.VISIBLE
        device_read_device_type.visibility = View.VISIBLE
        device_read_serial_number.visibility = View.VISIBLE
        device_read_holder.visibility = View.VISIBLE
        device_edit_owner.visibility = View.VISIBLE
        device_edit_default_location.visibility = View.VISIBLE
        device_edit_nst.visibility = View.VISIBLE
        device_edit_inventory_number.visibility = View.VISIBLE

        device_layout_edit_qr_code.visibility = View.GONE
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
        device_edit_electric_revision_new_date.setText(DateParser.toString(Date()))
        device_edit_electric_revision_new_date.setOnClickListener { view -> newDateListener(view) }
    }

    private fun saveRevisionState() {
        if (device?.revision == null) {
            device?.revision = DeviceElectricRevision(device?.id!!)
        }
        device?.revision?.revisionInterval = device_edit_electric_revision_period.selectedItemPosition
        device?.revision?.lastRevisionDateString = device_edit_electric_revision_new_date.text.toString()
        finishActivityWithResult(device, DeviceIntent.EL_REVISION)
    }

    private fun finishActivityWithResult(device: Device?, viewIntent: DeviceIntent) {
        intent.putExtra(resultParameterName, Gson().toJson(device))
        intent.putExtra("intent", viewIntent.toString())
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun newDateListener(it: View?) {
        val et = it as EditText
        val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            if (calendar.time.after(Date())) {
                snackbarNotifier?.show(getString(R.string.wrong_date))
            } else {
                et.setText(SimpleDateFormat(DateParser.datePattern,
                        Locale.getDefault()).format(calendar.time))
            }
        }, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    private fun displayGenericConfirmationLayout() {
        layoutInflater.inflate(R.layout.device_generic_confirmation, null)
        device_close_btn.setOnClickListener { finish() }
        device_edit_btn.visibility = if (AppData.loginUserScd?.flagWrite == true) View.VISIBLE else View.GONE
    }

    private fun startDeviceEditActivity(editMode: Boolean, device: Device?) {
        val deviceActivity = Intent(this, DeviceActivity::class.java)
        deviceActivity.putExtra("device", Gson().toJson(device))
        deviceActivity.putExtra("intent", DeviceIntent.EDIT.toString())
        deviceActivity.putExtra("editMode", editMode)
        startActivityForResult(deviceActivity, EDIT_DEVICE_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SCAN_ACTIVITY_REQUEST_CODE -> handleScanActivityResult(resultCode, data)
            EDIT_DEVICE_ACTIVITY_REQUEST_CODE -> handleEditDeviceActivityResult(resultCode, data)
        }
    }

    private fun handleEditDeviceActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            intent.putExtra("device", data?.getStringExtra("device"))
            recreate()
        }
    }

    private fun handleScanActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val barcode = data?.getStringExtra(barcode)
            device_edit_qr_code.setText(barcode)
        }
    }

    private fun isBorrowedByCurrentUser(): Boolean {
        return device?.holder?.id == AppData.loginUserScd?.id
    }

    private fun isBorrowedByNoOne(): Boolean {
        return device?.holder?.id == null
    }

    private fun getDeviceFromIntent(): Device? {
        return Gson().fromJson(intent.getStringExtra("device"), Device::class.java)
    }

    private fun handleIntent() {
        deviceIntent = DeviceIntent.valueOf(intent.getStringExtra("intent"))

        when (deviceIntent) {
            DeviceIntent.BORROW -> {
                setBorrowView()
            }
            DeviceIntent.CALIBRATION -> {
                setCalibrationView()
            }
            DeviceIntent.EL_REVISION -> {
                setElRevisionView()
            }
            DeviceIntent.INVENTORY -> {
                setInventoryView()
            }
            DeviceIntent.EDIT -> {
                setEditView()
            }
            DeviceIntent.CREATE -> {
                setCreateView()
            }
            DeviceIntent.DISPLAY -> {
                setDisplayView()
            }
            else -> {
            }
        }
        device_close_btn.setOnClickListener { finish() }
        device_edit_btn.setOnClickListener {
            startDeviceEditActivity(true, getDeviceFromIntent())
        }
    }

    private fun startScanQrCodeValue() {
        val scanIntent = Intent(this, ScanActivity::class.java)
        scanIntent.putExtra("intent", ScanIntent.Barcode.toString())
        scanIntent.putExtra("parameterName", barcode)
        startActivityForResult(scanIntent, SCAN_ACTIVITY_REQUEST_CODE)
    }

    private fun <T> processUnsuccessfulResponse(response : Response<T>?) {
        try {
            val error = Gson().fromJson(response?.errorBody()?.string(), ErrorBody::class.java)
            snackbarNotifier?.show(error.message)
        } catch(e: JsonSyntaxException) {
            //not this type of error message
            snackbarNotifier?.show(getString(R.string.unable_to_save_changes))
        }
    }
}
