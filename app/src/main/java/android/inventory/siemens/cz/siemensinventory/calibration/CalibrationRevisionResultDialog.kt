package android.inventory.siemens.cz.siemensinventory.calibration

import android.content.DialogInterface
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.device.DevActivity
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import com.shawnlin.numberpicker.NumberPicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by Stefan Matta on 24.03.2018.
 */

class CalibrationRevisionResultDialog {

    private var context : DevActivity? = null
    private var dialogContentView : View? = null
    private var revDateDatePicker : DatePicker? = null

    fun showDialog(context: DevActivity) {
        dialogContentView = context.layoutInflater.inflate(R.layout.dialog_device_calibration, null)

        val dialog = AlertDialog.Builder(context)
                .setMessage(context.getText(R.string.select_calibration_date))
                .setPositiveButton(context.getText(R.string.save), positiveDialogClickListener)
                .setNegativeButton(context.getText(R.string.cancel), positiveDialogClickListener)
                .create()

        revDateDatePicker = dialogContentView?.findViewById(R.id.calibration_date) as DatePicker
        revDateDatePicker?.maxDate = Date().time

        this.context = context
        dialog.setView(dialogContentView)
        dialog.show()
    }

    private var positiveDialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                context?.setCalibrationParams(getCalibrationResult())
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                //do nothing
            }
        }
    }

    private fun getCalibrationResult() : CalibrationResult {
        val cal = Calendar.getInstance()
        val dateP = revDateDatePicker as DatePicker
        cal.set(dateP.year, dateP.month, dateP.dayOfMonth)
        return CalibrationResult(cal.time)
    }
}