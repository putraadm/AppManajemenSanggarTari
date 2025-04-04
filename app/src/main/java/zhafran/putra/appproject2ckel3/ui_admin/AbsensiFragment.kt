package zhafran.putra.appproject2ckel3.ui_admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.EditorAbsensiActivity
import zhafran.putra.appproject2ckel3.databinding.FragmentAbsensiBinding


class AbsensiFragment : Fragment() {
    private var _binding: FragmentAbsensiBinding? = null
    private val binding get() = _binding!!
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAbsensiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fab = binding.fab

        fab.setOnClickListener {
            val intent = Intent(requireActivity(), EditorAbsensiActivity::class.java)
            startActivity(intent)
        }

        return root
    }
}