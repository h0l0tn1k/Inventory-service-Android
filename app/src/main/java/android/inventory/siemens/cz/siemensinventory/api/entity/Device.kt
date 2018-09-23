package android.inventory.siemens.cz.siemensinventory.api.entity

import android.inventory.siemens.cz.siemensinventory.inventory.InventoryRecord

/**
 * Created by Stefan Matta on 04.03.2018.
 */
class Device(
        var id: Long,
        var serialNumber: String,
        var barcodeNumber: String,
        var deviceType: DeviceType,
        var department: Department,
        var calibration: DeviceCalibration,
        var revision: DeviceElectricRevision,
        var holder: LoginUserScd?,
        var owner: LoginUserScd?,
        var project: Project,
        var companyOwner: CompanyOwner,
        var comment: String,
        var nstValue: String,
        var defaultLocation: String,
        var addDateString: String,
        var deviceState: DeviceState,
        var lastRevisionDateString: String,
        var inventoryRecord: InventoryRecord,
        var inventoryNumber: String?) {

    fun getHolderFullName(): String {
        if (holder == null) {
            return "-"
        }
        return holder?.getFullName() as String
    }

    fun getOwnerFullName(): String {
        if (owner == null) {
            return "-"
        }
        return owner?.getFullName() as String
    }

    fun getInventoryNumberString(): String {
        if (inventoryNumber == null) {
            return ""
        }
        return inventoryNumber as String
    }
}