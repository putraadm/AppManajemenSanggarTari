package zhafran.putra.appproject2ckel3.ui_user

import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.ReportAdapter
import zhafran.putra.appproject2ckel3.databinding.FragmentReportUserBinding
import zhafran.putra.appproject2ckel3.model.Report
import zhafran.putra.appproject2ckel3.preferences
import zhafran.putra.appproject2ckel3.viewmodel.ReportViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ReportFragmentUser : Fragment() {
    private var _binding: FragmentReportUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ReportAdapter
    private lateinit var listView: ListView
    private var userId: Int = -1

    private lateinit var preferences: preferences

    private val reportViewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = preferences(requireContext())
        userId = preferences.prefUserId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ReportAdapter(requireContext(), mutableListOf(), emptyMap()) { _, report ->
            exportReportToPdf(report)
        }
        listView = binding.listView
        listView.adapter = adapter

        observeViewModel()

        return root
    }

    private fun observeViewModel() {
        reportViewModel.userMapLiveData.observe(viewLifecycleOwner) { userMap ->
            adapter.userMap = userMap
            adapter.notifyDataSetChanged()
        }
        reportViewModel.reportListLiveData.observe(viewLifecycleOwner) { reportList ->
            adapter.updateList(reportList.toMutableList())
        }
        reportViewModel.loadUserMap()
        loadReports()
    }

    private fun loadReports() {
        if (userId == -1) return
        reportViewModel.loadReports(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun exportReportToPdf(report: Report) {
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

        canvas.drawBitmap(logoScaled, startX, y, paint)

        y += logoScaled.height + 10f
        val title = "Laporan Nilai"
        val titleWidth = boldPaint.measureText(title)
        val titleX = (pageWidth - titleWidth) / 2
        canvas.drawText(title, titleX, y, boldPaint)
        y += 30f

        canvas.drawText("Semester: ${report.semester}", startX, y, paint)
        y += 20f
        canvas.drawText("Tanggal : ${report.tanggal}", startX, y, paint)
        y += 20f
        canvas.drawText("Nama    : ${adapter.userMap[report.idUser] ?: "Unknown"}", startX, y, paint)
        y += 30f

        val colKategori = 100f
        val colNilai = 60f
        val colDeskripsi = pageWidth - startX * 2 - colKategori - colNilai
        val rowHeight = 30f

        canvas.drawRect(startX, y, startX + colKategori + colNilai + colDeskripsi, y + rowHeight, borderPaint)
        canvas.drawLine(startX + colKategori, y, startX + colKategori, y + rowHeight, borderPaint)
        canvas.drawLine(startX + colKategori + colNilai, y, startX + colKategori + colNilai, y + rowHeight, borderPaint)

        canvas.drawText("Kategori", startX + 5f, y + 20f, boldPaint)
        canvas.drawText("Nilai", startX + colKategori + 5f, y + 20f, boldPaint)
        canvas.drawText("Deskripsi", startX + colKategori + colNilai + 5f, y + 20f, boldPaint)
        y += rowHeight

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

        y += 20f
        canvas.drawText("Catatan:", startX, y, boldPaint)
        y += 20f

        val lines = report.catatan.split("\n")
        for (line in lines) {
            canvas.drawText(line, startX, y, paint)
            y += 20f
        }

        document.finishPage(page)

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
        val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Tidak ada aplikasi untuk membuka PDF", Toast.LENGTH_SHORT).show()
        }
    }
}