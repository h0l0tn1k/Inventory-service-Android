package android.inventory.siemens.cz.siemensinventory.activities

import android.inventory.siemens.cz.siemensinventory.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.profile.*

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        changePasswordBtn.setOnClickListener { changePassword() }
    }

    private fun changePassword() {
        TODO("Not implemented yet.")
        //TODO
//        curr_password.text
//        new_password1.text
//        new_password2.text
    }
}
