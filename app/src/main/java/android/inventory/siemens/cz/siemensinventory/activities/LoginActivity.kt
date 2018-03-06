package android.inventory.siemens.cz.siemensinventory.activities

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.LoginServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login?.setOnClickListener {loginUser()}
    }

    private fun loginUser() {

        val user = LoginServiceApi.Factory.create().login(login_email?.text.toString(), login_password?.text.toString())

        user.enqueue(object: Callback<LoginUserScd> {
            override fun onFailure(call: Call<LoginUserScd>?, t: Throwable?) {
                Toast.makeText(
                        this@LoginActivity, "ERROR", Toast.LENGTH_LONG
                ).show()
                setResult(Activity.RESULT_CANCELED, intent)
            }

            override fun onResponse(call: Call<LoginUserScd>?, response: Response<LoginUserScd>) {

                if(response.isSuccessful) {
                    val receivedUser = response.body() as LoginUserScd
                    val intent = Intent()
                    intent.putExtra("user", Gson().toJson(receivedUser))
                    setResult(RESULT_OK, intent)
                    finish()
                } else {

                    val message = when(response.code()) {
                        401 -> getString(R.string.invalid_credentials)
                        else -> getString(R.string.unknown_error)
                    }
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
