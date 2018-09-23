package android.inventory.siemens.cz.siemensinventory.data

import android.app.Application
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceState
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd

class AppData : Application() {

    companion object {
        var loginUserScd : LoginUserScd? = null
        var deviceStates: List<DeviceState>? = null
    }

}