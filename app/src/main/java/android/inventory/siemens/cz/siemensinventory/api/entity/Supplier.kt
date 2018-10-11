package android.inventory.siemens.cz.siemensinventory.api.entity

import android.inventory.siemens.cz.siemensinventory.view.ViewEntity

/**
 * Created by Stefan Matta on 04.03.2018.
 */
class Supplier(id: Long, name: String)
    : ViewEntity(id, name) {

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if(other == null || other !is Supplier) return false
        return id == other.id && name == other.name
    }

    override fun hashCode(): Int {
        return (id?.toInt() * 31 + name.hashCode()) * 31
    }
}