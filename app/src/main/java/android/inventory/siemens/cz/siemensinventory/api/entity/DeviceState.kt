package android.inventory.siemens.cz.siemensinventory.api.entity

class DeviceState(
        var id: Long,
        var name: String
) {

    override fun toString(): String {
        return name
    }
}