package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.Project
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ProjectServiceApi {
    
    @GET("projects/")
    fun getProjects() : Call<List<Project>>

    @GET("projects/{project}")
    fun getProject(@Path("project") projectId: Long) : Call<Project>

    object Factory {
        fun create(): ProjectServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl())
                    .build()
                    .create<ProjectServiceApi>(ProjectServiceApi::class.java)
        }
    }
}