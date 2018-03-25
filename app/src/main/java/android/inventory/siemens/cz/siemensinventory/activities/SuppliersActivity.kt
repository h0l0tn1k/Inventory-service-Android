package android.inventory.siemens.cz.siemensinventory.activities

import android.content.res.Resources
import android.graphics.Color
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
import com.baoyz.swipemenulistview.SwipeMenuListView
import com.baoyz.swipemenulistview.SwipeMenu
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenuCreator
import android.widget.ArrayAdapter
import android.util.TypedValue









class SuppliersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers)

        createSwipeMenu()
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

    private fun createSwipeMenu() {
        val creator = SwipeMenuCreator { menu ->

            val editItem = SwipeMenuItem(applicationContext)
            editItem.background = ColorDrawable(Color.rgb(0x00, 0x66,0xff))
            editItem.width = dipToPixels(60)
            editItem.setIcon(R.drawable.ic_edit_black_24dp)

            val deleteItem = SwipeMenuItem(applicationContext)
            deleteItem.background = ColorDrawable(Color.rgb(0xF9,0x3F, 0x25))
            deleteItem.width =  dipToPixels(60)
            deleteItem.setIcon(R.drawable.ic_delete_black_24dp)

            // add to menu
            menu.addMenuItem(editItem)
            menu.addMenuItem(deleteItem)
        }

        supplier_list_view.setMenuCreator(creator)

        supplier_list_view.setOnMenuItemClickListener({ position, menu, index ->
            when (index) {
                0 -> Toast.makeText(this, "onMenuItemClick: clicked item $index", Toast.LENGTH_LONG).show()
                1 -> Toast.makeText(this, "onMenuItemClick: clicked item $index", Toast.LENGTH_LONG).show()
            }
            // false : close the menu; true : not close the menu
            false
        })
    }

    fun dipToPixels(dipValue: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue.toFloat(), Resources.getSystem().displayMetrics).toInt()
    }
}
