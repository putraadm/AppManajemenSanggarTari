package zhafran.putra.appproject2ckel3.repository

import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import zhafran.putra.appproject2ckel3.database.DBOpenHelper

data class AbsensiEntity(
    val idAbsensi: Int,
    val idUser: Int,
    val tanggal: String,
    val status: String
)

class AbsensiRepository(context: Context) {

    private val dbHelper = DBOpenHelper(context)

    fun insertAbsensi(idUser: Int, tanggal: String, status: String): Long {
        return dbHelper.insertAbsensi(idUser, tanggal, status)
    }

    fun updateAbsensi(idAbsensi: Int, idUser: Int, tanggal: String, status: String): Int {
        return dbHelper.updateAbsensi(idAbsensi, idUser, tanggal, status)
    }

    fun deleteAbsensi(idAbsensi: Int): Int {
        return dbHelper.deleteAbsensi(idAbsensi)
    }

    fun getAbsensiById(idAbsensi: Int): AbsensiEntity? {
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM absensi WHERE id_absensi = ?", arrayOf(idAbsensi.toString()))
        var absensi: AbsensiEntity? = null
        if (cursor.moveToFirst()) {
            val idUser = cursor.getInt(cursor.getColumnIndexOrThrow("id_user"))
            val tanggal = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"))
            val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
            absensi = AbsensiEntity(idAbsensi, idUser, tanggal, status)
        }
        cursor.close()
        return absensi
    }

    fun getAllAbsensi(): LiveData<List<AbsensiEntity>> {
        val liveData = MutableLiveData<List<AbsensiEntity>>()
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM absensi", null)
        val absensiList = mutableListOf<AbsensiEntity>()
        if (cursor.moveToFirst()) {
            do {
                val idAbsensi = cursor.getInt(cursor.getColumnIndexOrThrow("id_absensi"))
                val idUser = cursor.getInt(cursor.getColumnIndexOrThrow("id_user"))
                val tanggal = cursor.getString(cursor.getColumnIndexOrThrow("tanggal"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
                val absensi = AbsensiEntity(idAbsensi, idUser, tanggal, status)
                absensiList.add(absensi)
            } while (cursor.moveToNext())
        }
        cursor.close()
        liveData.postValue(absensiList)
        return liveData
    }
}
