package zhafran.putra.appproject2ckel3.ui_user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.adapter.AbsensiAdapter
import zhafran.putra.appproject2ckel3.databinding.FragmentAbsensiUserBinding
import zhafran.putra.appproject2ckel3.preferences
import zhafran.putra.appproject2ckel3.viewmodel.AbsensiViewModel

class AbsensiFragmentUser : Fragment() {
    private var _binding: FragmentAbsensiUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AbsensiAdapter
    private lateinit var listView: ListView
    private lateinit var spinnerMonthFilter: Spinner
    private lateinit var btnPrintPdf: android.widget.Button
    private var userId: Int = -1

    private lateinit var preferences: preferences

    private val absensiViewModel: AbsensiViewModel by viewModels()

    private var currentAbsensiList: MutableList<zhafran.putra.appproject2ckel3.model.Absensi> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = preferences(requireContext())
        userId = preferences.prefUserId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAbsensiUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        spinnerMonthFilter = root.findViewById(R.id.spinnerMonthFilter)
        btnPrintPdf = root.findViewById(R.id.btnPrintPdf)

        adapter = AbsensiAdapter(requireContext(), mutableListOf(), emptyMap(), { _, _ -> }, AbsensiAdapter.Mode.USER)
        listView = binding.listView
        listView.adapter = adapter

        setupMonthFilter()
        observeViewModel()

        btnPrintPdf.setOnClickListener {
            generatePdf()
        }

