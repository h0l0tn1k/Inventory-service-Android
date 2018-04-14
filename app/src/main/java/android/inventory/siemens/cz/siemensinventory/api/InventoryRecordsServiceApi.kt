package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.InventoryRecord
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface InventoryRecordsServiceApi {

    @GET("inventoryrecords/checked")
    fun getCheckedDevices() : Call<List<InventoryRecord>>

    @GET("inventoryrecords/unchecked")
    fun getUnCheckedDevices() : Call<List<InventoryRecord>>

    @GET("inventoryrecords/{id}")
    fun getInvetoryRecord(@Path("id") inventoryRecordId: Long) : Call<InventoryRecord>

    @PUT("inventoryrecords/{id}/{checked}")
    fun updateCheckedValue(@Path("id") inventoryRecordId: Long, @Path("checked") checked: Boolean)

    object Factory {
        fun create(context : Context): InventoryRecordsServiceApi {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SiemensServiceApi.getBaseUrl(context))
                    .build()
                    .create<InventoryRecordsServiceApi>(InventoryRecordsServiceApi::class.java)
        }
    }
}