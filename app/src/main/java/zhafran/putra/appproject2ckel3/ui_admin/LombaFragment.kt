package zhafran.putra.appproject2ckel3.ui_admin

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.LombaAdapter
import zhafran.putra.appproject2ckel3.model.Lomba
import zhafran.putra.appproject2ckel3.repository.LombaRepository

class LombaFragment : Fragment() {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LombaAdapter

    private var lombaRepository: LombaRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lombaRepository = context?.let { LombaRepository(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lomba, container, false)

        fab = view.findViewById(R.id.fab)
        recyclerView = view.findViewById(R.id.rvLomba)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = LombaAdapter(mutableListOf(), ::showPopupMenu)
        recyclerView.adapter = adapter

        loadLomba()

        fab.setOnClickListener {
            (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditLomba(0)
        }

        return view
    }

    private fun loadLomba() {
        lombaRepository?.getAllLomba { lombaList ->
            adapter.updateList(lombaList.toMutableList())
        }
    }

    private fun showPopupMenu(view: View, lomba: Lomba) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_lomba_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditLomba(lomba.idLomba ?: 0)
                    true
                }
                R.id.menu_delete -> {
                    lombaRepository?.deleteLomba(lomba.idLomba ?: 0) { success ->
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
}
