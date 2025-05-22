package zhafran.putra.appproject2ckel3.ui_user

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.button.MaterialButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import zhafran.putra.appproject2ckel3.R
import zhafran.putra.appproject2ckel3.databinding.FragmentHomeUserBinding
import zhafran.putra.appproject2ckel3.preferences
import zhafran.putra.appproject2ckel3.viewmodel.UserViewModel

class HomeFragmentUser : Fragment() {

    private var _binding: FragmentHomeUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferences: preferences

    private val userViewModel: UserViewModel by viewModels()

    private var currentUserId: Int = -1
    private var currentUserName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = preferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tvWelcome = root.findViewById<TextView>(R.id.tvWelcomeMessageUser)
        val btnGenerateQrCode = root.findViewById<MaterialButton>(R.id.btnGenerateQrCode)

        val userId = preferences.prefUserId
        if (userId != -1) {
            currentUserId = userId
            userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    currentUserName = user.namaLengkap.toString()
                    tvWelcome.text = "Welcome to Home Page, ${user.namaLengkap}"
                } else {
                    tvWelcome.text = "Welcome to Home Page"
                }
            }
            userViewModel.loadUser(userId)
        } else {
            tvWelcome.text = "Welcome to Home Page"
        }

        btnGenerateQrCode.setOnClickListener {
            if (currentUserId != -1 && currentUserName.isNotEmpty()) {
                val qrData = "id:$currentUserId;name:$currentUserName"
                val bitmap = generateQrCode(qrData)
                if (bitmap != null) {
                    showQrCodeDialog(bitmap)
                }
            }
        }

        return root
    }

    private fun generateQrCode(data: String): Bitmap? {
        return try {
            val multiFormatWriter = MultiFormatWriter()
            val bitMatrix: BitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 400, 400)
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.createBitmap(bitMatrix)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showQrCodeDialog(bitmap: Bitmap) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Your QR Code")
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .setView(android.widget.ImageView(requireContext()).apply {
                setImageBitmap(bitmap)
                adjustViewBounds = true
            })
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
