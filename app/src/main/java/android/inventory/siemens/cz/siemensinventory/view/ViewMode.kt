package android.inventory.siemens.cz.siemensinventory.view

import android.view.View

class ViewMode(var edit : Boolean) {

    fun getVisibilityForEdit() : Int {
        return if (edit) View.VISIBLE else View.GONE
    }

    fun getVisibilityForRead() : Int {
        return if (edit) View.GONE else View.VISIBLE
    }
}