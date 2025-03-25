package zhafran.putra.appproject2ckel3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AbsensiActivity : AppCompatActivity() {

    private lateinit var fab : FloatingActionButton
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absensi)
        recyclerView = findViewById(R.id.reycler_view)
        fab = findViewById(R.id.fab)

        fab.setOnClickListener {
            val intent = Intent(this, EditorAbsensiActivity::class.java)
            startActivity(intent)
        }
    }
}