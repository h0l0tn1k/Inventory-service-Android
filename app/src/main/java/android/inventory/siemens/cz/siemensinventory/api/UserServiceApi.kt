package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface UserServiceApi {

    @GET("users/")
    fun getUsers() : Call<List<User>>

    @GET("users/{user}")
    fun getUser(@Path("user") userId: Long) : Call<User>

    object Factory {
        fun create(context : Context): UserServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<UserServiceApi>(UserServiceApi::class.java)
        }
    }
}