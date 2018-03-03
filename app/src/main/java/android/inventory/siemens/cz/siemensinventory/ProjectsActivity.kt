package android.inventory.siemens.cz.siemensinventory

import android.inventory.siemens.cz.siemensinventory.entity.GenericEntity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_projects.*

class ProjectsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        val projects = listOf(
                GenericEntity(1L, "RoboLAB"),
                GenericEntity(2L, "SageLab"),
                GenericEntity(3L, "CVUT Navigator")
        )

        project_list_view.adapter = ProjectsAdapter(this, projects)
    }
}
