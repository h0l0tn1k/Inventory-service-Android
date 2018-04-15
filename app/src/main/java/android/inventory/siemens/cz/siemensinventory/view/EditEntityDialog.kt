package android.inventory.siemens.cz.siemensinventory.view

import android.app.Activity
import android.content.DialogInterface
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.device.DevActivity
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.shawnlin.numberpicker.NumberPicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by Stefan Matta on 24.03.2018.
 */

class EditEntityDialog {

    private var dialogContentView : View? = null

    fun showDialog(context: Activity, entity: ViewEntity) {
        dialogContentView = context.layoutInflater.inflate(R.layout.dialog_edit_entity, null)

        val dialog = AlertDialog.Builder(context)
                .setMessage("Edit of " + entity.name)
                .setPositiveButton(context.getText(R.string.save), positiveDialogClickListener)
                .setNegativeButton(context.getText(R.string.cancel), positiveDialogClickListener)
                .create()

        getNameEditText().setText(entity.name)
        getNameEditText().requestFocus()
        dialog.setView(dialogContentView)

        dialog.show()
    }

    private fun getNameEditText() : EditText {
        return dialogContentView?.findViewById(R.id.dialog_edit_entity_name) as EditText
    }

    private var positiveDialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                //TODO: getNameEditText()
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                //do nothing
            }
        }
    }
}