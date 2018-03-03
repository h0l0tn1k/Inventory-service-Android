package android.inventory.siemens.cz.siemensinventory

import android.inventory.siemens.cz.siemensinventory.entity.GenericEntity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_company_owners.*

class CompanyOwnersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_owners)

        val companyOwners = listOf(
            GenericEntity(1L, "Peter"),
            GenericEntity(2L, "Peter"),
            GenericEntity(3L, "Florian"),
            GenericEntity(4L, "Adaam"),
            GenericEntity(5L, "Petra"),
            GenericEntity(6L, "Lenka"),
            GenericEntity(7L, "Tomas NANA")
        )


        company_owners_list_view.adapter = CompanyOwnersAdapter(this, companyOwners)
    }
}
