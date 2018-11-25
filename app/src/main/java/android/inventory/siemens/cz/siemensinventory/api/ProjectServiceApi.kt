package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import android.inventory.siemens.cz.siemensinventory.view.ViewEntity
import retrofit2.Call
import retrofit2.http.*

interface ProjectServiceApi {

    @GET("projects/")
    fun getProjects(): Call<List<Project>>

    @POST("projects/")
    fun createProject(@Body project: GenericNameEntity?): Call<Project>

    @PUT("projects/{projectId}")
    fun updateProject(@Path("projectId") projectId: Long, @Body project: ViewEntity?): Call<Project>

    @DELETE("projects/{projectId}")
    fun deleteProject(@Path("projectId") projectId: Long): Call<Void>
}