package android.inventory.siemens.cz.siemensinventory.api.entity


class LoginUserScd (
        var firstName: String,
        var lastName: String,
        var email: String,
        var superiorName: String,
        var flagRead: Boolean,
        var flagWrite: Boolean,
        var flagBorrow: Boolean,
        var flagInventory: Boolean,
        var flagRevision: Boolean,
        var flagAdmin: Boolean) {

    fun getFullName() : String {
        return "$firstName $lastName"
    }
}