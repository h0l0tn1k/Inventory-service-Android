package android.inventory.siemens.cz.siemensinventory.dialogs

import android.app.Activity
import android.content.DialogInterface
import android.inventory.siemens.cz.siemensinventory.R
import android.support.v7.app.AlertDialog
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by I333206 on 24.03.2018.
 */

class PassedElectricRevisionDialog {

    fun showDialog(context: Activity) {
        val dialogContentView = context.layoutInflater.inflate(R.layout.dialog_device_el_revision, null)


        val dialog = AlertDialog.Builder(context)
                .setMessage("Device Revision Properties")
                .setPositiveButton("Save", positiveDialogClickListener)
                .setNegativeButton("Close", positiveDialogClickListener)
                .create()
//
//        val revDateTextView = context.findViewById(R.id.el_revision_date) as TextView
//        revDateTextView.text = getTodaysDateFormatted()

        dialog.setView(dialogContentView)
        dialog.show()
    }

    private fun getTodaysDateFormatted() : String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private var positiveDialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                //Update DB
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                //do nothing
            }
        }
    }
}