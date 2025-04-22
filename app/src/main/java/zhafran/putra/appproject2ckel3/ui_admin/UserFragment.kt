package zhafran.putra.appproject2ckel3.ui_admin

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.EditorUserActivity
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.UserAdapter
import zhafran.putra.appproject2ckel3.model.User
import zhafran.putra.appproject2ckel3.viewmodel.UserViewModel

class UserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var listView: ListView
    private lateinit var adapter: UserAdapter
    private lateinit var fab: FloatingActionButton

    private val addUserLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            // Explicitly reload users as fallback to ensure UI refresh
            userViewModel.getAllUser().value?.let {
                adapter.updateList(it.map { userEntity ->
                    User(
                        idUser = userEntity.idUser,
                        namaLengkap = userEntity.namaLengkap,
                        username = userEntity.username,
                        email = userEntity.email,
                        password = userEntity.password,
                        idJadwalKelas = userEntity.idJadwalKelas,
                        role = userEntity.role
                    )
                }.toMutableList())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        listView = view.findViewById(R.id.lvUser)

        adapter = UserAdapter(requireContext(), mutableListOf())
        listView.adapter = adapter

        fab = view.findViewById(R.id.fab)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        fab.setOnClickListener {
            val intent = Intent(requireActivity(), EditorUserActivity::class.java)
            addUserLauncher.launch(intent)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getAllUser().observe(viewLifecycleOwner, Observer { userEntities ->
            val users = userEntities.map { userEntity ->
                User(
                    idUser = userEntity.idUser,
                    namaLengkap = userEntity.namaLengkap,
                    username = userEntity.username,
                    email = userEntity.email,
                    password = userEntity.password,
                    idJadwalKelas = userEntity.idJadwalKelas,
                    role = userEntity.role
                )
            }
            adapter.updateList(users.toMutableList())
        })

        listView.setOnItemClickListener { _, viewClicked, position, _ ->
            val user = adapter.getItem(position) as User
            showPopupMenu(user, viewClicked)
        }
    }

    private fun showPopupMenu(user: User, anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)
        popup.menuInflater.inflate(R.menu.menu_user_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(requireActivity(), EditorUserActivity::class.java)
                    intent.putExtra("user_id", user.idUser)
                    startActivity(intent)
                    true
                }
                R.id.menu_delete -> {
                    userViewModel.deleteUser(user.idUser) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "User deleted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to delete user", Toast.LENGTH_SHORT).show()
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