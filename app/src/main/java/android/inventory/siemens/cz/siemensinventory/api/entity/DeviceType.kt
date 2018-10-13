package android.inventory.siemens.cz.siemensinventory.api.entity

class DeviceType(
        var id: Long?,
        var objectTypeName: String,
        var classification: String,
        var manufacturer: String,
        var orderNumber: String,
        var version: String,
        var supplier: Supplier?,
        var price: Double
) {

    constructor() : this(null, "", "", "", "", "", null, 0.0)

    fun getDeviceTypeAndVersion(): String {
        return "$objectTypeName, Version: $version"
    }

    fun getPriceString() : String {
        return price.toString()
    }

    override fun toString(): String {
        return getDeviceTypeAndVersion()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceType

        if (id != other.id) return false
        if (objectTypeName != other.objectTypeName) return false
        if (classification != other.classification) return false
        if (manufacturer != other.manufacturer) return false
        if (orderNumber != other.orderNumber) return false
        if (version != other.version) return false
        if (supplier != other.supplier) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + objectTypeName.hashCode()
        result = 31 * result + classification.hashCode()
        result = 31 * result + manufacturer.hashCode()
        result = 31 * result + orderNumber.hashCode()
        result = 31 * result + version.hashCode()
        result = 31 * result + (supplier?.hashCode() ?: 0)
        result = 31 * result + price.hashCode()
        return result
    }


}