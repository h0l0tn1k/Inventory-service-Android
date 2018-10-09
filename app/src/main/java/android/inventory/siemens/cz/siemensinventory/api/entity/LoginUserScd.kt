package android.inventory.siemens.cz.siemensinventory.api.entity

/**
 * Created by Stefan Matta on 04.03.2018.
 */
class LoginUserScd (
        var id: Long,
        var scdId : Long,
        var firstName: String,
        var lastName: String,
        var email: String,
        var superiorFirstName: String,
        var superiorLastName: String,
        var flagRead: Boolean,
        var flagWrite: Boolean,
        var flagBorrow: Boolean,
        var flagInventory: Boolean,
        var flagRevision: Boolean,
        var flagAdmin: Boolean) {

    fun getFullName() : String {
        val fullName = "$firstName $lastName"
        if(fullName.isNotBlank()) {
            return fullName
        }
        return "-"
    }

    fun getSuperiorFullName(): String {
        return "$superiorFirstName $superiorLastName"
    }
}