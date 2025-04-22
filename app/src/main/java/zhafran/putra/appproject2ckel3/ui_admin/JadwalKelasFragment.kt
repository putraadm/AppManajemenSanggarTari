package zhafran.putra.appproject2ckel3.ui_admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import zhafran.putra.appproject2ckel3.EditorJadwalActivity
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.JadwalAdapter
import zhafran.putra.appproject2ckel3.model.Jadwal
import zhafran.putra.appproject2ckel3.viewmodel.JadwalViewModel

class JadwalKelasFragment : Fragment() {
    private lateinit var fab: View
    private lateinit var listView: ListView
    private lateinit var adapter: JadwalAdapter
    private lateinit var jadwalViewModel: JadwalViewModel

    private val EDIT_JADWAL_REQUEST_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jadwal_kelas, container, false)

        fab = view.findViewById(R.id.fab)
        listView = view.findViewById(R.id.list_view)

        adapter = JadwalAdapter(requireContext(), mutableListOf(), ::showPopupMenu)
        listView.adapter = adapter

        jadwalViewModel = ViewModelProvider(this).get(JadwalViewModel::class.java)

        loadJadwal()

        fab.setOnClickListener {
            val intent = Intent(requireActivity(), EditorJadwalActivity::class.java)
            startActivityForResult(intent, EDIT_JADWAL_REQUEST_CODE)
        }

        return view
    }

    private fun loadJadwal() {
        jadwalViewModel.getAllJadwal().observe(viewLifecycleOwner, Observer { jadwalEntityList ->
            val jadwalList = jadwalEntityList.map { entity ->
                Jadwal(
                    id_jadwal = entity.id_jadwal,
                    kelas = entity.kelas,
                    hari = entity.hari,
                    jam = entity.jam
                )
            }
            adapter.updateList(jadwalList.toMutableList())
        })
    }

    private fun showPopupMenu(view: View, jadwal: Jadwal) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_jadwal_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(requireActivity(), EditorJadwalActivity::class.java)
                    intent.putExtra("jadwal_id", jadwal.id_jadwal)
                    startActivityForResult(intent, EDIT_JADWAL_REQUEST_CODE)
                    true
                }
                R.id.menu_delete -> {
                    jadwalViewModel.deleteJadwal(jadwal.id_jadwal) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Jadwal deleted", Toast.LENGTH_SHORT).show()
                            loadJadwal()
                        } else {
                            Toast.makeText(requireContext(), "Failed to delete jadwal", Toast.LENGTH_SHORT).show()
                        }
                    }
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_JADWAL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadJadwal()
        }
    }
}
