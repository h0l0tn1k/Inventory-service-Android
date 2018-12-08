package android.inventory.siemens.cz.siemensinventory.login

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginServiceApi {

    @FormUrlEncoded
    @POST("/oauth/token")
    fun getNewAccessToken(@Header("authorization") auth: String,
                          @Field("username") username: String,
                          @Field("password") password: String,
                          @Field("grant_type") grantType: String = "password",
                          @Field("scope") scope: String = ""): Call<AccessToken>
}