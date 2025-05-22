package zhafran.putra.appproject2ckel3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.model.Absensi

class AbsensiAdapter(
    private val context: Context,
    private var absensiList: MutableList<Absensi>,
    var userMap: Map<Int, String>,
    private val onItemClick: (View, Absensi) -> Unit,
    private val mode: Mode = Mode.ADMIN
) : BaseAdapter() {

    enum class Mode {
        ADMIN,
        USER
    }

    override fun getCount(): Int = absensiList.size

    override fun getItem(position: Int): Absensi = absensiList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            val layoutRes = when (mode) {
                Mode.ADMIN -> R.layout.item_absensi_detail
                Mode.USER -> R.layout.item_absensi_user
            }
            view = LayoutInflater.from(context).inflate(layoutRes, parent, false)
            holder = ViewHolder(view, mode)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val absensi = getItem(position)
        if (mode == Mode.ADMIN) {
            holder.tvNama?.text = userMap[absensi.idUser] ?: absensi.idUser.toString()
            holder.tvStatus?.text = "Status Kehadiran: ${absensi.status}"
        } else {
            holder.tvTanggal?.text = absensi.tanggal ?: ""
            holder.tvStatusKehadiran?.text = "Status Kehadiran: ${absensi.status}"
        }

        view.setOnClickListener {
            onItemClick(it, absensi)
        }

        return view
    }

    fun updateList(newList: List<Absensi>) {
        absensiList.clear()
        absensiList.addAll(newList)
        notifyDataSetChanged()
    }

    private class ViewHolder(view: View, mode: Mode) {
        val tvNama: TextView? = if (mode == Mode.ADMIN) view.findViewById(R.id.tvNama) else null
        val tvStatus: TextView? = if (mode == Mode.ADMIN) view.findViewById(R.id.tvStatus) else null
        val tvTanggal: TextView? = if (mode == Mode.USER) view.findViewById(R.id.tvTanggal) else null
        val tvStatusKehadiran: TextView? = if (mode == Mode.USER) view.findViewById(R.id.tvStatusKehadiran) else null
    }
}
