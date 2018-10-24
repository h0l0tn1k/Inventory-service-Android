package android.inventory.siemens.cz.siemensinventory.api.entity

/**
 * Created by Stefan Matta on 04.03.2018.
 */
class LoginUserScd (
        var id: Long,
        var scdId : Long,
        var firstName: String = "",
        var lastName: String = "",
        var email: String = "",
        var superiorFirstName: String = "",
        var superiorLastName: String = "",
        var flagRead: Boolean = false,
        var flagWrite: Boolean = false,
        var flagBorrow: Boolean = false,
        var flagInventory: Boolean = false,
        var flagRevision: Boolean = false,
        var flagAdmin: Boolean = false) {

    private val emptyUserFlag = "-"

    fun getFullName() : String {
        val fullName = "$firstName $lastName"
        if(fullName.isNotBlank()) {
            return fullName
        }
        return emptyUserFlag
    }

    fun getSuperiorFullName(): String {
        return "$superiorFirstName $superiorLastName"
    }

    fun isEmpty() : Boolean {
        return getFullName() == emptyUserFlag
    }

    override fun toString(): String {
        return getFullName()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginUserScd

        if (id != other.id) return false
        if (scdId != other.scdId) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (email != other.email) return false
        if (superiorFirstName != other.superiorFirstName) return false
        if (superiorLastName != other.superiorLastName) return false
        if (flagRead != other.flagRead) return false
        if (flagWrite != other.flagWrite) return false
        if (flagBorrow != other.flagBorrow) return false
        if (flagInventory != other.flagInventory) return false
        if (flagRevision != other.flagRevision) return false
        if (flagAdmin != other.flagAdmin) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + scdId.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + superiorFirstName.hashCode()
        result = 31 * result + superiorLastName.hashCode()
        result = 31 * result + flagRead.hashCode()
        result = 31 * result + flagWrite.hashCode()
        result = 31 * result + flagBorrow.hashCode()
        result = 31 * result + flagInventory.hashCode()
        result = 31 * result + flagRevision.hashCode()
        result = 31 * result + flagAdmin.hashCode()
        result = 31 * result + emptyUserFlag.hashCode()
        return result
    }


}