package zhafran.putra.appproject2ckel3.ui_user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import zhafran.putra.appproject2ckel3.databinding.FragmentJadwalKelasUserBinding
import zhafran.putra.appproject2ckel3.preferences
import zhafran.putra.appproject2ckel3.viewmodel.JadwalViewModel

class JadwalKelasFragmentUser : Fragment() {
    private var _binding: FragmentJadwalKelasUserBinding? = null
    private val binding get() = _binding!!

    private var idKelas: Int = -1
    private lateinit var preferences: preferences

    private val jadwalViewModel: JadwalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = preferences(requireContext())
        idKelas = preferences.prefUserKelasId
        Log.d("JadwalFragment", "ID Kelas dari preferences: $idKelas")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJadwalKelasUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (idKelas != -1 && idKelas != 0) {
            jadwalViewModel.jadwalListLiveData.observe(viewLifecycleOwner) { jadwalList ->
                Log.d("JadwalFragment", "LiveData updated: $jadwalList")
                if (jadwalList.isNotEmpty()) {
                    val jadwal = jadwalList[0]
                    binding.tvKelas.text = jadwal.kelas
                    binding.tvHari.text = jadwal.hari
                    binding.tvJam.text = jadwal.jam
                } else {
                    Log.w("JadwalFragment", "Tidak ada jadwal ditemukan.")
                }
            }
            jadwalViewModel.loadJadwalByKelas(idKelas)
        } else {
            Log.e("JadwalFragment", "ID Kelas tidak valid, memuat semua jadwal")
            jadwalViewModel.jadwalListLiveData.observe(viewLifecycleOwner) { jadwalList ->
                if (jadwalList.isNotEmpty()) {
                    val jadwal = jadwalList[0]
                    binding.tvKelas.text = jadwal.kelas
                    binding.tvHari.text = jadwal.hari
                    binding.tvJam.text = jadwal.jam
                } else {
                    Log.w("JadwalFragment", "Tidak ada jadwal ditemukan.")
                }
            }
            jadwalViewModel.loadAllJadwal()
        }

        binding.tvNamaLengkap.text = preferences.prefUserFullName ?: "Nama Lengkap"
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
