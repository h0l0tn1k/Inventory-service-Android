package android.inventory.siemens.cz.siemensinventory.inventory

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface InventoryRecordsServiceApi {

    @PUT("inventory-records/{id}")
    fun updateInventoryRecord(@Path("id") inventoryRecordId: Long, @Body inventoryRecord: InventoryRecord): Call<InventoryRecord>
}