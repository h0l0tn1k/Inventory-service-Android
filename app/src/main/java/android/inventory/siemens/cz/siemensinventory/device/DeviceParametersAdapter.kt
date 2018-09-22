package android.inventory.siemens.cz.siemensinventory.device

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.entity.KeyValueParameters
import android.inventory.siemens.cz.siemensinventory.entity.Permission
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Stefan Matta on 06-Mar-18.
 */
class DeviceParametersAdapter(
        private val context: Context,
        private var device : Device
) : BaseAdapter() {

    private var parameters: List<KeyValueParameters> = getParameters()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val parameter = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.device_params_item, parent, false)
        }

        val parameterNameView = view?.findViewById(R.id.parameterName) as TextView
        parameterNameView.text = parameter.key

        //TODO: read only mode
        val parameterValueReadonly = view.findViewById(R.id.parameterValueReadonly) as TextView
        parameterValueReadonly.text = parameter.value

        //TODO: if edit mode
//        val parameterValueView = view?.findViewById(R.id.parameterValueEdit) as EditText
//        parameterValueView.setText(parameter.value)

        return view
    }

    override fun getItem(position: Int): KeyValueParameters = parameters[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = parameters.size

    private fun getParameters() : List<KeyValueParameters> {
        return listOf(
                KeyValueParameters("Device Type", device.deviceType.getDeviceTypeAndVersion()),
                KeyValueParameters("Serial Number", device.serialNumber),
                KeyValueParameters("Department", device.department.name),
                KeyValueParameters("Current Holder", device.getHolderFullName()),
                KeyValueParameters("Owner", device.getOwnerFullName()),
                KeyValueParameters("Project", device.project.name),
                KeyValueParameters("Default Location", device.defaultLocation),
                KeyValueParameters("Company Owner", device.companyOwner.name),
                //KeyValueParameters("Add Date", addDateString),
                KeyValueParameters("Status", device.deviceState.name),
//                KeyValueParameters("Last Revision Date", device.lastRevisionDateString),
                KeyValueParameters("Barcode Number", device.barcodeNumber),
                KeyValueParameters("Comment", device.comment)
        )
    }
}