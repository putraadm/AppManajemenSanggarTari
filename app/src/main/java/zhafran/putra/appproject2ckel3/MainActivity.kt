package zhafran.putra.appproject2ckel3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import zhafran.putra.appproject2ckel3.viewmodel.LoginViewModel
import zhafran.putra.appproject2ckel3.preferences

class MainActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var btnLogin: Button
    private lateinit var usernameInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        btnLogin = findViewById(R.id.btnLogin)
        usernameInputLayout = findViewById(R.id.textInputLayout)
        passwordInputLayout = findViewById(R.id.textInputLayout2)

        val pref = preferences(context)
        if (pref.prefStatus) {
            val intent = if (pref.prefLevel == "admin") {
                Intent(context, AdminActivity::class.java)
            } else {
                Intent(context, UserActivity::class.java)
            }
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val usernameEditText = findViewById<EditText>(R.id.edUsername)
            val passwordEditText = findViewById<EditText>(R.id.edPassword)
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            var valid = true

            if (username.isEmpty()) {
                usernameInputLayout.error = "Username tidak boleh kosong"
                valid = false
            } else {
                usernameInputLayout.error = null
            }

            if (password.isEmpty()) {
                passwordInputLayout.error = "Password tidak boleh kosong"
                valid = false
            } else {
                passwordInputLayout.error = null
            }

            if (!valid) {
                return@setOnClickListener
            }

            loginViewModel.login(username, password)
        }

        loginViewModel.loginResult.observe(this) { user ->
            val pref = preferences(context)
            if (user == null) {
                Snackbar.make(findViewById(R.id.main), "Username atau password salah", Snackbar.LENGTH_SHORT).show()
            } else {
                pref.prefStatus = true
                pref.prefLevel = user.role
                pref.prefUserId = user.idUser ?: 0
                pref.prefUserFullName = user.namaLengkap
                val kelasId = user.kelasId
                Log.d("MainActivity", "user.kelasId: $kelasId")
                if (kelasId != null && kelasId > 0) {
                    pref.prefUserKelasId = kelasId
                } else {
                    pref.prefUserKelasId = -1
                }
                val intent = if (user.role == "admin") {
                    Intent(context, AdminActivity::class.java)
                } else {
                    Intent(context, UserActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}
