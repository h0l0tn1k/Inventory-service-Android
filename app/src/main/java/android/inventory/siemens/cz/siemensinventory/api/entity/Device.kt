package android.inventory.siemens.cz.siemensinventory.api.entity

import android.inventory.siemens.cz.siemensinventory.inventory.InventoryResult

/**
 * Created by Stefan Matta on 04.03.2018.
 */
class Device(
        var id: Long,
        var serialNumber: String,
        var barcodeNumber: String,
        var objectTypeName: String,
        var objectTypeVersion: String,
        var departmentName: String,
        var holderName: String,
        var holder : LoginUserScd?,
        var projectName: String,
        var companyOwnerName: String,
        //var addDate: Date,
        var deviceStateName: String,
        var lastRevisionDateString: String,
        var inventoryRecord : InventoryResult,
        var typeAndVersionName: String) {
}