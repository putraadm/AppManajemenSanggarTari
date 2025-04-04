package zhafran.putra.appproject2ckel3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        btnLogin = findViewById(R.id.btnLogin)

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
            val username = findViewById<EditText>(R.id.edUsername).text.toString()
            val password = findViewById<EditText>(R.id.edPassword).text.toString()
            if (username.isEmpty()) {
                Snackbar.make(findViewById(R.id.main), "Username tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Snackbar.make(findViewById(R.id.main), "Password tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
            } else {
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
}
