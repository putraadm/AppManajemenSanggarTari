package zhafran.putra.appproject2ckel3.model

data class Report(
    val idReport: Int,
    val idUser: Int,
    val tanggal: String,
    val wirama: String,
    val deskripsiWirama: String,
    val wirasa: String,
    val deskripsiWirasa: String,
    val wiraga: String,
    val deskripsiWiraga: String,
    val catatan: String,
    val semester: String
)
