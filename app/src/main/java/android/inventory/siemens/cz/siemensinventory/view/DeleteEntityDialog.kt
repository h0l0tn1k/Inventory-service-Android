package android.inventory.siemens.cz.siemensinventory.view

import android.app.Activity
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
 * Created by Stefan Matta on 24.03.2018.
 */

class DeleteEntityDialog {

    fun showDialog(context: Activity, entity: ViewEntity) {

        val dialog = AlertDialog.Builder(context)
                .setMessage("Do you want to delete \"" + entity.name + "\"?")
                .setTitle(context.getString(R.string.delete))
                .setPositiveButton(context.getText(R.string.yes), positiveDialogClickListener)
                .setNegativeButton(context.getText(R.string.no), positiveDialogClickListener)
                .create()
        dialog.show()
    }

    private var positiveDialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {

            }
            DialogInterface.BUTTON_NEGATIVE -> {
                //do nothing
            }
        }
    }
}