package android.inventory.siemens.cz.siemensinventory.api.entity

class DeviceCalibration(
        var id: Long,
        var calibrationInterval: Int,
        var lastCalibrationDateString: String
) {
}