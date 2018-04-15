package android.inventory.siemens.cz.siemensinventory.api

import android.inventory.siemens.cz.siemensinventory.view.ViewEntity
import retrofit2.Call

interface CommonApi {

    fun getItems() : Call<ViewEntity>
}