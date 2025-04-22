package zhafran.putra.appproject2ckel3.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.model.Lomba

class LombaAdapter(
    private val context: Context,
    private var lombaList: MutableList<Lomba>,
    private val onItemClick: (View, Lomba) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = lombaList.size

    override fun getItem(position: Int): Any = lombaList[position]

    override fun getItemId(position: Int): Long = lombaList[position].idLomba.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_data_lomba, parent, false)
        val lomba = lombaList[position]

        val ivPoster = view.findViewById<ImageView>(R.id.imageView)
        val tvJudul = view.findViewById<TextView>(R.id.tvTitle)
        val tvDeskripsi = view.findViewById<TextView>(R.id.tvDescription)

        tvJudul.text = lomba.judul
        tvDeskripsi.text = lomba.deskripsi
        if (lomba.poster != null) {
            val bitmap = BitmapFactory.decodeByteArray(lomba.poster, 0, lomba.poster.size)
            ivPoster.setImageBitmap(bitmap)
        } else {
            ivPoster.setImageResource(R.drawable.ic_launcher_foreground)
        }

        view.setOnClickListener {
            onItemClick(it, lomba)
        }

        return view
    }

    fun updateList(newList: List<Lomba>) {
        lombaList.clear()
        lombaList.addAll(newList)
        notifyDataSetChanged()
    }
}
