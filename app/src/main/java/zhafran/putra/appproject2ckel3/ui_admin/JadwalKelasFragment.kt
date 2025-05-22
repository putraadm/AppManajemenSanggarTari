package zhafran.putra.appproject2ckel3.ui_admin

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.JadwalAdapter
import zhafran.putra.appproject2ckel3.model.Jadwal
import zhafran.putra.appproject2ckel3.repository.JadwalRepository

class JadwalKelasFragment : Fragment() {
    private lateinit var fab: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JadwalAdapter

    private var jadwalRepository: JadwalRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jadwalRepository = context?.let { JadwalRepository(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jadwal_kelas, container, false)

        fab = view.findViewById(R.id.fab)
        recyclerView = view.findViewById(R.id.recycler_view)

        adapter = JadwalAdapter(mutableListOf(), ::showPopupMenu)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadJadwal()

        fab.setOnClickListener {
            (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditJadwal(0)
        }

        return view
    }

    private fun loadJadwal() {
        jadwalRepository?.getAllJadwal { jadwalList ->
            adapter.updateList(jadwalList.toMutableList())
        }
    }

    private fun showPopupMenu(view: View, jadwal: Jadwal) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_jadwal_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditJadwal(jadwal.idJadwal ?: 0)
                    true
                }
                R.id.menu_delete -> {
                    jadwalRepository?.deleteJadwal(jadwal.idJadwal ?: 0) { success ->
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
}
