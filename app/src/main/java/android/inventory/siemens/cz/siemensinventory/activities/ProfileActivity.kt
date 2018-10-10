package android.inventory.siemens.cz.siemensinventory.activities

import android.content.Intent
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.adapters.PermissionsAdapter
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.inventory.siemens.cz.siemensinventory.data.AppData
import android.inventory.siemens.cz.siemensinventory.entity.Permission
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        profileChangePasswordBtn.setOnClickListener { startChangePasswordActivity() }

        setUserDetails()
    }

    private fun getUser(): LoginUserScd? {
        return AppData.loginUserScd
    }

    private fun setUserDetails() {

        val permissions = listOf(
                Permission("Read-only", getUser()?.flagRead),
                Permission("Edit", getUser()?.flagWrite),
                Permission("Borrowing", getUser()?.flagBorrow),
                Permission("Inventory-making", getUser()?.flagInventory),
                Permission("Revision-making", getUser()?.flagRevision),
                Permission("Admin", getUser()?.flagAdmin)
        )

        permissionsView.adapter = PermissionsAdapter(this, permissions)
    }

    private fun startChangePasswordActivity() {
        startActivity(Intent(this, ChangePasswordActivity::class.java))
    }
}
