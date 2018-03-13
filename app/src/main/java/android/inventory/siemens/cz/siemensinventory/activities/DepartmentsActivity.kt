package android.inventory.siemens.cz.siemensinventory.activities

import android.inventory.siemens.cz.siemensinventory.adapters.DepartmentAdapter
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.DepartmentsServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Department
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_departments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepartmentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_departments)

        val departments = DepartmentsServiceApi.Factory.create(this).getDepartments()

        departments.enqueue(object : Callback<List<Department>> {
            override fun onResponse(call: Call<List<Department>>?, response: Response<List<Department>>?) {
                val departments = response?.body()
                if(departments != null) {
                    department_list_view.adapter = DepartmentAdapter(this@DepartmentsActivity, departments)
                }
            }

            override fun onFailure(call: Call<List<Department>>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }
}
