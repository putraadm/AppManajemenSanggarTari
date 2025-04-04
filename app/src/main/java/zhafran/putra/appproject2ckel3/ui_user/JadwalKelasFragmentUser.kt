package zhafran.putra.appproject2ckel3.ui_user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import zhafran.putra.appproject2ckel3.databinding.FragmentJadwalKelasUserBinding

class JadwalKelasFragmentUser : Fragment() {
    private var _binding: FragmentJadwalKelasUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJadwalKelasUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}