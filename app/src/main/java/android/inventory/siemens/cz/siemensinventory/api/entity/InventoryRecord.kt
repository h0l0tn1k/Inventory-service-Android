package android.inventory.siemens.cz.siemensinventory.api.entity

class InventoryRecord(
        val id : Long,
        val registered : Boolean,
        val deviceInventory : Device
) {
}