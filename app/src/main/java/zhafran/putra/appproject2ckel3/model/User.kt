package zhafran.putra.appproject2ckel3.model

data class User(
    val idUser: Int,
    val namaLengkap: String,
    val username: String,
    val email: String,
    val password: String,
    val idJadwalKelas: Int,
    val role: String
)
