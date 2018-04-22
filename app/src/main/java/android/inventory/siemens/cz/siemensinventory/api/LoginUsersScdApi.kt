package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface LoginUsersScdApi {

    @GET("loginusers/")
    fun getUsers() : Call<List<LoginUserScd>>

    object Factory {
        fun create(context : Context): LoginUsersScdApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<LoginUsersScdApi>(LoginUsersScdApi::class.java)
        }
    }
}