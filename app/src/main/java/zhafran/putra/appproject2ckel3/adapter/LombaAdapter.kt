package zhafran.putra.appproject2ckel3.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.model.Lomba

class LombaAdapter(
    private var lombaList: MutableList<Lomba>,
    private val onItemClick: (View, Lomba) -> Unit
) : RecyclerView.Adapter<LombaAdapter.LombaViewHolder>() {

    inner class LombaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPoster: ImageView = itemView.findViewById(R.id.imageView)
        val tvJudul: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDeskripsi: TextView = itemView.findViewById(R.id.tvDescription)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(itemView, lombaList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LombaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data_lomba, parent, false)
        return LombaViewHolder(view)
    }

    override fun onBindViewHolder(holder: LombaViewHolder, position: Int) {
        val lomba = lombaList[position]
        holder.tvJudul.text = lomba.judul
        holder.tvDeskripsi.text = lomba.deskripsi
        if (lomba.poster != null && lomba.poster.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(lomba.poster)
            holder.ivPoster.setImageBitmap(bitmap)
        } else {
            holder.ivPoster.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    override fun getItemCount(): Int = lombaList.size

    fun updateList(newList: MutableList<Lomba>) {
        lombaList.clear()
        lombaList.addAll(newList)
        notifyDataSetChanged()
    }
}
