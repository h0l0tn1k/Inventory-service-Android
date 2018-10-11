package android.inventory.siemens.cz.siemensinventory.view

import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.view.View

class ViewMode(var edit : Boolean) {

    fun getVisibilityForEdit() : Int {
        return if (edit) View.VISIBLE else View.GONE
    }

    fun getVisibilityForRead() : Int {
        return if (edit) View.GONE else View.VISIBLE
    }

    /**
     * Do not show Edit button if user doesn't have write permission
     * Also do not show if view is in edit mode
     */
    fun getShowUserEdit() : Int {
        return if (!edit && AppData.loginUserScd?.flagWrite == true) View.VISIBLE else View.GONE
    }
}