package android.inventory.siemens.cz.siemensinventory.tools

import android.app.Activity
import android.graphics.Color
import com.bluehomestudio.progresswindow.ProgressWindow
import com.bluehomestudio.progresswindow.ProgressWindowConfiguration


/**
 * Created by Stefan on 21-Mar-18.
 */
class ProgressIndicator {
    object Builder {

        fun create(activity : Activity): ProgressWindow {

            val progressWindow = ProgressWindow.getInstance(activity)
            val progressWindowConfiguration = ProgressWindowConfiguration()
            progressWindowConfiguration.backgroundColor = Color.parseColor("#32000000")
            progressWindowConfiguration.progressColor = Color.WHITE
            progressWindow.setConfiguration(progressWindowConfiguration)
            return progressWindow
        }
    }
}