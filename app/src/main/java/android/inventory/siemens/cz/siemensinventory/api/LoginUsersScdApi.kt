package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import retrofit2.Call
import retrofit2.http.GET

interface LoginUsersScdApi {

    @GET("users/")
    fun getUsers(): Call<List<LoginUserScd>>

    @GET("users/me")
    fun getCurrentUser(): Call<LoginUserScd>
}