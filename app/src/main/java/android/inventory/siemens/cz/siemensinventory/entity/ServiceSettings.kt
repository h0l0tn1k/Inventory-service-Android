package android.inventory.siemens.cz.siemensinventory.entity

import android.content.Context
import android.content.SharedPreferences
import android.inventory.siemens.cz.siemensinventory.adapters.SupplierAdapter
import android.inventory.siemens.cz.siemensinventory.api.SiemensServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Supplier
import android.preference.PreferenceManager
import android.util.Patterns
import android.webkit.URLUtil
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_suppliers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by Stefan Matta on 13-Mar-18.
 */
class ServiceSettings(context: Context) {

    var preferenceManager : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    var preferenceEditor : SharedPreferences.Editor = preferenceManager.edit()

    var baseUrl : String
        get() = preferenceManager.getString("baseUrl", "")
        set(value) {
            preferenceEditor.putString("baseUrl", value).commit()
        }

    var port : Int
        get() = Integer.parseInt(preferenceManager.getString("port", "0"))
        set(value) {
            preferenceEditor.putInt("port", value).commit()
        }

    var httpMethod: String
        get() = preferenceManager.getString("httpMethod", "")
        set(value) {
            preferenceEditor.putString("httpMethod", value).commit()
        }
    var path: String
        get() = preferenceManager.getString("path", "")
        set(value) {
            preferenceEditor.putString("path", value).commit()
        }

    var serviceUrl: String
        get() = preferenceManager.getString("serviceUrl", "")
        set(value) {
            preferenceEditor.putString("serviceUrl", getServiceUrlFormatted()).commit()
        }

    fun getServiceUrlFormatted() : String {
        return "$httpMethod://$baseUrl:$port$path/"
    }

    fun checkConnection(): Call<Void> {
        return Retrofit.Builder().baseUrl(getServiceUrlFormatted()).build().create(SiemensServiceApi::class.java).getService()
    }

    fun isUrlWellFormated(): Boolean {
        return Patterns.WEB_URL.matcher(getServiceUrlFormatted()).matches()
    }
}