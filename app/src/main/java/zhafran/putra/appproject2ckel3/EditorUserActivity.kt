package zhafran.putra.appproject2ckel3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import android.view.View
import android.widget.Toast
import zhafran.putra.appproject2ckel3.viewmodel.UserViewModel
import zhafran.putra.appproject2ckel3.viewmodel.JadwalViewModel
import zhafran.putra.appproject2ckel3.repository.JadwalEntity

class EditorUserActivity : AppCompatActivity(), View.OnClickListener {
    private val userViewModel: UserViewModel by viewModels()
    private val jadwalViewModel: JadwalViewModel by viewModels()

    private lateinit var actvKelas: AutoCompleteTextView
    private lateinit var backBtnUser: ImageButton
    private lateinit var edUsername: EditText
    private lateinit var edFullname: EditText
    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText
    private lateinit var btnSave: Button

    private var cachedJadwalList: List<JadwalEntity> = emptyList()
    private var editingUserId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_user)

        backBtnUser = findViewById(R.id.backBtnUser)
        backBtnUser.setOnClickListener {
            finish()
        }

        actvKelas = findViewById(R.id.actvKelas)
        edUsername = findViewById(R.id.edUsername)
        edFullname = findViewById(R.id.edFullname)
        edEmail = findViewById(R.id.edEmail)
        edPassword = findViewById(R.id.edPassword)
        btnSave = findViewById(R.id.btn_save)

        loadKelasDropdown()

        // Check if editing existing user
        val userIdFromIntent = intent.getIntExtra("user_id", -1)
        editingUserId = if (userIdFromIntent != -1) userIdFromIntent else null
        if (editingUserId != null) {
            loadUserData(editingUserId!!)
        }

        btnSave.setOnClickListener(this)
    }

    private fun loadKelasDropdown() {
        jadwalViewModel.getAllJadwal().observe(this) { jadwalList ->
            cachedJadwalList = jadwalList
            val kelasList = jadwalList.map { it.kelas }.distinct()
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, kelasList)
            actvKelas.setAdapter(adapter)
        }
    }

    private fun loadUserData(userId: Int) {
        userViewModel.loadUser(userId)
        userViewModel.user.observe(this) { userEntity ->
            if (userEntity != null) {
                edFullname.setText(userEntity.namaLengkap)
                edUsername.setText(userEntity.username)
                edEmail.setText(userEntity.email)
                edPassword.setText(userEntity.password)
                val kelas = cachedJadwalList.find { it.id_jadwal == userEntity.idJadwalKelas }?.kelas
                if (kelas != null) {
                    actvKelas.setText(kelas, false)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save -> {
                val username = edUsername.text.toString()
                val fullname = edFullname.text.toString()
                val kelasStr = actvKelas.text.toString()
                val email = edEmail.text.toString()
                val password = edPassword.text.toString()

                if (username.isBlank() || fullname.isBlank() || kelasStr.isBlank() || email.isBlank() || password.isBlank()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return
                }

                val kelasName = kelasStr
                val matchedJadwal = cachedJadwalList.find { it.kelas == kelasName }
                val kelasId = matchedJadwal?.id_jadwal

                if (kelasId == null) {
                    Toast.makeText(this, "Invalid kelas value", Toast.LENGTH_SHORT).show()
                    return
                }

                if (editingUserId == null) {
                    // Insert new user
                    userViewModel.insertUser(fullname, username, email, password, kelasId, "user") { success ->
                        if (success) {
                            Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            // Delay finish to allow LiveData update
                            android.os.Handler(mainLooper).postDelayed({
                                finish()
                            }, 100)
                        } else {
                            Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Update existing user
                    userViewModel.updateUser(editingUserId!!, fullname, username, email, password, kelasId, "user") { success ->
                        if (success) {
                            Toast.makeText(this, "User updated", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            // Delay finish to allow LiveData update
                            android.os.Handler(mainLooper).postDelayed({
                                finish()
                            }, 100)
                        } else {
                            Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
