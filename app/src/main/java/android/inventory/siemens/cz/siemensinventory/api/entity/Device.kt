package android.inventory.siemens.cz.siemensinventory.api.entity

import java.util.*

/**
 * Created by I333206 on 04.03.2018.
 */
class Device(
        var id: Long,
        var serialNumber: String,
        var barcodeNumber: String,
        var objectTypeName: String,
        var objectTypeVersion: String,
        var ownerName: String,
        var departmentName: String,
        var holderName: String,
        var projectName: String,
        var companyOwnerName: String,
        var addDate: Date,
        var deviceStateName: String,
        var lastRevisionDateString: Date) {
}