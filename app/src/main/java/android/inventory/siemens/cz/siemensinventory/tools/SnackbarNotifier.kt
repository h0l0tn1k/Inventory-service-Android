package android.inventory.siemens.cz.siemensinventory.tools

import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by I333206 on 22.03.2018.
 */
class SnackbarNotifier(
        val layout : View,
        private val activity : AppCompatActivity
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