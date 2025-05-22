package zhafran.putra.appproject2ckel3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.model.Report

class ReportAdapter(
    private val context: Context,
    private var reportList: MutableList<Report>,
    var userMap: Map<Int, String>,
    private val onItemClick: (View, Report) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = reportList.size

    override fun getItem(position: Int): Any = reportList[position]

    override fun getItemId(position: Int): Long = reportList[position].idReport.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_data_report, parent, false)
        val report = reportList[position]

        val tvTitle = view.findViewById<TextView>(R.id.titleTextView)
        val tvNama = view.findViewById<TextView>(R.id.tvNama)
        val tvTanggal = view.findViewById<TextView>(R.id.dateTextView)
        val tvWirama = view.findViewById<TextView>(R.id.tvWirama)
        val tvWirasa = view.findViewById<TextView>(R.id.tvWirasa)
        val tvWiraga = view.findViewById<TextView>(R.id.tvWiraga)
        val tvCatatan = view.findViewById<TextView>(R.id.tvCatatan)
        val tvSemester = view.findViewById<TextView>(R.id.tvSemester)
        val btnPrintPdf = view.findViewById<android.widget.Button>(R.id.btnPrintPdf)

        val userName = userMap[report.idUser] ?: "Unknown User"
        tvTitle.text = "Laporan Nilai"
        tvTanggal.text = report.tanggal
        tvNama.text = "Nama: $userName"
        tvWirama.text = "Wirama: ${report.wirama}"
        tvWirasa.text = "Wirasa: ${report.wirasa}"
        tvWiraga.text = "Wiraga: ${report.wiraga}"
        tvCatatan.text = "Catatan: ${report.catatan}"
        tvSemester.text = "Semester: ${report.semester}"

        btnPrintPdf.setOnClickListener {
            onItemClick(it, report)
        }

        view.setOnClickListener {
            onItemClick(it, report)
        }

        return view
    }

    fun updateList(newList: List<Report>) {
        reportList.clear()
        reportList.addAll(newList)
        notifyDataSetChanged()
    }
}
