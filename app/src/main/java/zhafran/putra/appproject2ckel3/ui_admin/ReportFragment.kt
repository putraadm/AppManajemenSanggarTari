package zhafran.putra.appproject2ckel3.ui_admin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.ReportAdapter
import zhafran.putra.appproject2ckel3.model.Report
import zhafran.putra.appproject2ckel3.repository.ReportRepository
import zhafran.putra.appproject2ckel3.repository.UserRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ReportFragment : Fragment() {
    private lateinit var fab: FloatingActionButton
    private lateinit var listView: ListView
    private lateinit var adapter: ReportAdapter

    private var reportRepository: ReportRepository? = null
    private var userRepository: UserRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportRepository = context?.let { ReportRepository(it) }
        userRepository = context?.let { UserRepository(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        fab = view.findViewById(R.id.fab)
        listView = view.findViewById(R.id.list_view)

        adapter = ReportAdapter(requireContext(), mutableListOf(), emptyMap(), this::onReportItemClick)
        listView.adapter = adapter

        loadUser()
        loadReport()

        fab.setOnClickListener {
            (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditReport(0)
        }

        return view
    }

    private fun loadUser() {
        userRepository?.getAllUserProfiles { users ->
            val userMap = users.associate { it.idUser to (it.namaLengkap ?: "Unknown") }
            adapter.userMap = userMap
            adapter.notifyDataSetChanged()
        }
    }

    private fun loadReport() {
        reportRepository?.getAllReports { reportList ->
            adapter.updateList(reportList.toMutableList())
        }
    }

    private fun onReportItemClick(view: View, report: Report) {
        if (view.id == R.id.btnPrintPdf) {
            generatePdf(report)
        } else {
            showPopupMenu(view, report)
        }
    }

    private fun generatePdf(report: Report) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint().apply {
            textSize = 12f
            isAntiAlias = true
        }

        val boldPaint = Paint(paint).apply {
            isFakeBoldText = true
        }

        val borderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
        }

        val logo = BitmapFactory.decodeResource(resources, R.drawable.logo_sanggar_gk)
        val logoScaled = Bitmap.createScaledBitmap(logo, 80, 80, true)

        val pageWidth = pageInfo.pageWidth.toFloat()
        val startX = 50f
        var y = 50f

        // Draw logo on top-left
        canvas.drawBitmap(logoScaled, startX, y, paint)

        // Draw title aligned center (adjusted vertically by logo height)
        y += logoScaled.height + 10f
        val title = "Laporan Nilai"
        val titleWidth = paint.measureText(title)
        val titleX = (pageWidth - titleWidth) / 2
        canvas.drawText(title, titleX, y + logoScaled.height / 2f + 5f, boldPaint)

        y += logoScaled.height + 5f

        // Draw report details
        canvas.drawText("Semester: ${report.semester}", startX, y, paint)
        y += 20f
        canvas.drawText("Tanggal : ${report.tanggal}", startX, y, paint)
        y += 20f
        canvas.drawText("Nama    : ${adapter.userMap[report.idUser] ?: "Unknown"}", startX, y, paint)
        y += 30f

        // Table dimensions
        val colKategori = 100f
        val colNilai = 60f
        val colDeskripsi = pageWidth - startX * 2 - colKategori - colNilai
        val rowHeight = 30f

        // Draw table header with border
        canvas.drawRect(startX, y, startX + colKategori + colNilai + colDeskripsi, y + rowHeight, borderPaint)
        canvas.drawLine(startX + colKategori, y, startX + colKategori, y + rowHeight, borderPaint)
        canvas.drawLine(startX + colKategori + colNilai, y, startX + colKategori + colNilai, y + rowHeight, borderPaint)

        canvas.drawText("Kategori", startX + 5f, y + 20f, boldPaint)
        canvas.drawText("Nilai", startX + colKategori + 5f, y + 20f, boldPaint)
        canvas.drawText("Deskripsi", startX + colKategori + colNilai + 5f, y + 20f, boldPaint)
        y += rowHeight

        // Helper to draw a row
        fun drawRow(kategori: String, nilai: String, deskripsi: String) {
            canvas.drawRect(startX, y, startX + colKategori + colNilai + colDeskripsi, y + rowHeight, borderPaint)
            canvas.drawLine(startX + colKategori, y, startX + colKategori, y + rowHeight, borderPaint)
            canvas.drawLine(startX + colKategori + colNilai, y, startX + colKategori + colNilai, y + rowHeight, borderPaint)

            canvas.drawText(kategori, startX + 5f, y + 20f, paint)
            canvas.drawText(nilai, startX + colKategori + 5f, y + 20f, paint)
            canvas.drawText(deskripsi, startX + colKategori + colNilai + 5f, y + 20f, paint)
            y += rowHeight
        }

        drawRow("Wirama", report.wirama, report.deskripsiWirama)
        drawRow("Wirasa", report.wirasa, report.deskripsiWirasa)
        drawRow("Wiraga", report.wiraga, report.deskripsiWiraga)

        // Catatan
        y += 20f
        canvas.drawText("Catatan:", startX, y, boldPaint)
        y += 20f

        val lines = report.catatan.split("\n")
        for (line in lines) {
            canvas.drawText(line, startX, y, paint)
            y += 20f
        }

        document.finishPage(page)

        // Save PDF
        val directory = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val studentName = adapter.userMap[report.idUser] ?: "Unknown"
        val safeName = studentName.replace(Regex("[^a-zA-Z0-9_]"), "_")
        val safeDate = report.tanggal.replace(Regex("[^a-zA-Z0-9_]"), "_")
        val safeSemester = report.semester.replace(Regex("[^a-zA-Z0-9_]"), "_")
        val fileName = "Laporan_${safeName}_${safeDate}_${safeSemester}.pdf"
        val file = File(directory, fileName)

        try {
            document.writeTo(FileOutputStream(file))
            Toast.makeText(requireContext(), "PDF berhasil dibuat: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            openPdf(file)
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Gagal membuat PDF: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            document.close()
        }
    }

    private fun openPdf(file: File) {
        val context = context ?: return
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Tidak ada aplikasi untuk membuka PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPopupMenu(view: View, report: Report) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_report_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditReport(report.idReport)
                    true
                }
                R.id.menu_delete -> {
                    reportRepository?.deleteReport(report.idReport) { success ->
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
