package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface LoginServiceApi {

    @GET("login/{email}/{password}")
    fun login(@Path("email") email: String,@Path("password") password: String) : Call<LoginUserScd>

    object Factory {
        fun create(): LoginServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl())
                    .build()
                    .create<LoginServiceApi>(LoginServiceApi::class.java)
        }
    }
}