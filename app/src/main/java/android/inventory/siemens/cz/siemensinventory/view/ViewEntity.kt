package android.inventory.siemens.cz.siemensinventory.view

open class ViewEntity(
        var id: Long,
        var name: String
) {
    override fun toString(): String {
        if(id == -1L) {
            return ""
        }
        return name
    }

    fun isEmpty() : Boolean {
        return id == -1L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ViewEntity

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}