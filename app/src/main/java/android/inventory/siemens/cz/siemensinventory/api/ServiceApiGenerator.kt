package android.inventory.siemens.cz.siemensinventory.api

import android.app.Activity
import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.login.AccessToken
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceApiGenerator {
    object Factory {

        fun <T> createService(classType: Class<T>, context: Context): T {
            val httpClient = OkHttpClient.Builder()
            val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))

            val client = httpClient.build()
            val retrofit = retrofitBuilder.client(client).build()
            return retrofit.create(classType)

        }

        fun <T> createService(classType: Class<T>, accessToken: AccessToken?, context: Context): T {
            val httpClient = OkHttpClient.Builder()

            if (accessToken != null) {
                httpClient.addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val requestBuilder = originalRequest.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-type", "application/json")
                            .header("Authorization", accessToken.getTokenType() + " " + accessToken.access_token)
                            .method(originalRequest.method(), originalRequest.body())

                    val response = chain.proceed(requestBuilder.build())
                    (context as Activity).runOnUiThread {
                        if (!response.isSuccessful) {
                            val message = when (response.code()) {
                                401 -> context.getString(R.string.unauthorized_to_perform_this_action)
                                403 -> context.getString(R.string.access_denied)
                                else -> {
                                    context.getString(R.string.unknown_error)
                                }
                            }
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                    response
                }
            }

            return Retrofit.Builder().client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getServiceUrl(context)).build().create(classType)
        }
    }
}