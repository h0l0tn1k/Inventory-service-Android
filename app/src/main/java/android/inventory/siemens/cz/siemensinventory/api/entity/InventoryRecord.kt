package android.inventory.siemens.cz.siemensinventory.api.entity

class InventoryRecord(
        val id : Long,
        var registered : Boolean,
        val deviceInventory : Device
) {
}