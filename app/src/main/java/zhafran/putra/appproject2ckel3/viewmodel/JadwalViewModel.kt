package zhafran.putra.appproject2ckel3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zhafran.putra.appproject2ckel3.repository.JadwalEntity
import zhafran.putra.appproject2ckel3.repository.JadwalRepository

class JadwalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = JadwalRepository(application.applicationContext)

    private val _jadwal = MutableLiveData<JadwalEntity?>()
    val jadwal: LiveData<JadwalEntity?> = _jadwal

    fun loadJadwal(idJadwal: Int) {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                repository.getJadwalById(idJadwal)
            }
            _jadwal.value = data
        }
    }

    fun getAllJadwal(): LiveData<List<JadwalEntity>> {
        return repository.getAllJadwal()
    }

    fun insertJadwal(kelas: String, hari: String, jam: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertJadwal(kelas, hari, jam)
            withContext(Dispatchers.Main) {
                onResult(result != -1L)
            }
        }
    }

    fun updateJadwal(idJadwal: Int, kelas: String, hari: String, jam: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateJadwal(idJadwal, kelas, hari, jam)
            withContext(Dispatchers.Main) {
                onResult(result > 0)
            }
        }
    }

    fun deleteJadwal(idJadwal: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteJadwal(idJadwal)
            withContext(Dispatchers.Main) {
                onResult(result > 0)
            }
        }
    }
}
