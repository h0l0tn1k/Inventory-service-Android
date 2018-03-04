package android.inventory.siemens.cz.siemensinventory.api.entity

/**
 * Created by I333206 on 04.03.2018.
 */
class LoginUserScd (
        var firstName: String,
        var lastName: String,
        var email: String) {

    fun getFullName() : String {
        return "$firstName $lastName"
    }
}