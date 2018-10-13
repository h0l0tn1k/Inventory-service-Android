package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import android.inventory.siemens.cz.siemensinventory.view.ViewEntity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ProjectServiceApi {
    
    @GET("projects/")
    fun getProjects() : Call<List<Project>>

    @GET("projects/{project}")
    fun getProject(@Path("project") projectId: Long) : Call<Project>

    @POST("projects/")
    fun createProject(@Body project: GenericNameEntity?): Call<Project>

    @PUT("projects/{projectId}")
    fun updateProject(@Path("projectId") projectId: Long, @Body project: ViewEntity?): Call<Project>

    @DELETE("projects/{projectId}")
    fun deleteProject(@Path("projectId") projectId: Long) : Call<Void>

    object Factory {
        fun create(context : Context): ProjectServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<ProjectServiceApi>(ProjectServiceApi::class.java)
        }
    }
}