        return root
    }

    private fun setupMonthFilter() {
        val months = listOf("All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val adapterSpinner = android.widget.ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonthFilter.adapter = adapterSpinner

        spinnerMonthFilter.setSelection(0)
        spinnerMonthFilter.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMonth = spinnerMonthFilter.selectedItem.toString()
                val monthNumber = when (selectedMonth) {
                    "January" -> "01"
                    "February" -> "02"
                    "March" -> "03"
                    "April" -> "04"
                    "May" -> "05"
                    "June" -> "06"
                    "July" -> "07"
                    "August" -> "08"
                    "September" -> "09"
                    "October" -> "10"
                    "November" -> "11"
                    "December" -> "12"
                    else -> null
                }
                absensiViewModel.loadAbsensi(userId) {
                    val absensiList = absensiViewModel.absensiListLiveData.value ?: emptyList()
                    val filteredList = if (monthNumber == null) {
                        absensiList
                    } else {
                        absensiList.filter { it.tanggal.substring(5, 7) == monthNumber }
                    }
                    currentAbsensiList = filteredList.toMutableList()
                    adapter.updateList(currentAbsensiList)
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }
    }

    private fun observeViewModel() {
        absensiViewModel.userProfilesLiveData.observe(viewLifecycleOwner) { profiles ->
            val userMap = profiles.associate { it.idUser to (it.namaLengkap ?: "Unknown") }
            adapter.userMap = userMap
        }
        absensiViewModel.absensiListLiveData.observe(viewLifecycleOwner) { absensiList ->
            val filteredList = absensiList.filter { it.idUser == userId }
            adapter.updateList(filteredList.toMutableList())
        }
        absensiViewModel.loadUserProfiles()
        absensiViewModel.loadAbsensi(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generatePdf() {
        val document = android.graphics.pdf.PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val marginLeft = 40
        val marginTop = 40
        val rowHeight = 24
        val cellPadding = 5f

        val titlePaint = android.graphics.Paint().apply {
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
            textSize = 16f
        }

        val tablePaint = android.graphics.Paint().apply {
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = 1f
            color = android.graphics.Color.BLACK
        }
        val textPaint = android.graphics.Paint().apply {
            textSize = 12f
            color = android.graphics.Color.BLACK
        }
        val boldTextPaint = android.graphics.Paint(textPaint).apply {
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }

        var pageNumber = 1
        var y = marginTop

        fun startNewPage(): android.graphics.pdf.PdfDocument.Page {
            val page = document.startPage(android.graphics.pdf.PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
            val canvas = page.canvas

            val bitmap = android.graphics.BitmapFactory.decodeResource(resources, R.drawable.logo_sanggar_gk)
            val scaledBitmap = android.graphics.Bitmap.createScaledBitmap(bitmap, 80, 80 * bitmap.height / bitmap.width, true)
            canvas.drawBitmap(scaledBitmap, marginLeft.toFloat(), y.toFloat(), null)
            y += scaledBitmap.height + 30

            val title = "REKAP ABSENSI"
            canvas.drawText(title, (pageWidth / 2 - titlePaint.measureText(title) / 2), y.toFloat(), titlePaint)
            y += 30

            // Draw user info below title
            val userName = getUserName()
            canvas.drawText("Nama : $userName", marginLeft.toFloat(), y.toFloat(), textPaint)
            y += 20

            return page
        }

        var page = startNewPage()

        val absensiList = adapter.getAbsensiList()

        val headers = listOf("No", "Tanggal", "Status Kehadiran")
        val xStart = marginLeft
        val yStart = y

        val colWidths = listOf(50, 250, 200)
        val totalRows = absensiList.size + 1 // +1 for header
        val tableWidth = colWidths.sum()
        val tableHeight = totalRows * rowHeight

        for (i in 0..totalRows) {
            val lineY = yStart + i * rowHeight
            page.canvas.drawLine(xStart.toFloat(), lineY.toFloat(), (xStart + tableWidth).toFloat(), lineY.toFloat(), tablePaint)
        }

        var xPos = xStart
        for (width in colWidths) {
            page.canvas.drawLine(xPos.toFloat(), yStart.toFloat(), xPos.toFloat(), (yStart + tableHeight).toFloat(), tablePaint)
            xPos += width
        }
        page.canvas.drawLine(xPos.toFloat(), yStart.toFloat(), xPos.toFloat(), (yStart + tableHeight).toFloat(), tablePaint)

        xPos = xStart
        for (i in headers.indices) {
            val text = headers[i]
            val xText = xPos + cellPadding
            val yText = yStart + rowHeight / 2 + 5
            page.canvas.drawText(text, xText, yText.toFloat(), boldTextPaint)
            xPos += colWidths[i]
        }

        var rowIndex = 1
        for (item in absensiList) {
            val status = item.status
            val formattedDate = formatDate(item.tanggal)
            val values = listOf(
                rowIndex.toString(),
                formattedDate,
                status
            )
            xPos = xStart
            for (i in values.indices) {
                val text = values[i]
                val xText = xPos + cellPadding
                val yText = yStart + rowIndex * rowHeight + rowHeight / 2 + 5
                page.canvas.drawText(text, xText, yText.toFloat(), textPaint)
                xPos += colWidths[i]
            }
            rowIndex++
        }

        document.finishPage(page)

        val fileName = "Absensi_${getUserName().replace(" ", "_")}.pdf"
        val file = java.io.File(requireContext().getExternalFilesDir(null), fileName)
        document.writeTo(java.io.FileOutputStream(file))
        document.close()

        android.widget.Toast.makeText(requireContext(), "PDF disimpan: ${file.absolutePath}", android.widget.Toast.LENGTH_LONG).show()

        openPdfFile(file)
    }

    private fun openPdfFile(file: java.io.File) {
        val uri = androidx.core.content.FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
        try {
            startActivity(intent)
        } catch (e: Exception) {
            android.widget.Toast.makeText(requireContext(), "Tidak ada aplikasi untuk membuka PDF", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

     private fun getUserName(): String {
         val userMap = absensiViewModel.userProfilesLiveData.value?.associate { it.idUser to (it.namaLengkap ?: "Unknown") }
         return userMap?.get(userId) ?: "Unknown"
     }

    private fun formatDate(dateString: String?): String {
        if (dateString == null) return ""
        return try {
            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale("id", "ID"))
            val date = inputFormat.parse(dateString)
            val outputFormat = java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale("id", "ID"))
            outputFormat.format(date)
        } catch (e: Exception) {
            dateString
        }
    }
}