package zhafran.putra.appproject2ckel3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.widget.DatePicker
import zhafran.putra.appproject2ckel3.databinding.ActivityEditorReportBinding
import zhafran.putra.appproject2ckel3.viewmodel.ReportViewModel
import zhafran.putra.appproject2ckel3.viewmodel.UserViewModel
import java.util.Calendar

class EditorReportActivity : AppCompatActivity() {
    private lateinit var b: ActivityEditorReportBinding
    private val reportViewModel: ReportViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private var idReport: Int? = null
    private var selectedUserId: Int? = null
    private var userNameList: List<String> = emptyList()
    private var userMap: Map<String, Int> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEditorReportBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.backBtnReport.setOnClickListener {
            finish()
        }

        b.btnSaveReport.setOnClickListener {
            saveReport()
        }

        setupUserDropdown()
        setupDatePicker()

        // Setup date icon click to show DatePicker
        b.dateInputLayout.setEndIconOnClickListener {
            showDatePickerDialog()
        }

        idReport = intent.getIntExtra("report_id", -1)
        if (idReport == -1) {
            idReport = null
        } else {
            // Load existing report data if needed
            reportViewModel.loadReport(idReport!!)
            reportViewModel.report.observe(this) { reportEntity ->
                reportEntity?.let {
                    b.tanggal.setText(it.tanggal)
                    b.wirama.setText(it.wirama)
                    b.wirasa.setText(it.wirasa)
                    b.wiraga.setText(it.wiraga)
                    b.catatan.setText(it.catatan)
                    // Set selected user name in dropdown
                    userViewModel.getAllUser().observe(this) { userList ->
                        userMap = userList.associate { it.namaLengkap to it.idUser }
                        val userName = userMap.entries.find { entry -> entry.value == it.idUser }?.key
                        userName?.let { name ->
                            (b.actvSiswaRp as AutoCompleteTextView).setText(name, false)
                            selectedUserId = userMap[name]
                        }
                    }
                }
            }
        }
    }

    private fun setupUserDropdown() {
        userViewModel.getAllUser().observe(this) { userList ->
            userMap = userList.associate { it.namaLengkap to it.idUser }
            userNameList = userMap.keys.toList()
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, userNameList)
            (b.actvSiswaRp as AutoCompleteTextView).setAdapter(adapter)

            (b.actvSiswaRp as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id ->
                val selectedName = parent.getItemAtPosition(position) as String
                selectedUserId = userMap[selectedName]
            }
        }
    }

    private fun setupDatePicker() {
        b.tanggal.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val monthFormatted = String.format("%02d", selectedMonth + 1)
            val dayFormatted = String.format("%02d", selectedDay)
            val dateString = "$selectedYear-$monthFormatted-$dayFormatted"
            b.tanggal.setText(dateString)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveReport() {
        val tanggal = b.tanggal.text.toString()
        val wirama = b.wirama.text.toString()
        val wirasa = b.wirasa.text.toString()
        val wiraga = b.wiraga.text.toString()
        val catatan = b.catatan.text.toString()

        if (tanggal.isBlank() || wirama.isBlank() || wirasa.isBlank() || wiraga.isBlank() || catatan.isBlank()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val idUser = selectedUserId
        if (idUser == null) {
            Toast.makeText(this, "Please select a valid user", Toast.LENGTH_SHORT).show()
            return
        }

        if (idReport == null) {
            reportViewModel.insertReport(idUser, tanggal, wirama, wirasa, wiraga, catatan) { success ->
                if (success) {
                    Toast.makeText(this, "Report added", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add report", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            reportViewModel.updateReport(idReport!!, idUser, tanggal, wirama, wirasa, wiraga, catatan) { success ->
                if (success) {
                    Toast.makeText(this, "Report updated", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update report", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
