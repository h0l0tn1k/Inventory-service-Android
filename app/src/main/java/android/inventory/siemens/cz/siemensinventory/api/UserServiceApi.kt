package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path



/**
 * Created by I333206 on 04.03.2018.
 */
interface UserServiceApi {
    @GET("users/")
    fun getUsers() : Call<List<User>>

    @GET("users/{user}")
    fun getUser(@Path("user") userId: Long) : Call<User>

}