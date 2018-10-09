package android.inventory.siemens.cz.siemensinventory.activities

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.LoginServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.entity.ServiceSettings
import android.inventory.siemens.cz.siemensinventory.tools.SnackbarNotifier
import android.inventory.siemens.cz.siemensinventory.tools.TextViewHelper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var snackbarNotifier: SnackbarNotifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        snackbarNotifier = SnackbarNotifier(login_activity_layout, this)

        btn_login?.setOnClickListener { loginUser() }
        login_btn_connection?.setOnClickListener { launchSettings() }

        checkConnectionToService()
    }

    override fun onBackPressed() {
        //do nothing
    }

    private fun checkConnectionToService() {
        if (ServiceSettings(this).isUrlWellFormated()) {
            ServiceSettings(this).checkConnection().enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response?.isSuccessful != true) {
                        showMessageAndShakeWithConnectionIcon(getString(R.string.error_cannot_connect_to_service))
                    }
                }

                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    showMessageAndShakeWithConnectionIcon(getString(R.string.error_cannot_connect_to_service))
                }
            })
        } else {
            showMessageAndShakeWithConnectionIcon(getString(R.string.service_url_not_valid))
        }
    }

    private fun showMessageAndShakeWithConnectionIcon(message: String) {
        snackbarNotifier?.show(message)
        login_btn_connection.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
    }

    private fun launchSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun loginUser() {
        if (isUserInputValid() && ServiceSettings(this).isUrlWellFormated()) {
            fireLoginUserRequest()
        }
    }

    private fun fireLoginUserRequest() {
        val user = LoginServiceApi.Factory.create(this).login(login_email?.text.toString(), login_password?.text.toString())

        user.enqueue(object : Callback<LoginUserScd> {
            override fun onFailure(call: Call<LoginUserScd>?, t: Throwable?) {
                AppData.loginUserScd = null
                snackbarNotifier?.show(getString(R.string.error_cannot_connect_to_service))
                setResult(Activity.RESULT_CANCELED, intent)
            }

            override fun onResponse(call: Call<LoginUserScd>?, response: Response<LoginUserScd>) {
                if (response.isSuccessful) {
                    val receivedUser = response.body() as LoginUserScd
                    AppData.loginUserScd = receivedUser
                    val intent = Intent()
                    intent.putExtra("user", Gson().toJson(receivedUser))
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    val message = when (response.code()) {
                        401 -> getString(R.string.invalid_credentials)
                        else -> getString(R.string.unknown_error)
                    }
                    snackbarNotifier?.show(message)
                }
            }
        })
    }

    private fun isUserInputValid(): Boolean {
        return TextViewHelper().withContext(this).isEmailValid(login_email).and.isNotEmpty(login_password, getString(R.string.password_empty)).isValid
    }
}
