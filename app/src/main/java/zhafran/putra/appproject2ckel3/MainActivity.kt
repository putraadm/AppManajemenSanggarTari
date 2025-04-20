package zhafran.putra.appproject2ckel3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var btnLogin: Button
    private lateinit var usernameInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout

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

            val intent = if (username == "admin" && password == "admin") {
                pref.prefStatus = true
                pref.prefLevel = "admin"
                Intent(context, AdminActivity::class.java)
            } else if (username == "user" && password == "user") {
                pref.prefStatus = true
                pref.prefLevel = "user"
                Intent(context, UserActivity::class.java)
            } else {
                Snackbar.make(findViewById(R.id.main), "Username atau password salah", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(intent)
            finish()
        }
    }
}
