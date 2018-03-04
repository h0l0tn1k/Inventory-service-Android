package android.inventory.siemens.cz.siemensinventory.api

class SiemensServiceApi {

    //http://10.182.36.56:8080/rest/ - work
    companion object {
        fun getBaseUrl(): String = "http://192.168.137.1:8080/rest/"
    }

/*
    object Factory {
        fun getRetrofit() : Retrofit {
            return Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl())
                    .build()
        }
    }*/
}