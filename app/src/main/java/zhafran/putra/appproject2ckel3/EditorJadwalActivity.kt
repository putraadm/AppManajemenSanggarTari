package zhafran.putra.appproject2ckel3

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import zhafran.putra.appproject2ckel3.databinding.ActivityEditorJadwalBinding
import zhafran.putra.appproject2ckel3.viewmodel.JadwalViewModel
import java.util.Calendar

class EditorJadwalActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var b : ActivityEditorJadwalBinding
    private val jadwalViewModel: JadwalViewModel by viewModels()

    private var idJadwal: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEditorJadwalBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.backBtnJadwal.setOnClickListener {
            finish()
        }

        b.btnSaveJadwal.setOnClickListener(this)

        // Setup time picker dialog on time input and icon click
        b.edJam.setOnClickListener {
            showTimePickerDialog()
        }
        b.timeInputLayout.setEndIconOnClickListener {
            showTimePickerDialog()
        }

        // Check if editing existing jadwal
        idJadwal = intent.getIntExtra("jadwal_id", -1)
        if (idJadwal == -1) {
            idJadwal = null
        } else {
            // Load existing jadwal data if needed
            jadwalViewModel.loadJadwal(idJadwal!!)
            jadwalViewModel.jadwal.observe(this) { jadwalEntity ->
                jadwalEntity?.let {
                    b.edKelas.setText(it.kelas)
                    b.edHari.setText(it.hari)
                    b.edJam.setText(it.jam)
                }
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val hourFormatted = String.format("%02d", selectedHour)
            val minuteFormatted = String.format("%02d", selectedMinute)
            val timeString = "$hourFormatted:$minuteFormatted"
            b.edJam.setText(timeString)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSaveJadwal -> {
                val kelas = b.edKelas.text.toString()
                val hari = b.edHari.text.toString()
                val jam = b.edJam.text.toString()

                if (kelas.isBlank() || hari.isBlank() || jam.isBlank()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return
                }

                if (idJadwal == null) {
                    // Insert new jadwal
                    jadwalViewModel.insertJadwal(kelas, hari, jam) { success ->
                        if (success) {
                            Toast.makeText(this, "Jadwal added", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to add jadwal", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Update existing jadwal
                    jadwalViewModel.updateJadwal(idJadwal!!, kelas, hari, jam) { success ->
                        if (success) {
                            Toast.makeText(this, "Jadwal updated", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to update jadwal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
