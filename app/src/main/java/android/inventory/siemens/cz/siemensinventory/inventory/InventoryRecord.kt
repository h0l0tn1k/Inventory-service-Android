package android.inventory.siemens.cz.siemensinventory.inventory

import android.inventory.siemens.cz.siemensinventory.api.entity.InventoryState

class InventoryRecord(
    val id : Long,
    var inventoryState : InventoryState,
    var comment: String?
) {
}