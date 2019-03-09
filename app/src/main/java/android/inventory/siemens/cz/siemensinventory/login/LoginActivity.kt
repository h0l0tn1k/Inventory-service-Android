package android.inventory.siemens.cz.siemensinventory.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.BuildConfig
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.activities.SettingsActivity
import android.inventory.siemens.cz.siemensinventory.api.LoginUsersScdApi
import android.inventory.siemens.cz.siemensinventory.api.ServiceApiGenerator
import android.inventory.siemens.cz.siemensinventory.api.SiemensServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.entity.ServiceSettings
import android.inventory.siemens.cz.siemensinventory.tools.SnackBarNotifier
import android.inventory.siemens.cz.siemensinventory.tools.TextViewHelper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.view.animation.AnimationUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var snackBarNotifier: SnackBarNotifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        snackBarNotifier = SnackBarNotifier(login_activity_layout, this)

        btn_login?.setOnClickListener { loginUser() }
        login_btn_connection?.setOnClickListener { launchSettings() }

        checkConnectionToService()
    }

    override fun onBackPressed() {
        //do nothing
    }

    private fun checkConnectionToService() {
        if (ServiceSettings(this).isUrlWellFormatted()) {
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
        snackBarNotifier?.show(message)
        login_btn_connection.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
    }

    private fun launchSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun loginUser() {
        if (isUserInputValid() && ServiceSettings(this).isUrlWellFormatted()) {
            getAccessToken()
        }
    }

    private fun getAccessToken() {
        val username = login_email?.text.toString().trim()
        val password = login_password?.text.toString().trim()
        val prefs = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        val client = ServiceApiGenerator.Factory.createService(LoginServiceApi::class.java, this)
        val accessTokenCall = client.getNewAccessToken(SiemensServiceApi.getClientBaseAuthorization(this), username, password)

        accessTokenCall.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>?, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    val token = response.body() as AccessToken
                    prefs.edit().putBoolean("oauth.loggedin", true).apply()
                    prefs.edit().putString("oauth.accesstoken", token.access_token).apply()
                    prefs.edit().putString("oauth.tokentype", token.getTokenType()).apply()
                    AppData.accessToken = token
                    getCurrentUser(token)
                } else {
                    val message = when (response.code()) {
                        400, 401 -> getString(R.string.invalid_credentials)
                        else -> getString(R.string.unknown_error)
                    }
                    snackBarNotifier?.show(message)
                }
            }

            override fun onFailure(call: Call<AccessToken>?, t: Throwable?) {
                AppData.loginUserScd = null
                snackBarNotifier?.show(getString(R.string.error_cannot_connect_to_service))
                setResult(Activity.RESULT_CANCELED, intent)
            }
        })
    }

    private fun getCurrentUser(token: AccessToken) {
        ServiceApiGenerator.Factory.createService(LoginUsersScdApi::class.java, token, this@LoginActivity).getCurrentUser().enqueue(object: Callback<LoginUserScd> {
            override fun onFailure(call: Call<LoginUserScd>?, t: Throwable?) {
                snackBarNotifier?.show(getString(R.string.unknown_error))
            }

            override fun onResponse(call: Call<LoginUserScd>?, response: Response<LoginUserScd>) {
                if (response.isSuccessful) {
                    AppData.loginUserScd = response.body() as LoginUserScd
                    val intent = Intent()
                    intent.putExtra("user", Gson().toJson(AppData.loginUserScd))
                    setResult(RESULT_OK, intent)
                    finish()
                } else {

                    snackBarNotifier?.show(getString(R.string.unknown_error))
                }
            }
        })
    }

    private fun isUserInputValid(): Boolean {
        return TextViewHelper().withContext(this).isEmailValid(login_email).and.isNotEmpty(login_password, getString(R.string.password_empty)).isValid
    }
}
