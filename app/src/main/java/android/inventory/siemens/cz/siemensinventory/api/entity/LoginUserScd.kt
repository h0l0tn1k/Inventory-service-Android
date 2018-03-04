package android.inventory.siemens.cz.siemensinventory.api.entity


class LoginUserScd (
        var firstName: String,
        var lastName: String,
        var email: String) {

    fun getFullName() : String {
        return "$firstName $lastName"
    }
}