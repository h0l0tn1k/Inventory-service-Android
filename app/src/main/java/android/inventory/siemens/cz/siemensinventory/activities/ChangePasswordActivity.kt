package android.inventory.siemens.cz.siemensinventory.activities

import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.tools.TextViewHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        changePasswordBtn.setOnClickListener { changePassword() }
    }

    private fun changePassword() {
        val helper = TextViewHelper().withContext(this)

        if (helper.isNotEmpty(curr_password)
                .and.isNotEmpty(new_password1)
                .and.isNotEmpty(new_password2).isValid) {

            //TODO: add - check curr_password equals

            //check new passwords equal
            if(new_password1.text.toString() != new_password2.text.toString()) {
                helper.setErrorAndShake(new_password2, getString(R.string.new_pswd_not_equal))
            } else {
                if(curr_password.text.toString() == new_password1.text.toString()) {
                    helper.setErrorAndShake(new_password1, getString(R.string.curr_new_pswds_same))
                } else {
                    //TODO: change password
                    //call service
                }
            }
        }
    }
}
