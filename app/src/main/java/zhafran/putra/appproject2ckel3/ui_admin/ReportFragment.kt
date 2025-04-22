package zhafran.putra.appproject2ckel3.ui_admin

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.EditorReportActivity
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.ReportAdapter
import zhafran.putra.appproject2ckel3.model.Report
import zhafran.putra.appproject2ckel3.viewmodel.ReportViewModel

class ReportFragment : Fragment() {
    private lateinit var fab: FloatingActionButton
    private lateinit var listView: ListView
    private lateinit var adapter: ReportAdapter
    private lateinit var reportViewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        fab = view.findViewById(R.id.fab)
        listView = view.findViewById(R.id.list_view)

        adapter = ReportAdapter(requireContext(), mutableListOf(), this::showPopupMenu)
        listView.adapter = adapter

        reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        loadReport()

        fab.setOnClickListener {
            val intent = Intent(requireActivity(), EditorReportActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun loadReport() {
        reportViewModel.getAllReport().observe(viewLifecycleOwner, Observer { reportEntityList ->
            val reportList = reportEntityList.map { entity ->
                Report(
                    idReport = entity.idReport,
                    idUser = entity.idUser,
                    tanggal = entity.tanggal,
                    wirama = entity.wirama,
                    wirasa = entity.wirasa,
                    wiraga = entity.wiraga,
                    catatan = entity.catatan
                )
            }
            adapter.updateList(reportList.toMutableList())
        })
    }

    private fun showPopupMenu(view: View, report: Report) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_report_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(requireActivity(), EditorReportActivity::class.java)
                    intent.putExtra("report_id", report.idReport)
                    startActivity(intent)
                    true
                }
                R.id.menu_delete -> {
                    reportViewModel.deleteReport(report.idReport) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Report deleted", Toast.LENGTH_SHORT).show()
                            loadReport()
                        } else {
                            Toast.makeText(requireContext(), "Failed to delete report", Toast.LENGTH_SHORT).show()
                        }
                    }
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}
