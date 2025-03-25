package zhafran.putra.appproject2ckel3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import zhafran.putra.appproject2ckel3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var b : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemUser -> {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
            R.id.itemJadwal -> {
                val intent = Intent(this, JadwalKelasActivity::class.java)
                startActivity(intent)
            }
            R.id.itemAbsensi -> {
                val intent = Intent(this, AbsensiActivity::class.java)
                startActivity(intent)
            }
            R.id.itemReport -> {
                val intent = Intent(this, ReportActivity::class.java)
                startActivity(intent)
            }
            R.id.itemLomba -> {
                val intent = Intent(this, LombaActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {

    }
}
