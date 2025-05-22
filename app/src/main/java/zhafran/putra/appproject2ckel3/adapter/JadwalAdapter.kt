package zhafran.putra.appproject2ckel3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.model.Jadwal

class JadwalAdapter(
    private var jadwalList: MutableList<Jadwal>,
    private val onItemClick: (View, Jadwal) -> Unit
) : RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder>() {

    inner class JadwalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvKelas: TextView = itemView.findViewById(R.id.tvKelas)
        val tvHari: TextView = itemView.findViewById(R.id.tvHari)
        val tvJam: TextView = itemView.findViewById(R.id.tvJam)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(itemView, jadwalList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data_jadwal_kelas, parent, false)
        return JadwalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalViewHolder, position: Int) {
        val jadwal = jadwalList[position]
        holder.tvKelas.text = jadwal.kelas
        holder.tvHari.text = jadwal.hari
        holder.tvJam.text = jadwal.jam
    }

    override fun getItemCount(): Int = jadwalList.size

    fun updateList(newList: MutableList<Jadwal>) {
        jadwalList.clear()
        jadwalList.addAll(newList)
        notifyDataSetChanged()
    }
}
