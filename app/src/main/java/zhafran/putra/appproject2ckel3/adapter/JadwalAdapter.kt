package zhafran.putra.appproject2ckel3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.model.Jadwal

class JadwalAdapter(
    private val context: Context,
    private var jadwalList: MutableList<Jadwal>,
    private val onItemClick: (View, Jadwal) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = jadwalList.size

    override fun getItem(position: Int): Any = jadwalList[position]

    override fun getItemId(position: Int): Long = jadwalList[position].id_jadwal.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_data_jadwal_kelas, parent, false)
        val jadwal = jadwalList[position]

        val tvKelas = view.findViewById<TextView>(R.id.tvKelas)
        val tvHari = view.findViewById<TextView>(R.id.tvHari)
        val tvJam = view.findViewById<TextView>(R.id.tvJam)

        tvKelas.text = jadwal.kelas
        tvHari.text = jadwal.hari
        tvJam.text = jadwal.jam

        view.setOnClickListener {
            onItemClick(it, jadwal)
        }

        return view
    }

    fun updateList(newList: List<Jadwal>) {
        jadwalList.clear()
        jadwalList.addAll(newList)
        notifyDataSetChanged()
    }
}
