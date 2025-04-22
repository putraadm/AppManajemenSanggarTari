package zhafran.putra.appproject2ckel3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zhafran.putra.appproject2ckel3.repository.ReportEntity
import zhafran.putra.appproject2ckel3.repository.ReportRepository

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReportRepository(application.applicationContext)

    private val _report = MutableLiveData<ReportEntity?>()
    val report: LiveData<ReportEntity?> = _report

    fun loadReport(idReport: Int) {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                repository.getReportById(idReport)
            }
            _report.value = data
        }
    }

    fun getAllReport(): LiveData<List<ReportEntity>> {
        return repository.getAllReport()
    }

    fun insertReport(idUser: Int, tanggal: String, wirama: String, wirasa: String, wiraga: String, catatan: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertReport(idUser, tanggal, wirama, wirasa, wiraga, catatan)
            withContext(Dispatchers.Main) {
                onResult(result != -1L)
            }
        }
    }

    fun updateReport(idReport: Int, idUser: Int, tanggal: String, wirama: String, wirasa: String, wiraga: String, catatan: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateReport(idReport, idUser, tanggal, wirama, wirasa, wiraga, catatan)
            withContext(Dispatchers.Main) {
                onResult(result > 0)
            }
        }
    }

    fun deleteReport(idReport: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteReport(idReport)
            withContext(Dispatchers.Main) {
                onResult(result > 0)
            }
        }
    }
}
