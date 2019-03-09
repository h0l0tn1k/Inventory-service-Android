package android.inventory.siemens.cz.siemensinventory.borrow

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceState
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView

class BorrowDialog(var context: Activity) {

    private var dialog: Dialog? = null
    private var commentTv: TextView? = null
    private var deviceStateSpinner: Spinner? = null

    fun buildDialog(device: Device?, borrow: Boolean, onSaveListener: DialogInterface.OnClickListener) {
        val title = context.getString(if (borrow) R.string.borrow_device else R.string.return_device)
        val currHolder = if (borrow) AppData.loginUserScd?.getFullName() else ""

        val view = context.layoutInflater.inflate(R.layout.dialog_borrow, null)

        initViewFields(view, device, currHolder, borrow)

        dialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(context.getText(R.string.save), onSaveListener)
                .setNegativeButton(context.getText(R.string.cancel)) { _, _ -> dialog?.dismiss() }
                .create()
    }

    private fun initViewFields(view: View, device: Device?, currHolder: String?, borrow: Boolean) {
        val ownerTv = view.findViewById(R.id.dialog_borrow_owner) as TextView
        ownerTv.text = device?.getOwnerFullName()

        val currHolderTv = view.findViewById(R.id.dialog_borrow_current_holder) as TextView
        currHolderTv.text = currHolder

        commentTv = view.findViewById(R.id.dialog_borrow_comment) as EditText
        commentTv?.text = device?.comment

        deviceStateSpinner = view.findViewById(R.id.dialog_borrow_device_state) as Spinner
        deviceStateSpinner?.isEnabled = false
        deviceStateSpinner?.adapter = ArrayAdapter(context.baseContext, android.R.layout.simple_spinner_dropdown_item,
                getDeviceStateFromAppData(if(borrow) "Borrowed" else "OK"))
    }

    private fun getDeviceStateFromAppData(stateName: String) : List<DeviceState> {
        val states = AppData.deviceStates?.filter { x -> x.name == stateName }
        if(states?.isNotEmpty() == true) {
            return states
        }
        return emptyList()
    }
    fun show() {
        dialog?.show()
    }

    fun getComment(): String {
        return commentTv?.text.toString()
    }

    fun getDeviceState(): DeviceState {
        return deviceStateSpinner?.selectedItem as DeviceState
    }
}