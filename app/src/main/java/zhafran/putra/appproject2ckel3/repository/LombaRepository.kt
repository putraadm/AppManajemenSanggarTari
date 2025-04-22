package zhafran.putra.appproject2ckel3.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import zhafran.putra.appproject2ckel3.database.DBOpenHelper

data class LombaEntity(
    val idLomba: Int,
    val poster: ByteArray?,
    val judul: String,
    val deskripsi: String
)

class LombaRepository(context: Context) {

    private val dbHelper = DBOpenHelper(context)

    fun insertLomba(poster: ByteArray?, judul: String, deskripsi: String): Long {
        return dbHelper.insertLomba(poster, judul, deskripsi)
    }

    fun updateLomba(idLomba: Int, poster: ByteArray?, judul: String, deskripsi: String): Int {
        return dbHelper.updateLomba(idLomba, poster, judul, deskripsi)
    }

    fun deleteLomba(idLomba: Int): Int {
        return dbHelper.deleteLomba(idLomba)
    }

    fun getLombaById(idLomba: Int): LombaEntity? {
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM lomba WHERE id_lomba = ?", arrayOf(idLomba.toString()))
        var lomba: LombaEntity? = null
        if (cursor.moveToFirst()) {
            val poster = cursor.getBlob(cursor.getColumnIndexOrThrow("poster"))
            val judul = cursor.getString(cursor.getColumnIndexOrThrow("judul"))
            val deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"))
            lomba = LombaEntity(idLomba, poster, judul, deskripsi)
        }
        cursor.close()
        return lomba
    }

    fun getAllLomba(): LiveData<List<LombaEntity>> {
        val liveData = MutableLiveData<List<LombaEntity>>()
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM lomba", null)
        val lombaList = mutableListOf<LombaEntity>()
        if (cursor.moveToFirst()) {
            do {
                val idLomba = cursor.getInt(cursor.getColumnIndexOrThrow("id_lomba"))
                val poster = cursor.getBlob(cursor.getColumnIndexOrThrow("poster"))
                val judul = cursor.getString(cursor.getColumnIndexOrThrow("judul"))
                val deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"))
                val lomba = LombaEntity(idLomba, poster, judul, deskripsi)
                lombaList.add(lomba)
            } while (cursor.moveToNext())
        }
        cursor.close()
        liveData.postValue(lombaList)
        return liveData
    }
}
