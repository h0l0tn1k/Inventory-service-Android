package android.inventory.siemens.cz.siemensinventory.view

import android.app.Activity
import android.content.DialogInterface
import android.inventory.siemens.cz.siemensinventory.R
import android.support.v7.app.AlertDialog
import android.view.View

/**
 * Created by Stefan Matta on 24.03.2018.
 */

class DeleteEntityDialog {

    private var dataProvider: EditEntityDataProvider? = null
    private var entityId: Long = 0

    fun showDialog(context: Activity, entity: ViewEntity, dataProvider: EditEntityDataProvider?) {
        this.dataProvider = dataProvider
        this.entityId = entity.id

        val dialog = AlertDialog.Builder(context)
                .setMessage("Do you want to delete \"" + entity.name + "\"?")
                .setTitle(context.getString(R.string.delete))
                .setPositiveButton(context.getText(R.string.yes), positiveDialogClickListener)
                .setNegativeButton(context.getText(R.string.no), positiveDialogClickListener)
                .create()
        dialog.show()
    }

    private var positiveDialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                dataProvider?.deleteData(entityId)
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                dialog.dismiss()
            }
        }
    }
}