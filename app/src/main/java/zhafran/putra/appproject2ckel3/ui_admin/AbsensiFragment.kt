package zhafran.putra.appproject2ckel3.ui_admin

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.AbsensiGroupedAdapter
import zhafran.putra.appproject2ckel3.model.Absensi
import zhafran.putra.appproject2ckel3.viewmodel.AbsensiViewModel
import java.io.File
import java.io.FileOutputStream

class AbsensiFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var listView: ListView
    private lateinit var adapter: AbsensiGroupedAdapter
    private lateinit var spinner: Spinner
    private lateinit var btnCetak: Button

    private val absensiViewModel: AbsensiViewModel by viewModels()

    private var userMap: Map<Int, String> = emptyMap()
    private var userIdToKelasId: Map<Int, Int> = emptyMap()
    private var jadwalMap: Map<Int, String> = emptyMap()
    private var absensiGroupedData: List<Pair<String, List<Absensi>>> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_absensi, container, false)

        fab = view.findViewById(R.id.fab)
        listView = view.findViewById(R.id.list_view)
        spinner = view.findViewById(R.id.spinnerRekap)
        btnCetak = view.findViewById(R.id.btnCetakPdf)

        adapter = AbsensiGroupedAdapter(requireContext(), listOf(), emptyMap(), this::showPopupMenu)
        listView.adapter = adapter

        setupSpinner()
        setupObservers()

        fab.setOnClickListener {
            (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditAbsensi(0)
        }

        btnCetak.setOnClickListener {
            val file = generatePdf(absensiGroupedData, userMap, userIdToKelasId, jadwalMap, requireContext())
            openPdfFile(file, requireContext())
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        absensiViewModel.loadUserProfiles()
        absensiViewModel.loadJadwal()
        absensiViewModel.loadAbsensi()
    }

    private fun setupSpinner() {
        val adapterSpinner = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
            listOf("Harian", "Bulanan", "Semesteran"))
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = parent.getItemAtPosition(position).toString()
                when (selected) {
                    "Harian" -> absensiViewModel.setGroupingType(AbsensiViewModel.GroupingType.HARI)
                    "Bulanan" -> absensiViewModel.setGroupingType(AbsensiViewModel.GroupingType.BULAN)
                    "Semesteran" -> absensiViewModel.setGroupingType(AbsensiViewModel.GroupingType.SEMESTER)
                    else -> absensiViewModel.setGroupingType(AbsensiViewModel.GroupingType.HARI)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner.setSelection(0)
    }

    private fun setupObservers() {
        absensiViewModel.groupedAbsensiLiveData.observe(viewLifecycleOwner) { groupedData ->
            absensiGroupedData = groupedData
            updateAdapterData()
        }

        absensiViewModel.userProfilesLiveData.observe(viewLifecycleOwner) { profiles ->
            userMap = profiles.associate { it.idUser to (it.namaLengkap ?: "Unknown") }
            userIdToKelasId = profiles.associate { it.idUser to (it.idJadwal ?: 0) }
            updateAdapterData()
        }

        absensiViewModel.jadwalListLiveData.observe(viewLifecycleOwner) { jadwalList ->
            jadwalMap = jadwalList.associate { it.idJadwal to it.kelas }
            updateAdapterData()
        }
    }

    private fun updateAdapterData() {
        if (absensiGroupedData.isNotEmpty() && userMap.isNotEmpty()) {
            adapter.updateGroupedData(absensiGroupedData)
            adapter = AbsensiGroupedAdapter(requireContext(), absensiGroupedData, userMap, this::showPopupMenu)
            listView.adapter = adapter
        }
    }

    private fun showPopupMenu(view: View, absensi: Absensi) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.menu_absensi_item, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    (activity as? zhafran.putra.appproject2ckel3.AdminActivity)?.openEditAbsensi(absensi.idAbsensi)
                    true
                }
                R.id.menu_delete -> {
                    absensiViewModel.deleteAbsensi(absensi.idAbsensi) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Absensi deleted", Toast.LENGTH_SHORT).show()
                            absensiViewModel.loadAbsensi()
                        } else {
                            Toast.makeText(requireContext(), "Failed to delete absensi", Toast.LENGTH_SHORT).show()
                        }
                    }
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun generatePdf(
        groupedData: List<Pair<String, List<Absensi>>>,
        userMap: Map<Int, String>,
        userIdToKelasId: Map<Int, Int>,
        jadwalMap: Map<Int, String>,
        context: Context
    ): File {
        val document = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val marginLeft = 40
        val marginTop = 40
        val marginBottom = 40
        val rowHeight = 24
        val colWidths = listOf(150, 80, 80, 80, 80)
        val cellPadding = 5f

        val titlePaint = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 16f
        }
        val headerPaint = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 14f
        }
        val tablePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
            color = Color.BLACK
        }
        val textPaint = Paint().apply {
            textSize = 12f
            color = Color.BLACK
        }
        val boldTextPaint = Paint(textPaint).apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        var pageNumber = 1
        var y = marginTop

        fun startNewPage(): PdfDocument.Page {
            val page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
            val canvas = page.canvas

            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_sanggar_gk)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 80, 80 * bitmap.height / bitmap.width, true)
            canvas.drawBitmap(scaledBitmap, marginLeft.toFloat(), y.toFloat(), null)
            y += scaledBitmap.height + 30

            val title = "REKAP ABSENSI"
            canvas.drawText(title, (pageWidth / 2 - titlePaint.measureText(title) / 2), y.toFloat(), titlePaint)
            y += 30

            return page
        }

        var page = startNewPage()

        for ((header, items) in groupedData) {
            val itemsByClass = items.groupBy {
                val kelasId = userIdToKelasId[it.idUser] ?: 0
                jadwalMap[kelasId] ?: "Unknown Class"
            }

            for ((kelas, kelasItems) in itemsByClass) {
                val canvas = page.canvas

                if (y + rowHeight * (kelasItems.size + 4) > pageHeight - marginBottom) {
                    document.finishPage(page)
                    pageNumber++
                    y = marginTop
                    page = startNewPage()
                }

                canvas.drawText("Kelas: $kelas", marginLeft.toFloat(), y.toFloat(), headerPaint)
                y += rowHeight
                canvas.drawText("Tanggal: $header", marginLeft.toFloat(), y.toFloat(), headerPaint)
                y += rowHeight

                val headers = listOf("Nama lengkap", "Hadir", "Sakit", "Izin", "Alpha")
                val xStart = marginLeft
                val yStart = y

                val totalRows = kelasItems.map { it.idUser }.distinct().size + 1 // +1 untuk header
                val tableWidth = colWidths.sum()
                val tableHeight = totalRows * rowHeight

                for (i in 0..totalRows) {
                    val lineY = yStart + i * rowHeight
                    canvas.drawLine(xStart.toFloat(), lineY.toFloat(), (xStart + tableWidth).toFloat(), lineY.toFloat(), tablePaint)
                }

                var xPos = xStart
                for (width in colWidths) {
                    canvas.drawLine(xPos.toFloat(), yStart.toFloat(), xPos.toFloat(), (yStart + tableHeight).toFloat(), tablePaint)
                    xPos += width
                }
                canvas.drawLine(xPos.toFloat(), yStart.toFloat(), xPos.toFloat(), (yStart + tableHeight).toFloat(), tablePaint)

                xPos = xStart
                for (i in headers.indices) {
                    val text = headers[i]
                    val xText = xPos + cellPadding
                    val yText = yStart + rowHeight / 2 + 5
                    canvas.drawText(text, xText, yText.toFloat(), boldTextPaint)
                    xPos += colWidths[i]
                }

                val attendanceMap = mutableMapOf<Int, MutableMap<String, Int>>()
                for (item in kelasItems) {
                    val userId = item.idUser
                    val status = item.status.lowercase()
                    val userAttendance = attendanceMap.getOrPut(userId) {
                        mutableMapOf("hadir" to 0, "sakit" to 0, "izin" to 0, "alpha" to 0)
                    }
                    userAttendance[status] = userAttendance[status]!! + 1
                }

                var rowIndex = 1
                for ((userId, counts) in attendanceMap) {
                    val values = listOf(
                        userMap[userId] ?: "Unknown",
                        counts["hadir"].toString(),
                        counts["sakit"].toString(),
                        counts["izin"].toString(),
                        counts["alpha"].toString()
                    )
                    xPos = xStart
                    for (i in values.indices) {
                        val text = values[i]
                        val xText = xPos + cellPadding
                        val yText = yStart + rowIndex * rowHeight + rowHeight / 2 + 5
                        canvas.drawText(text, xText, yText.toFloat(), textPaint)
                        xPos += colWidths[i]
                    }
                    rowIndex++
                }

                y += tableHeight + rowHeight
            }
        }

        document.finishPage(page)

        val file = File(context.getExternalFilesDir(null), "RekapAbsensi.pdf")
        document.writeTo(FileOutputStream(file))
        document.close()

        Toast.makeText(context, "PDF disimpan: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        return file
    }

    private fun openPdfFile(file: File, context: Context) {
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
}
