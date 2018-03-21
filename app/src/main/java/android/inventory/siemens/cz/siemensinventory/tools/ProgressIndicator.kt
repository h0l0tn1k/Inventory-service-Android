package android.inventory.siemens.cz.siemensinventory.tools

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import javax.security.auth.login.Configuration.setConfiguration
import android.graphics.Color.parseColor
import com.bluehomestudio.progresswindow.ProgressWindow
import com.bluehomestudio.progresswindow.ProgressWindowConfiguration


/**
 * Created by Stefan on 21-Mar-18.
 */
class ProgressIndicator {
    object Builder {
//        fun create ( activity : AppCompatActivity): ProgressDialog {
//            val progressDialog = ProgressDialog(activity)
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//            progressDialog.setIcon(android.R.drawable.ic_dialog_info)
////            progressDialog.setTitle("Carregando...")
////            progressDialog.setMessage("Obtendo palestras, aguarde...")
//            progressDialog.setCancelable(false)
//            progressDialog.
//            progressDialog.isIndeterminate = true
//            return progressDialog
//        }

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