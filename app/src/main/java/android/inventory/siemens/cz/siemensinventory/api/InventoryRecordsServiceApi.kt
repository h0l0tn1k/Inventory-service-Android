package android.inventory.siemens.cz.siemensinventory.api

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.inventory.InventoryRecord
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface InventoryRecordsServiceApi {

    @PUT("inventory-records/{id}")
    fun updateInventoryRecord(@Path("id") inventoryRecordId: Long, @Body inventoryRecord: InventoryRecord) : Call<InventoryRecord>

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