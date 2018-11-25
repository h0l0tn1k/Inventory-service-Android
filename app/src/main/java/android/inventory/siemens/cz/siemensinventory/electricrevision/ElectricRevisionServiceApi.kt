package android.inventory.siemens.cz.siemensinventory.electricrevision

import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceElectricRevision
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface ElectricRevisionServiceApi {

    @PUT("electric-revisions/{revisionId}")
    fun updateRevision(@Path("revisionId") revisionId: Long?, @Body revision: DeviceElectricRevision?): Call<DeviceElectricRevision>
}