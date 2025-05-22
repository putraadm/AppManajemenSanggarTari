package zhafran.putra.appproject2ckel3.ui_user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.AbsensiAdapter
import zhafran.putra.appproject2ckel3.databinding.FragmentAbsensiUserBinding
import zhafran.putra.appproject2ckel3.preferences
import zhafran.putra.appproject2ckel3.viewmodel.AbsensiViewModel

class AbsensiFragmentUser : Fragment() {
    private var _binding: FragmentAbsensiUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AbsensiAdapter
    private lateinit var listView: ListView
    private lateinit var spinnerMonthFilter: Spinner
    private var userId: Int = -1

    private lateinit var preferences: preferences

    private val absensiViewModel: AbsensiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = preferences(requireContext())
        userId = preferences.prefUserId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAbsensiUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        spinnerMonthFilter = root.findViewById(R.id.spinnerMonthFilter)

        adapter = AbsensiAdapter(requireContext(), mutableListOf(), emptyMap(), { _, _ -> }, AbsensiAdapter.Mode.USER)
        listView = binding.listView
        listView.adapter = adapter

        setupMonthFilter()
        observeViewModel()

        return root
    }

    private fun setupMonthFilter() {
        val months = listOf("All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val adapterSpinner = android.widget.ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonthFilter.adapter = adapterSpinner

        spinnerMonthFilter.setSelection(0)
        spinnerMonthFilter.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMonth = spinnerMonthFilter.selectedItem.toString()
                val monthNumber = when (selectedMonth) {
                    "January" -> "01"
                    "February" -> "02"
                    "March" -> "03"
                    "April" -> "04"
                    "May" -> "05"
                    "June" -> "06"
                    "July" -> "07"
                    "August" -> "08"
                    "September" -> "09"
                    "October" -> "10"
                    "November" -> "11"
                    "December" -> "12"
                    else -> null
                }
                absensiViewModel.loadAbsensi(userId) {
                    absensiViewModel.filterAbsensiByMonth(monthNumber)
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }
    }

    private fun observeViewModel() {
        absensiViewModel.userMapLiveData.observe(viewLifecycleOwner) { userMap ->
            adapter.userMap = userMap
        }
        absensiViewModel.absensiListLiveData.observe(viewLifecycleOwner) { absensiList ->
            adapter.updateList(absensiList.toMutableList())
        }
        absensiViewModel.loadUserMap()
        absensiViewModel.loadAbsensi(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
