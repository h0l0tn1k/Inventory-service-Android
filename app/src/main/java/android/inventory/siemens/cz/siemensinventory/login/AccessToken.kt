package android.inventory.siemens.cz.siemensinventory.login

class AccessToken(
        var access_token: String,
        var expires_in: Int,
        var refresh_token: String,
        var scope: String,
        var client_id: String,
        var client_secret: String
) {
    fun getTokenType(): String {
        return "Bearer"
    }
}