package zhafran.putra.appproject2ckel3.ui_admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.databinding.FragmentHomeBinding
import zhafran.putra.appproject2ckel3.repository.JadwalRepository
import zhafran.putra.appproject2ckel3.repository.ReportRepository
import zhafran.putra.appproject2ckel3.repository.UserRepository

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val userRepository by lazy { UserRepository(requireContext()) }
    private val jadwalRepository by lazy { JadwalRepository(requireContext()) }
    private val reportRepository by lazy { ReportRepository(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // userName = it.getString("userName")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupCounts()
        setupButtonListeners()

        return root
    }

    private fun setupCounts() {
        userRepository.getAllUserProfiles { profiles ->
            activity?.runOnUiThread {
                _binding?.let {
                    it.tvJmlhUser.text = profiles.size.toString()
                }
            }
        }
        jadwalRepository.getAllJadwal { jadwalList ->
            activity?.runOnUiThread {
                _binding?.let {
                    it.tvJmlhJadwal.text = jadwalList.size.toString()
                }
            }
        }
        reportRepository.getAllReports { reportList ->
            activity?.runOnUiThread {
                _binding?.let {
                    it.tvJmlhLaporan.text = reportList.size.toString()
                }
            }
        }
    }

    private fun setupButtonListeners() {
        binding.btnKelolaUser.setOnClickListener {
            findNavController().navigate(R.id.nav_user)
        }
        binding.btnKelolaJadwal.setOnClickListener {
            findNavController().navigate(R.id.nav_jadwal_kelas)
        }
        binding.btnKelolaAbsensi.setOnClickListener {
            findNavController().navigate(R.id.nav_absensi)
        }
        binding.btnKelolaLomba.setOnClickListener {
            findNavController().navigate(R.id.nav_info_lomba)
        }
        binding.btnKelolaReport.setOnClickListener {
            findNavController().navigate(R.id.nav_report)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
