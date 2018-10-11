package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.CompanyOwner
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import android.inventory.siemens.cz.siemensinventory.view.ViewEntity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProjectServiceApi {
    
    @GET("projects/")
    fun getProjects() : Call<List<Project>>

    @GET("projects/{project}")
    fun getProject(@Path("project") projectId: Long) : Call<Project>

    @POST("projects/")
    fun createProject(@Body project: GenericNameEntity?): Call<Project>

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