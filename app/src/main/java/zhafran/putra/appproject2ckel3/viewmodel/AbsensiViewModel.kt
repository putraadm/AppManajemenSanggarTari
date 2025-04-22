package zhafran.putra.appproject2ckel3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zhafran.putra.appproject2ckel3.repository.AbsensiEntity
import zhafran.putra.appproject2ckel3.repository.AbsensiRepository

class AbsensiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AbsensiRepository(application.applicationContext)

    private val _absensi = MutableLiveData<AbsensiEntity?>()
    val absensi: LiveData<AbsensiEntity?> = _absensi

    fun loadAbsensi(idAbsensi: Int) {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                repository.getAbsensiById(idAbsensi)
            }
            _absensi.value = data
        }
    }

    fun getAllAbsensi(): LiveData<List<AbsensiEntity>> {
        return repository.getAllAbsensi()
    }

    fun insertAbsensi(idUser: Int, tanggal: String, status: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertAbsensi(idUser, tanggal, status)
            withContext(Dispatchers.Main) {
                onResult(result != -1L)
            }
        }
    }

    fun updateAbsensi(idAbsensi: Int, idUser: Int, tanggal: String, status: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateAbsensi(idAbsensi, idUser, tanggal, status)
            withContext(Dispatchers.Main) {
                onResult(result > 0)
            }
        }
    }

    fun deleteAbsensi(idAbsensi: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteAbsensi(idAbsensi)
            withContext(Dispatchers.Main) {
                onResult(result > 0)
            }
        }
    }
}
