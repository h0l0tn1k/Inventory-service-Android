package android.inventory.siemens.cz.siemensinventory

import android.inventory.siemens.cz.siemensinventory.api.CompanyOwnerServiceApi
import android.inventory.siemens.cz.siemensinventory.api.ProjectServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.CompanyOwner
import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import android.inventory.siemens.cz.siemensinventory.entity.GenericEntity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_company_owners.*
import kotlinx.android.synthetic.main.activity_projects.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyOwnersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_owners)

        val companyOwnersCall = CompanyOwnerServiceApi.Factory.create().getCompanyOwners()

        companyOwnersCall.enqueue(object : Callback<List<CompanyOwner>> {
            override fun onResponse(call: Call<List<CompanyOwner>>?, response: Response<List<CompanyOwner>>?) {
                val companies = response?.body()
                if(companies != null) {
                    company_owners_list_view.adapter = CompanyOwnersAdapter(this@CompanyOwnersActivity, companies)
                }
            }

            override fun onFailure(call: Call<List<CompanyOwner>>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }
}
