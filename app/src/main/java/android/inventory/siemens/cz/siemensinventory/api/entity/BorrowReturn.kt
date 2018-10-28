package android.inventory.siemens.cz.siemensinventory.api.entity

class BorrowReturn(
        val device: Device?,
        val newHolder: LoginUserScd?,
        val comment: String
)