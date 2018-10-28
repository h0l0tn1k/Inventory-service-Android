package android.inventory.siemens.cz.siemensinventory.login

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.SiemensServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface LoginServiceApi {

    //TODO: CHANGE!!

    @GET("login/{email}/{password}")
    fun login(@Path("email") email: String,@Path("password") password: String) : Call<LoginUserScd>

    object Factory {
        fun create(context : Context): LoginServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<LoginServiceApi>(LoginServiceApi::class.java)
        }
    }
}