package android.inventory.siemens.cz.siemensinventory.borrow

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.api.SiemensServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.BorrowReturn
import android.inventory.siemens.cz.siemensinventory.api.entity.GenericNameEntity
import android.inventory.siemens.cz.siemensinventory.api.entity.Supplier
import android.inventory.siemens.cz.siemensinventory.view.ViewEntity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface BorrowServiceApi {

    @POST("borrow/")
    fun createBorrowReturn(@Body borrowReturn: BorrowReturn): Call<Void>
}