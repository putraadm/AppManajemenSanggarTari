package zhafran.putra.appproject2ckel3

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import zhafran.putra.appproject2ckel3.viewmodel.LombaViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream

class EditorLombaActivity : AppCompatActivity() {

    private lateinit var imageViewPhoto: ImageView
    private lateinit var btnUploadPhoto: Button
    private lateinit var edJudul: EditText
    private lateinit var edDesc: EditText
    private lateinit var btnSaveLomba: Button

    private var selectedImageBitmap: Bitmap? = null
    private var lombaId: Int? = null

    private lateinit var lombaViewModel: LombaViewModel

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputStream: InputStream? = contentResolver.openInputStream(it)
            selectedImageBitmap = BitmapFactory.decodeStream(inputStream)
            imageViewPhoto.setImageBitmap(selectedImageBitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_lomba)

        imageViewPhoto = findViewById(R.id.imageViewPhoto)
        btnUploadPhoto = findViewById(R.id.btn_uploadPhoto)
        edJudul = findViewById(R.id.edJudul)
        edDesc = findViewById(R.id.edDesc)
        btnSaveLomba = findViewById(R.id.btn_saveLomba)

        lombaViewModel = ViewModelProvider(this).get(LombaViewModel::class.java)

        lombaId = intent.getIntExtra("lomba_id", -1)
        if (lombaId != -1) {
            lombaViewModel.loadLomba(lombaId!!)
        } else {
            lombaId = null
        }

        lombaViewModel.lomba.observe(this) { lomba ->
            lomba?.let {
                it.poster?.let { posterBlob ->
                    val bitmap = BitmapFactory.decodeByteArray(posterBlob, 0, posterBlob.size)
                    imageViewPhoto.setImageBitmap(bitmap)
                    selectedImageBitmap = bitmap
                }
                edJudul.setText(it.judul)
                edDesc.setText(it.deskripsi)
            }
        }

        btnUploadPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSaveLomba.setOnClickListener {
            saveLomba()
        }

        val backBtnLomba = findViewById<ImageButton>(R.id.backBtnLomba)
        backBtnLomba.setOnClickListener {
            finish()
        }
    }

    private fun saveLomba() {
        val judul = edJudul.text.toString().trim()
        val deskripsi = edDesc.text.toString().trim()

        if (judul.isEmpty()) {
            Toast.makeText(this, "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (deskripsi.isEmpty()) {
            Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val posterBytes = selectedImageBitmap?.let { bitmapToByteArray(it) }

        if (lombaId == null) {
            lombaViewModel.insertLomba(posterBytes, judul, deskripsi) { success ->
                if (success) {
                    Toast.makeText(this, "Lomba berhasil disimpan", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menyimpan lomba", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            lombaViewModel.updateLomba(lombaId!!, posterBytes, judul, deskripsi) { success ->
                if (success) {
                    Toast.makeText(this, "Lomba berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, "Gagal memperbarui lomba", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}
