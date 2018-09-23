package android.inventory.siemens.cz.siemensinventory.electricrevision

import android.content.DialogInterface
import android.inventory.siemens.cz.siemensinventory.device.DevActivity
import android.support.v7.app.AlertDialog

/**
 * Created by Stefan Matta on 24.03.2018.
 */

class FailedElectricRevisionDialog {

    private var context: DevActivity? = null

    fun showDialog(context: DevActivity) {
        this.context = context
        AlertDialog.Builder(context)
                .setMessage("Are you sure device is not ok?")
                .setPositiveButton("Yes", negativeDialogClickListener)
                .setNegativeButton("No", negativeDialogClickListener)
                .show()
    }

    private var negativeDialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                this.context?.finish()
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                //do nothing
            }
        }
    }
}