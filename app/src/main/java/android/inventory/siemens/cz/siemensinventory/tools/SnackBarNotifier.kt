package android.inventory.siemens.cz.siemensinventory.tools

import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by Stefan Matta on 22.03.2018.
 */
class SnackBarNotifier(
        val layout : View,
        private val activity : Activity
) {

    fun show(text : String) {
        hideKeyboard()
        Snackbar.make(layout, text, Snackbar.LENGTH_LONG).show()
    }

    private fun hideKeyboard() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if(imm != null && activity.currentFocus != null) {
            imm.hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
        }
    }
}