package android.inventory.siemens.cz.siemensinventory.api.entity

/**
 * Created by I333206 on 04.03.2018.
 */
class Device(
        var id: Long,
        var serialNumber: String,
        var barcodeNumber: String,
        var objectTypeName: String,
        var objectTypeVersion: String,
        var ownerName: String) {
}