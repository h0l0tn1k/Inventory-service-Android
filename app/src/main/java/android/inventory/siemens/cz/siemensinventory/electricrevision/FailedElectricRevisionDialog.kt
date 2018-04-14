package android.inventory.siemens.cz.siemensinventory.electricrevision

import android.app.Activity
import android.content.DialogInterface
import android.support.v7.app.AlertDialog

/**
 * Created by I333206 on 24.03.2018.
 */

class FailedElectricRevisionDialog {

    fun showDialog(context : Activity) {
        AlertDialog.Builder(context)
                .setMessage("Are you sure device is not ok?")
                .setPositiveButton("Yes", negativeDialogClickListener)
                .setNegativeButton("No", negativeDialogClickListener)
                .show()
    }

    private var negativeDialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which ->
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