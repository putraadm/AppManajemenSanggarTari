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
    private val onItemClick: (View, Absensi) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = absensiList.size

    override fun getItem(position: Int): Any = absensiList[position]

    override fun getItemId(position: Int): Long = absensiList[position].idAbsensi.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_data_absensi, parent, false)

        val absensi = absensiList[position]

        val tvTanggal = view.findViewById<TextView>(R.id.tvTanggal)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val tvNama = view.findViewById<TextView>(R.id.tvNama)

        tvTanggal.text = absensi.tanggal
        tvStatus.text = absensi.status
        tvNama.text = absensi.idUser.toString()

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
}
