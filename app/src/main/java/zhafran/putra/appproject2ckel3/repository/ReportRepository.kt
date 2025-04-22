package zhafran.putra.appproject2ckel3.repository

import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import zhafran.putra.appproject2ckel3.database.DBOpenHelper

data class ReportEntity(
    val idReport: Int,
    val idUser: Int,
    val tanggal: String,
    val wirama: String,
    val wirasa: String,
    val wiraga: String,
    val catatan: String
)

class ReportRepository(context: Context) {

    private val dbHelper = DBOpenHelper(context)

    fun insertReport(idUser: Int, tanggal: String, wirama: String, wirasa: String, wiraga: String, catatan: String): Long {
        return dbHelper.insertReport(idUser, tanggal, wirama, wirasa, wiraga, catatan)
    }

    fun updateReport(idReport: Int, idUser: Int, tanggal: String, wirama: String, wirasa: String, wiraga: String, catatan: String): Int {
        return dbHelper.updateReport(idReport, idUser, tanggal, wirama, wirasa, wiraga, catatan)
    }

    fun deleteReport(idReport: Int): Int {
        return dbHelper.deleteReport(idReport)
    }

    fun getReportById(idReport: Int): ReportEntity? {
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM report WHERE id_report = ?", arrayOf(idReport.toString()))
        var report: ReportEntity? = null
        if (cursor.moveToFirst()) {
            val idUser = cursor.getInt(cursor.getColumnIndexOrThrow("id_user"))
            val tanggal = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"))
            val wirama = cursor.getString(cursor.getColumnIndexOrThrow("wirama"))
            val wirasa = cursor.getString(cursor.getColumnIndexOrThrow("wirasa"))
            val wiraga = cursor.getString(cursor.getColumnIndexOrThrow("wiraga"))
            val catatan = cursor.getString(cursor.getColumnIndexOrThrow("catatan"))
            report = ReportEntity(idReport, idUser, tanggal, wirama, wirasa, wiraga, catatan)
        }
        cursor.close()
        return report
    }

    fun getAllReport(): LiveData<List<ReportEntity>> {
        val liveData = MutableLiveData<List<ReportEntity>>()
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM report", null)
        val reportList = mutableListOf<ReportEntity>()
        if (cursor.moveToFirst()) {
            do {
                val idReport = cursor.getInt(cursor.getColumnIndexOrThrow("id_report"))
                val idUser = cursor.getInt(cursor.getColumnIndexOrThrow("id_user"))
                val tanggal = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"))
                val wirama = cursor.getString(cursor.getColumnIndexOrThrow("wirama"))
                val wirasa = cursor.getString(cursor.getColumnIndexOrThrow("wirasa"))
                val wiraga = cursor.getString(cursor.getColumnIndexOrThrow("wiraga"))
                val catatan = cursor.getString(cursor.getColumnIndexOrThrow("catatan"))
                val report = ReportEntity(idReport, idUser, tanggal, wirama, wirasa, wiraga, catatan)
                reportList.add(report)
            } while (cursor.moveToNext())
        }
        cursor.close()
        liveData.postValue(reportList)
        return liveData
    }
}
