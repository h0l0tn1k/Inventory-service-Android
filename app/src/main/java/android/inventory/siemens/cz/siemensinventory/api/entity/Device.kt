package android.inventory.siemens.cz.siemensinventory.api.entity

import android.inventory.siemens.cz.siemensinventory.inventory.InventoryRecord

/**
 * Created by Stefan Matta on 04.03.2018.
 */
class Device(
        var id: Long,
        var serialNumber: String = "",
        var barcodeNumber: String = "",
        var deviceType: DeviceType? = null,
        var department: Department? = null,
        var calibration: DeviceCalibration? = null,
        var revision: DeviceElectricRevision? = null,
        var holder: LoginUserScd? = null,
        var owner: LoginUserScd?= null,
        var project: Project? = null,
        var companyOwner: CompanyOwner? = null,
        var comment: String = "",
        var nstValue: String = "",
        var defaultLocation: String = "",
        var addDateString: String = "",
        var deviceState: DeviceState? = null,
        var lastRevisionDateString: String = "",
        var inventoryRecord: InventoryRecord? = null,
        var inventoryNumber: String? = null) {

    fun getHolderFullName(): String {
        if (holder == null) {
            return "-"
        }
        return holder?.getFullName() as String
    }

    fun getOwnerFullName(): String {
        if (owner == null) {
            return "-"
        }
        return owner?.getFullName() as String
    }

    fun getInventoryNumberString(): String {
        if (inventoryNumber == null) {
            return ""
        }
        return inventoryNumber as String
    }

    fun getDepartmentName(): String {
        if (department == null) {
            return "-"
        }
        return department?.name.toString()
    }

    fun getCompanyOwnerName(): String {
        if (companyOwner == null) {
            return "-"
        }
        return companyOwner?.name.toString()
    }

    fun getProjectName(): String {
        if (project == null) {
            return "-"
        }
        return project?.name.toString()
    }
}