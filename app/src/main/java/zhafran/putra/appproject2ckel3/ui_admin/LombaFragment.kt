package zhafran.putra.appproject2ckel3.ui_admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.EditorLombaActivity
import zhafran.putra.appproject2ckel3.databinding.FragmentLombaBinding


class LombaFragment : Fragment() {
    private var _binding: FragmentLombaBinding? = null
    private val binding get() = _binding!!
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLombaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fab = binding.fab

        fab.setOnClickListener {
            val intent = Intent(requireActivity(), EditorLombaActivity::class.java)
            startActivity(intent)
        }

        return root
    }
}