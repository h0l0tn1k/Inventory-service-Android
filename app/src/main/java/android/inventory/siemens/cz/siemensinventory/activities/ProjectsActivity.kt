package android.inventory.siemens.cz.siemensinventory.activities

import android.inventory.siemens.cz.siemensinventory.adapters.ProjectsAdapter
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.ProjectServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_projects.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        val projects = ProjectServiceApi.Factory.create(this).getProjects()

        projects.enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>?, response: Response<List<Project>>?) {
                val projs = response?.body()
                if(projs != null) {
                    project_list_view.adapter = ProjectsAdapter(this@ProjectsActivity, projs)
                }
            }

            override fun onFailure(call: Call<List<Project>>?, t: Throwable?) {
                Toast.makeText(this@ProjectsActivity, getText(R.string.error_cannot_connect_to_service), Toast.LENGTH_LONG).show()
            }
        })

    }
}
