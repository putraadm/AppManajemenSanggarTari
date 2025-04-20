package zhafran.putra.appproject2ckel3

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import zhafran.putra.appproject2ckel3.databinding.ActivityEditorAbsensiBinding

class EditorAbsensiActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var b: ActivityEditorAbsensiBinding
    private var nama = ""; private var tanggal = ""; private var status = ""

//    Adapter
    private lateinit var adapData: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEditorAbsensiBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.statusRadioGroup.check(b.radioHadir.id)
        b.statusRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                b.radioHadir.id -> status = "Hadir"
                b.radioSakit.id -> status = "Sakit"
                b.radioIzin.id -> status = "Izin"
                b.radioAlpha.id -> status = "Alpha"
            }
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_saveAbsensi -> {
            }
        }
    }
}
