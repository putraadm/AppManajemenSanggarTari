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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.EditorLombaActivity
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.LombaAdapter
import zhafran.putra.appproject2ckel3.model.Lomba
import zhafran.putra.appproject2ckel3.viewmodel.LombaViewModel

class LombaFragment : Fragment() {
    private lateinit var fab: FloatingActionButton
    private lateinit var listView: ListView
    private lateinit var adapter: LombaAdapter
    private lateinit var lombaViewModel: LombaViewModel

    private val EDIT_LOMBA_REQUEST_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lomba, container, false)

        fab = view.findViewById(R.id.fab)
        listView = view.findViewById(R.id.lVLomba)

        adapter = LombaAdapter(requireContext(), mutableListOf(), ::showPopupMenu)
        listView.adapter = adapter

        lombaViewModel = ViewModelProvider(this).get(LombaViewModel::class.java)

        loadLomba()

        fab.setOnClickListener {
            val intent = Intent(requireActivity(), EditorLombaActivity::class.java)
            startActivityForResult(intent, EDIT_LOMBA_REQUEST_CODE)
        }

        return view
    }

    private fun loadLomba() {
        lombaViewModel.getAllLomba().observe(viewLifecycleOwner, Observer { lombaEntityList ->
            val lombaList = lombaEntityList.map { entity ->
                Lomba(
                    idLomba = entity.idLomba,
                    poster = entity.poster,
                    judul = entity.judul,
                    deskripsi = entity.deskripsi
                )
            }
            adapter.updateList(lombaList.toMutableList())
        })
    }

    private fun showPopupMenu(view: View, lomba: Lomba) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_lomba_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(requireActivity(), EditorLombaActivity::class.java)
                    intent.putExtra("lomba_id", lomba.idLomba)
                    startActivityForResult(intent, EDIT_LOMBA_REQUEST_CODE)
                    true
                }
                R.id.menu_delete -> {
                    lombaViewModel.deleteLomba(lomba.idLomba) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Lomba deleted", Toast.LENGTH_SHORT).show()
                            loadLomba()
                        } else {
                            Toast.makeText(requireContext(), "Failed to delete lomba", Toast.LENGTH_SHORT).show()
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
        if (requestCode == EDIT_LOMBA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadLomba()
        }
    }
}
