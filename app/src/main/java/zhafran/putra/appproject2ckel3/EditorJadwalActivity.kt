package zhafran.putra.appproject2ckel3

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import zhafran.putra.appproject2ckel3.databinding.ActivityEditorJadwalBinding


class EditorJadwalActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var b : ActivityEditorJadwalBinding

    //    Adapter
    private lateinit var adapData: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEditorJadwalBinding.inflate(layoutInflater)
        setContentView(b.root)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_saveJadwal -> {
            }
        }
    }
}