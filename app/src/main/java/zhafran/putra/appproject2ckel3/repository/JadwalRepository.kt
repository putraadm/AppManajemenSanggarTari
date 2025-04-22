package zhafran.putra.appproject2ckel3.repository

import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import zhafran.putra.appproject2ckel3.database.DBOpenHelper

data class JadwalEntity(
    val id_jadwal: Int,
    val kelas: String,
    val hari: String,
    val jam: String
)

class JadwalRepository(context: Context) {

    private val dbHelper = DBOpenHelper(context)

    fun insertJadwal(kelas: String ,hari: String, jam: String): Long {
        return dbHelper.insertJadwal(kelas, hari, jam)
    }

    fun updateJadwal(idJadwal: Int, kelas: String, hari: String, jam: String): Int {
        return dbHelper.updateJadwal(idJadwal, kelas,hari, jam)
    }

    fun deleteJadwal(idJadwal: Int): Int {
        return dbHelper.deleteJadwal(idJadwal)
    }

    fun getJadwalById(idJadwal: Int): JadwalEntity? {
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM jadwal_kelas WHERE id_jadwal = ?", arrayOf(idJadwal.toString()))
        var jadwal: JadwalEntity? = null
        if (cursor.moveToFirst()) {
            val kelas = cursor.getString(cursor.getColumnIndexOrThrow("kelas"))
            val hari = cursor.getString(cursor.getColumnIndexOrThrow("hari"))
            val jam = cursor.getString(cursor.getColumnIndexOrThrow("jam"))
            jadwal = JadwalEntity(idJadwal, kelas, hari, jam)
        }
        cursor.close()
        return jadwal
    }

    fun getAllJadwal(): LiveData<List<JadwalEntity>> {
        val liveData = MutableLiveData<List<JadwalEntity>>()
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM jadwal_kelas", null)
        val jadwalList = mutableListOf<JadwalEntity>()
        if (cursor.moveToFirst()) {
            do {
                val idJadwal = cursor.getInt(cursor.getColumnIndexOrThrow("id_jadwal"))
                val kelas = cursor.getString(cursor.getColumnIndexOrThrow("kelas"))
                val hari = cursor.getString(cursor.getColumnIndexOrThrow("hari"))
                val jam = cursor.getString(cursor.getColumnIndexOrThrow("jam"))
                val jadwal = JadwalEntity(idJadwal, kelas, hari, jam)
                jadwalList.add(jadwal)
            } while (cursor.moveToNext())
        }
        cursor.close()
        liveData.postValue(jadwalList)
        return liveData
    }
}
