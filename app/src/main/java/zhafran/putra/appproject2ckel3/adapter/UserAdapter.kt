package zhafran.putra.appproject2ckel3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.model.User

class UserAdapter(private val context: Context, private var userList: MutableList<User>) : BaseAdapter() {

    override fun getCount(): Int = userList.size

    override fun getItem(position: Int): Any = userList[position]

    override fun getItemId(position: Int): Long = userList[position].idUser.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_data_user, parent, false)

        val user = userList[position]

        val tvName = view.findViewById<TextView>(R.id.fullNameItem)
        val tvUsername = view.findViewById<TextView>(R.id.usernameItem)
        val tvEmail = view.findViewById<TextView>(R.id.emailItem)

        tvName.text = user.namaLengkap
        tvUsername.text = user.username
        tvEmail.text = user.email

        return view
    }

    fun updateList(newList: List<User>) {
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }
}
