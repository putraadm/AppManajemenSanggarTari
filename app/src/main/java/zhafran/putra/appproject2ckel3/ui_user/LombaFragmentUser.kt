package zhafran.putra.appproject2ckel3.ui_user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import zhafran.putra.appproject2ckel3.adapter.LombaAdapter
import zhafran.putra.appproject2ckel3.databinding.FragmentLombaUserBinding
import zhafran.putra.appproject2ckel3.viewmodel.LombaViewModel

class LombaFragmentUser : Fragment() {
    private var _binding: FragmentLombaUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LombaAdapter
    private lateinit var recyclerView: RecyclerView

    private val lombaViewModel: LombaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLombaUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = LombaAdapter(mutableListOf()) { _, _ -> }
        recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lombaViewModel.lombaListLiveData.observe(viewLifecycleOwner) { lombaList ->
            adapter.updateList(lombaList.toMutableList())
        }
        lombaViewModel.loadLomba()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}