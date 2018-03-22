package android.inventory.siemens.cz.siemensinventory.activities

import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.adapters.SupplierAdapter
import android.inventory.siemens.cz.siemensinventory.api.SupplierServiceApi
import android.inventory.siemens.cz.siemensinventory.api.entity.Supplier
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_suppliers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuppliersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers)


        val suppliersApi = SupplierServiceApi.Factory.create(this).getSuppliers()

        suppliersApi.enqueue(object : Callback<List<Supplier>> {
            override fun onResponse(call: Call<List<Supplier>>?, response: Response<List<Supplier>>?) {
                val suppliers = response?.body()
                if(suppliers != null) {
                    supplier_list_view.adapter = SupplierAdapter(this@SuppliersActivity, suppliers)
                }
            }

            override fun onFailure(call: Call<List<Supplier>>?, t: Throwable?) {
                Toast.makeText(this@SuppliersActivity, getText(R.string.error_cannot_connect_to_service), Toast.LENGTH_LONG).show()
            }
        })
    }
}
