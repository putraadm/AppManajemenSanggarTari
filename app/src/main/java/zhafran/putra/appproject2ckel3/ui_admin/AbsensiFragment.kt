package zhafran.putra.appproject2ckel3.ui_admin

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.EditorAbsensiActivity
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.AbsensiAdapter
import zhafran.putra.appproject2ckel3.model.Absensi
import zhafran.putra.appproject2ckel3.viewmodel.AbsensiViewModel

class AbsensiFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var listView: android.widget.ListView
    private lateinit var adapter: AbsensiAdapter
    private lateinit var absensiViewModel: AbsensiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_absensi, container, false)

        fab = view.findViewById(R.id.fab)
        listView = view.findViewById(R.id.list_view)

        adapter = AbsensiAdapter(requireContext(), mutableListOf(), this::showPopupMenu)
        listView.adapter = adapter

        absensiViewModel = ViewModelProvider(this).get(AbsensiViewModel::class.java)

        loadAbsensi()

        fab.setOnClickListener {
            val intent = Intent(requireActivity(), EditorAbsensiActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun loadAbsensi() {
        absensiViewModel.getAllAbsensi().observe(viewLifecycleOwner, Observer { absensiEntityList ->
            val absensiList = absensiEntityList.map { entity ->
                Absensi(
                    idAbsensi = entity.idAbsensi,
                    idUser = entity.idUser,
                    tanggal = entity.tanggal,
                    status = entity.status
                )
            }
            adapter.updateList(absensiList)
        })
    }

    private fun showPopupMenu(view: View, absensi: Absensi) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_absensi_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(requireActivity(), EditorAbsensiActivity::class.java)
                    intent.putExtra("absensi_id", absensi.idAbsensi)
                    startActivity(intent)
                    true
                }
                R.id.menu_delete -> {
                    absensiViewModel.deleteAbsensi(absensi.idAbsensi) { success ->
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
