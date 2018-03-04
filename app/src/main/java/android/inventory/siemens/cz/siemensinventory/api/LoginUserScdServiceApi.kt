package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by I333206 on 04.03.2018.
 */
interface LoginUserScdServiceApi {

    @GET("loginusers/{email}/{password}")
    fun login(@Path("email") email: String,@Path("password") password: String) : Call<LoginUserScd>

}