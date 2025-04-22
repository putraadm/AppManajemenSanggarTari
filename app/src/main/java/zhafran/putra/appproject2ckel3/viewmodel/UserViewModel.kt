package zhafran.putra.appproject2ckel3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zhafran.putra.appproject2ckel3.repository.UserEntity
import zhafran.putra.appproject2ckel3.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application.applicationContext)

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity?> = _user

    private val _userList = MutableLiveData<List<UserEntity>>()
    val userList: LiveData<List<UserEntity>> = _userList

    init {
        loadUserList()
    }

    private fun loadUserList() {
        viewModelScope.launch(Dispatchers.IO) {
            // Query database synchronously
            val cursor = repository.dbHelper.readableDatabase.rawQuery("SELECT * FROM user", null)
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
            withContext(Dispatchers.Main) {
                _userList.value = userList
            }
        }
    }

    fun loadUser(idUser: Int) {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                repository.getUserById(idUser)
            }
            _user.value = data
        }
    }

    fun getAllUser(): LiveData<List<UserEntity>> {
        return userList
    }

    fun insertUser(namaLengkap: String, username: String, email: String, password: String, idJadwalKelas: Int, level: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertUser(namaLengkap, username, email, password, idJadwalKelas, level)
            withContext(Dispatchers.Main) {
                onResult(result != -1L)
                loadUserList()
            }
        }
    }

    fun updateUser(idUser: Int, namaLengkap: String, username: String, email: String, password: String, idJadwalKelas: Int, level: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateUser(idUser, namaLengkap, username, email, password, idJadwalKelas, level)
            withContext(Dispatchers.Main) {
                onResult(result > 0)
                loadUserList()
            }
        }
    }

    fun deleteUser(idUser: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteUser(idUser)
            withContext(Dispatchers.Main) {
                onResult(result > 0)
                loadUserList()
            }
        }
    }
}
