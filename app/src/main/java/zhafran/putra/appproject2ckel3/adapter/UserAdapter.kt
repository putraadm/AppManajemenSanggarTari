package zhafran.putra.appproject2ckel3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.model.User
import android.widget.ImageView

class UserAdapter(private val context: Context, private var userList: MutableList<User>, private val infoClickListener: OnInfoClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface OnInfoClickListener {
        fun onInfoClick(user: User)
    }

    interface OnItemClickListener {
        fun onItemClick(user: User, anchorView: View)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.fullNameItem)
        val tvUsername: TextView = itemView.findViewById(R.id.usernameItem)
        val tvIdKelas: TextView = itemView.findViewById(R.id.idKelasItem)
        val ivInfo: ImageView = itemView.findViewById(R.id.ivInfo)

        init {
            ivInfo.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    infoClickListener.onInfoClick(userList[position])
                }
            }
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(userList[position], it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_data_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvName.text = user.namaLengkap
        holder.tvUsername.text = user.username
        holder.tvIdKelas.text = user.kelasId?.toString() ?: "-"
    }

    override fun getItemCount(): Int = userList.size

    fun updateList(newList: List<User>) {
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }
}
