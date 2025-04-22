package zhafran.putra.appproject2ckel3.repository

import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import zhafran.putra.appproject2ckel3.database.DBOpenHelper

data class UserEntity(
    val idUser: Int,
    val namaLengkap: String,
    val username: String,
    val email: String,
    val password: String,
    val idJadwalKelas: Int,
    val role: String
)

class UserRepository(context: Context) {

    internal val dbHelper = DBOpenHelper(context)

    fun insertUser(namaLengkap: String, username: String, email: String, password: String, idJadwalKelas: Int, role: String): Long {
        return dbHelper.insertUser(namaLengkap, username, email, password, idJadwalKelas, role)
    }

    fun updateUser(idUser: Int, namaLengkap: String, username: String, email: String, password: String, idJadwalKelas: Int, role: String): Int {
        return dbHelper.updateUser(idUser, namaLengkap, username, email, password, idJadwalKelas, role)
    }

    fun deleteUser(idUser: Int): Int {
        return dbHelper.deleteUser(idUser)
    }

    fun getUserById(idUser: Int): UserEntity? {
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM user WHERE id_user = ?", arrayOf(idUser.toString()))
        var user: UserEntity? = null
        if (cursor.moveToFirst()) {
            val namaLengkap = cursor.getString(cursor.getColumnIndexOrThrow("nama_lengkap"))
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            val idJadwalKelas = cursor.getInt(cursor.getColumnIndexOrThrow("id_jadwal_kelas"))
            val role = cursor.getString(cursor.getColumnIndexOrThrow("role"))
            user = UserEntity(idUser, namaLengkap, username, email, password, idJadwalKelas, role)
        }
        cursor.close()
        return user
    }

    fun getAllUser(): LiveData<List<UserEntity>> {
        val liveData = MutableLiveData<List<UserEntity>>()
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM user", null)
        val userList = mutableListOf<UserEntity>()
        if (cursor.moveToFirst()) {
            do {
                val idUser = cursor.getInt(cursor.getColumnIndexOrThrow("id_user"))
                val namaLengkap = cursor.getString(cursor.getColumnIndexOrThrow("nama_lengkap"))
                val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                val idJadwalKelas = cursor.getInt(cursor.getColumnIndexOrThrow("id_jadwal_kelas"))
                val role = cursor.getString(cursor.getColumnIndexOrThrow("role"))
                val user = UserEntity(idUser, namaLengkap, username, email, password, idJadwalKelas, role)
                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        liveData.postValue(userList)
        return liveData
    }
}
