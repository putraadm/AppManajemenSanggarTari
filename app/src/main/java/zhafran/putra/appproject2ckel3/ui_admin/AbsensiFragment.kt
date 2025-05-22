package zhafran.putra.appproject2ckel3.ui_admin

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.AbsensiGroupedAdapter
import zhafran.putra.appproject2ckel3.repository.AbsensiRepository
import zhafran.putra.appproject2ckel3.repository.UserRepository
import zhafran.putra.appproject2ckel3.model.Absensi as ModelAbsensi

class AbsensiFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var listView: android.widget.ListView
    private lateinit var adapter: AbsensiGroupedAdapter

    private val absensiRepository by lazy { AbsensiRepository(requireContext()) }
    private val userRepository by lazy { UserRepository(requireContext()) }

    private var userMap: Map<Int, String> = emptyMap()
    private var absensiGroupedData: List<Pair<String, List<ModelAbsensi>>> = listOf()

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        val view = inflater.inflate(R.layout.fragment_absensi, container, false)

        fab = view.findViewById(R.id.fab)
        listView = view.findViewById(R.id.list_view)

        adapter = AbsensiGroupedAdapter(requireContext(), listOf(), emptyMap(), this::showPopupMenu)
        listView.adapter = adapter

        loadUserProfiles()
        loadAbsensi()

        fab.setOnClickListener {
            (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditAbsensi(0)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadUserProfiles()
        loadAbsensi()
    }

    private fun loadUserProfiles() {
        userRepository.getAllUserProfiles { users ->
            userMap = users.associate { it.idUser to (it.namaLengkap ?: "Unknown") }
            updateAdapterData()
        }
    }

    private fun loadAbsensi() {
        absensiRepository.getAllAbsensi { absensiList ->
            val modelAbsensiList = absensiList.map {
                ModelAbsensi(
                    idAbsensi = it.idAbsensi,
                    idUser = it.idUser,
                    tanggal = it.tanggal,
                    status = it.status
                )
            }
            absensiGroupedData = modelAbsensiList.groupBy { it.tanggal }.toList().sortedByDescending { it.first }
            updateAdapterData()
        }
    }

    private fun updateAdapterData() {
        if (userMap.isNotEmpty() && absensiGroupedData.isNotEmpty()) {
            adapter = AbsensiGroupedAdapter(requireContext(), absensiGroupedData, userMap, this::showPopupMenu)
            listView.adapter = adapter
        }
    }

    private fun showPopupMenu(view: android.view.View, absensi: ModelAbsensi) {
        val popup = android.widget.PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_absensi_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditAbsensi(absensi.idAbsensi)
                    true
                }
                R.id.menu_delete -> {
                    absensiRepository.deleteAbsensi(absensi.idAbsensi) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Absensi deleted", Toast.LENGTH_SHORT).show()
                            loadAbsensi()
                        } else {
                            Toast.makeText(requireContext(), "Failed to delete absensi", Toast.LENGTH_SHORT).show()
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
