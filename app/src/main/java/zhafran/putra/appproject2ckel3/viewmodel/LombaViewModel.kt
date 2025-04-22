package zhafran.putra.appproject2ckel3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zhafran.putra.appproject2ckel3.repository.LombaEntity
import zhafran.putra.appproject2ckel3.repository.LombaRepository

class LombaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LombaRepository(application.applicationContext)

    private val _lomba = MutableLiveData<LombaEntity?>()
    val lomba: LiveData<LombaEntity?> = _lomba

    fun loadLomba(idLomba: Int) {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                repository.getLombaById(idLomba)
            }
            _lomba.value = data
        }
    }

    fun getAllLomba(): LiveData<List<LombaEntity>> {
        return repository.getAllLomba()
    }

    fun insertLomba(poster: ByteArray?, judul: String, deskripsi: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertLomba(poster, judul, deskripsi)
            withContext(Dispatchers.Main) {
                onResult(result != -1L)
            }
        }
    }

    fun updateLomba(idLomba: Int, poster: ByteArray?, judul: String, deskripsi: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateLomba(idLomba, poster, judul, deskripsi)
            withContext(Dispatchers.Main) {
                onResult(result > 0)
            }
        }
    }

    fun deleteLomba(idLomba: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteLomba(idLomba)
            withContext(Dispatchers.Main) {
                onResult(result > 0)
            }
        }
    }
}
