package android.inventory.siemens.cz.siemensinventory.electricrevision

import android.content.DialogInterface
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.device.DevActivity
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import com.shawnlin.numberpicker.NumberPicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by I333206 on 24.03.2018.
 */

class PassedElectricRevisionDialog {

    private var context : DevActivity? = null
    private var dialogContentView : View? = null
    private val dateFormat: String = "yyyy-MM-dd"
    private var date : Date? = null

    fun showDialog(context: DevActivity) {
        dialogContentView = context.layoutInflater.inflate(R.layout.dialog_device_el_revision, null)

        val dialog = AlertDialog.Builder(context)
                .setMessage("Device Revision Properties")
                .setPositiveButton(context.getText(R.string.save), positiveDialogClickListener)
                .setNegativeButton(context.getText(R.string.cancel), positiveDialogClickListener)
                .create()

        val revDateTextView = dialogContentView?.findViewById(R.id.el_revision_date) as TextView
        revDateTextView.text = getTodaysDateFormatted()

        this.context = context
        dialog.setView(dialogContentView)
        dialog.show()
    }

    private fun getTodaysDateFormatted() : String {
        date = Date()
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
    }

    private var positiveDialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                context?.setPassedRevisionParams(getElectricRevisionResult())
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                //do nothing
            }
        }
    }

    private fun getElectricRevisionResult() : ElectricRevisionResult {
        val periodTv = dialogContentView?.findViewById(R.id.el_revision_period) as NumberPicker

        return ElectricRevisionResult(periodTv.value, date as Date)
    }
}