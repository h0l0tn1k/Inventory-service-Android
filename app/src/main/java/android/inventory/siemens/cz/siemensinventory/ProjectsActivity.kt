package android.inventory.siemens.cz.siemensinventory

import android.inventory.siemens.cz.siemensinventory.api.ProjectServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import android.inventory.siemens.cz.siemensinventory.entity.GenericEntity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_projects.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        val projects = ProjectServiceApi.Factory.create().getProjects()

        projects.enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>?, response: Response<List<Project>>?) {
                val projs = response?.body()
                if(projs != null) {
                    project_list_view.adapter = ProjectsAdapter(this@ProjectsActivity, projs)
                }
            }

            override fun onFailure(call: Call<List<Project>>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }
}
