package android.inventory.siemens.cz.siemensinventory.api.entity

class DeviceType(
        var id: Long,
        var objectTypeName: String,
        var classification: String,
        var manufacturer: String,
        var orderNumber: String,
        var version: String,
        var supplier: Supplier,
        var price: Double
) {

    fun getDeviceTypeAndVersion(): String {
        return "$objectTypeName, Version: $version"
    }
}