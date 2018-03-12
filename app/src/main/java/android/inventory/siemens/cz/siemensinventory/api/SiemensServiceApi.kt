package android.inventory.siemens.cz.siemensinventory.api

class SiemensServiceApi {

    //http://10.182.36.56:8080/rest/ - work
    companion object {
        //configurable from App Settings
        fun getBaseUrl(): String = "http://10.182.36.38:8080/rest/" //"http://192.168.137.1:8080/rest/"
    }
}