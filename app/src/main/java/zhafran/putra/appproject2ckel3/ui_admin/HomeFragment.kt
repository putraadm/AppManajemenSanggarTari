package zhafran.putra.appproject2ckel3.ui_admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
                binding.tvJmlhUser.text = profiles.size.toString()
            }
        }
        jadwalRepository.getAllJadwal { jadwalList ->
            activity?.runOnUiThread {
                binding.tvJmlhJadwal.text = jadwalList.size.toString()
            }
        }
        reportRepository.getAllReports { reportList ->
            activity?.runOnUiThread {
                binding.tvJmlhLaporan.text = reportList.size.toString()
            }
        }
    }

    private fun setupButtonListeners() {
        binding.btnKelolaUser.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_admin, UserFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.btnKelolaJadwal.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_admin, AdminEditJadwalFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.btnKelolaReport.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_admin, AdminEditReportFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
