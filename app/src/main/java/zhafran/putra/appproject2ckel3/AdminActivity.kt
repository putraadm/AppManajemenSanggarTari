package zhafran.putra.appproject2ckel3

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
//import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.ui.NavigationUI
import zhafran.putra.appproject2ckel3.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAdminBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarAdmin.toolbar)

//        binding.appBarAdmin.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_admin) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_user, R.id.nav_absensi, R.id.nav_jadwal_kelas, R.id.nav_info_lomba, R.id.nav_report
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navController.popBackStack(R.id.nav_home, false) // navigasi eksplisit ke home
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    NavigationUI.onNavDestinationSelected(menuItem, navController)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.nav_home -> {
//                    binding.appBarAdmin.fab.show()
//                }
//                else -> {
//                    binding.appBarAdmin.fab.hide()
//                }
//            }
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.admin, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun logout(item: MenuItem){
        val pref = preferences(this)
        pref.clear()
        pref.prefStatus = false

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun openEditJadwal(jadwalId: Int?) {
        val args = Bundle()
        if (jadwalId != null && jadwalId != 0) {
            args.putInt("jadwal_id", jadwalId)
        }
        navController.navigate(
            R.id.action_nav_jadwal_kelas_to_adminEditJadwalFragment,
            args
        )
    }

    fun openEditAbsensi(absensiId: Int?) {
        val args = Bundle()
        if (absensiId != null && absensiId != 0) {
            args.putInt("absensi_id", absensiId)
        }
        navController.navigate(
            R.id.action_nav_absensi_to_adminEditAbsensiFragment,
            args
        )
    }

    fun openEditLomba(lombaId: Int?) {
        val args = lombaId?.let { Bundle().apply { putInt("lomba_id", it) } } ?: Bundle()
        navController.navigate(
            R.id.action_nav_info_lomba_to_adminEditLombaFragment,
            args
        )
    }

    fun openEditReport(reportId: Int?) {
        val args = reportId?.let { Bundle().apply { putInt("report_id", it) } } ?: Bundle()
        navController.navigate(
            R.id.action_nav_report_to_adminEditReportFragment,
            args
        )
    }

    fun openEditUser(userId: Int?) {
        val args = userId?.let { Bundle().apply { putInt("user_id", it) } } ?: Bundle()
        navController.navigate(
            R.id.action_nav_user_to_adminEditUserFragment,
            args
        )
    }
}
