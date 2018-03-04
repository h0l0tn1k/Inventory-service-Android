package android.inventory.siemens.cz.siemensinventory

import android.app.Activity
import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.api.LoginUserScdServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {

    private var loggedInUserName: String = ""
    private var loggedInEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login?.setOnClickListener {
            // Finish the registration screen and return to the Login activity

            val retrofit = Retrofit.Builder().baseUrl("http://10.182.36.56:8080/rest/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
            val service = retrofit.create(LoginUserScdServiceApi::class.java)
            val user = service.login(login_email?.text.toString(), login_password?.text.toString())

            user.enqueue(object: Callback<LoginUserScd> {
                override fun onFailure(call: Call<LoginUserScd>?, t: Throwable?) {
                    Toast.makeText(
                            this@LoginActivity, "ERROR", Toast.LENGTH_LONG
                    ).show()
                    setResult(Activity.RESULT_CANCELED, intent)
                    finish()
                }

                override fun onResponse(call: Call<LoginUserScd>?, response: Response<LoginUserScd>?) {
                    val receivedUser = response?.body()
                    //TODO: UGLY
                    Toast.makeText(
                            this@LoginActivity, "Ahoj " + receivedUser?.firstName + " " + receivedUser?.lastName, Toast.LENGTH_LONG
                    ).show()

                    val intent = Intent()
                    intent.putExtra("user", Gson().toJson(response?.body()))
                    setResult(RESULT_OK, intent)
                    finish()
                }
            })
        }
    }
}
