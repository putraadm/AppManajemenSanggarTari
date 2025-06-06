package zhafran.putra.appproject2ckel3.ui_admin

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.UserAdapter
import zhafran.putra.appproject2ckel3.model.User
import zhafran.putra.appproject2ckel3.repository.UserRepository
import zhafran.putra.appproject2ckel3.repository.JadwalRepository
import android.view.View
import androidx.appcompat.widget.SearchView
import java.util.Locale

class UserFragment : Fragment(), UserAdapter.OnInfoClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var searchView: SearchView

    private var userList: MutableList<User> = mutableListOf()
    private var filteredUserList: MutableList<User> = mutableListOf()
    private var jadwalMap: Map<Int, String> = emptyMap()

    private var userRepository: UserRepository? = null
    private var jadwalRepository: JadwalRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRepository = context?.let { UserRepository(it) }
        jadwalRepository = context?.let { JadwalRepository(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        recyclerView = view.findViewById(R.id.rvUser)
        fab = view.findViewById(R.id.fab)
        searchView = view.findViewById(R.id.searchViewUser)

        adapter = UserAdapter(requireContext(), mutableListOf(), this, emptyMap())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        adapter.setOnItemClickListener(object : UserAdapter.OnItemClickListener {
            override fun onItemClick(user: User, anchorView: View) {
                showPopupMenu(user, anchorView)
            }
        })

        fab.setOnClickListener {
            (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditUser(null)
        }

        setupSearchView()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadJadwalAndUsers()
    }

    private fun loadJadwalAndUsers() {
        jadwalRepository?.getAllJadwal { jadwalList ->
            jadwalMap = jadwalList.associate { it.idJadwal to it.kelas }
            userRepository?.getAllUserAuths { auths ->
                userRepository?.getAllUserProfiles { profiles ->
                    val users = mutableListOf<User>()
                    for (auth in auths) {
                        val profile = profiles.find { it.idUser == auth.idUser }
                        val user = User(
                            idUser = auth.idUser,
                            username = auth.username,
                            email = auth.email,
                            password = auth.password,
                            namaLengkap = profile?.namaLengkap ?: "",
                            tanggalLahir = profile?.tanggalLahir,
                            namaOrtu = profile?.namaOrtu,
                            nomorHp = profile?.nomorHp,
                            kelasId = profile?.idJadwal,
                            role = auth.role
                        )
                        users.add(user)
                    }
                    userList = users
                    filteredUserList = users.toMutableList()
                    adapter = UserAdapter(requireContext(), filteredUserList, this, jadwalMap)
                    adapter.setOnItemClickListener(object : UserAdapter.OnItemClickListener {
                        override fun onItemClick(user: User, anchorView: View) {
                            showPopupMenu(user, anchorView)
                        }
                    })
                    recyclerView.adapter = adapter
                }
            }
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUsers(newText)
                return true
            }
        })
    }

    private fun filterUsers(query: String?) {
        if (query.isNullOrEmpty()) {
            filteredUserList = userList.toMutableList()
        } else {
            val lowerCaseQuery = query.lowercase(Locale.getDefault())
            filteredUserList = userList.filter {
                it.namaLengkap?.lowercase(Locale.getDefault())?.contains(lowerCaseQuery) == true
            }.toMutableList()
        }
        adapter.updateList(filteredUserList)
    }

    private fun showPopupMenu(user: User, anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)
        popup.menuInflater.inflate(R.menu.menu_user_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditUser(user.idUser ?: 0)
                    true
                }
                R.id.menu_delete -> {
                    userRepository?.deleteUser(user.idUser ?: 0) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "User deleted", Toast.LENGTH_SHORT).show()
                            loadJadwalAndUsers()
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

    override fun onInfoClick(user: User) {
        val dialog = UserDetailDialogFragment.newInstance(user, jadwalMap)
        dialog.show(parentFragmentManager, "UserDetailDialog")
    }
}
