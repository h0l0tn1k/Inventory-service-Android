package android.inventory.siemens.cz.siemensinventory.activities

import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.adapters.DeviceParametersAdapter
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.entity.KeyValueParameters
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.google.gson.Gson
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import kotlinx.android.synthetic.main.activity_device.*
import java.text.SimpleDateFormat
import java.util.*
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import android.content.DialogInterface




class DeviceActivity : AppCompatActivity(), RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<DeviceActivity.Result> {

    private var rfaContent : RapidFloatingActionContentLabelList? = null
    private var rfabHelper : RapidFloatingActionHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val device = Gson().fromJson(intent.getStringExtra("device"), Device::class.java)

        initRFA()

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        //val addDateString = dateFormatter.format(device.addDate)
        val lastRevDateString = dateFormatter.format(device.lastRevisionDateString)

        val parameters = listOf(
                KeyValueParameters("Barcode Number", device.barcodeNumber),
                KeyValueParameters("Device Type", device.objectTypeName),
                KeyValueParameters("Serial Number", device.serialNumber),
                KeyValueParameters("Owner", device.ownerName),
                KeyValueParameters("Department", device.departmentName),
                KeyValueParameters("Current Holder", device.holderName),
                KeyValueParameters("Project", device.projectName),
                KeyValueParameters("Company Owner", device.companyOwnerName),
                //KeyValueParameters("Add Date", addDateString),
                KeyValueParameters("Status", device.deviceStateName),
                KeyValueParameters("Last Revision Date", lastRevDateString)
        )

        deviceParameters.adapter = DeviceParametersAdapter(this, parameters)
    }

    private fun initRFA() {
        setContent(listOf(getOkOption(), getNokOption()))

        rfabHelper = RapidFloatingActionHelper(this, electricRevisionLayout, electricRevisionBtn, rfaContent).build()
    }

    override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Result>) {
        rfabHelper?.toggleContent()
        handleRevisionResult(item.wrapper)
    }

    override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Result>) {
        rfabHelper?.toggleContent()
        handleRevisionResult(item.wrapper)
    }

    private fun handleRevisionResult(result : Result) {
        when(result) {
            Result.YES -> { handlePositiveResult() }
            Result.NO -> { handleNegativeResult() }
        }
    }

    private fun handlePositiveResult() {
        val dialogContentView = layoutInflater.inflate(R.layout.dialog_device_el_revision, null)

        val dialog = AlertDialog.Builder(this)
                .setMessage("Device Revision Properties")
                .setPositiveButton("Save", positiveDialogClickListener)
                .setNegativeButton("Close", positiveDialogClickListener)
                .create()

        dialog.setView(dialogContentView)
        dialog.show()
    }

    private fun handleNegativeResult() {
        AlertDialog.Builder(this)
                .setMessage("Are you sure device is not ok?")
                .setPositiveButton("Yes", negativeDialogClickListener)
                .setNegativeButton("No", negativeDialogClickListener)
                .show()
    }
    var positiveDialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                //Update DB
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                //do nothing
            }
        }
    }

    var negativeDialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                //Update DB
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                //do nothing
            }
        }
    }

    private fun setContent(items : List<RFACLabelItem<Result>>) {
        rfaContent = RapidFloatingActionContentLabelList(this)
        rfaContent?.setOnRapidFloatingActionContentLabelListListener(this)
        rfaContent?.items = items
        rfaContent?.setIconShadowRadius(5)
        rfaContent?.setIconShadowDy(5)
        rfaContent?.setIconShadowColor(0xff888888.toInt())
    }

    private fun getNokOption() : RFACLabelItem<Result> {
        val nokOption = RFACLabelItem<Result>()
        nokOption.label = "Device NOK"
        nokOption.resId = R.drawable.ic_close_red_800_24dp
        nokOption.iconNormalColor = 0xff4e342e.toInt()
        nokOption.iconPressedColor = 0xff3e2723.toInt()
        nokOption.wrapper = Result.NO
        nokOption.labelSizeSp = 18
        return nokOption
    }

    private fun getOkOption() : RFACLabelItem<Result> {
        val okOption = RFACLabelItem<Result>()
        okOption.label = "Device OK"
        okOption.resId = R.drawable.ic_check_green_a700_24dp
        okOption.iconNormalColor = 0xffd84315.toInt()
        okOption.iconPressedColor = 0xffbf360c.toInt()
        okOption.wrapper = Result.YES
        okOption.labelSizeSp = 18
        return okOption
    }

    enum class Result {
        YES, NO
    }
}
