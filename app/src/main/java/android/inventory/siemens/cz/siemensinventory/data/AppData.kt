package android.inventory.siemens.cz.siemensinventory.data

import android.app.Application
import android.inventory.siemens.cz.siemensinventory.api.entity.DeviceState
import android.inventory.siemens.cz.siemensinventory.api.entity.LoginUserScd
import android.inventory.siemens.cz.siemensinventory.login.AccessToken

class AppData : Application() {

    companion object {
        var loginUserScd : LoginUserScd? = null
        var deviceStates: List<DeviceState>? = null
        var accessToken: AccessToken? = null
    }

}