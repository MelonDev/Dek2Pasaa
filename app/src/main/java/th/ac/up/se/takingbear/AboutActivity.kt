package th.ac.up.se.takingbear

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_about.*
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.se.takingbear.Tools.FSTool

class AboutActivity : AppCompatActivity() {

    lateinit var sqlite: LangSQ


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        FSTool(window).loadFunction()


        about_back.setOnClickListener { finish() }

        sqlite = LangSQ(this)


        if (sqlite.read().contentEquals(LangSQ.THAI)) {
            about_text.text = "ขอบคุณด้วยใจ:\n- ทีมภาษาดี\n- นายชัยวิวัฒน์ กกสันเทียะ\n- อุทยานวิทยาศาสตร์ มหาวิทยาลัยพะเยา\n- กระทรวงวิทยาศาสตร์ และ เทคโนโลยี"
        } else {
            about_text.text = "Acknowledgement:\n- Pasaa-d.com and Team\n- Chaiwiwat Koksantia\n- University of Phayao Science Park (UPSP)\n- TED FUND, Ministry of Science and Technology"
        }


    }

    override fun onStart() {
        super.onStart()
        FSTool(window).loadFunction()

    }

}
