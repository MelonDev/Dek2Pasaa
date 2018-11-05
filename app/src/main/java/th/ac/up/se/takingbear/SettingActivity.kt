package th.ac.up.se.takingbear

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth
import com.up.se.tkbcontrol.Data.PeopleInfo

import kotlinx.android.synthetic.main.activity_setting.*
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.se.takingbear.Tools.FSTool
import th.ac.up.se.takingbear.Tools.MelonFirebaseProcess

class SettingActivity : AppCompatActivity() {

    private var weight: Int = 0
    private var height: Int = 0

    lateinit var bundle: Bundle
    var color: Int = 0
    var title: String = ""
    var colorDark: Int = 0


    lateinit var sqlite: LangSQ

    lateinit var data: ArrayList<PeopleInfo>
    private lateinit var task: MelonFirebaseProcess

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        FSTool(window).loadFunction()

        weight = DeviceUtills(this.applicationContext).getScreenWidth()
        height = DeviceUtills(this.applicationContext).getScreenHeight()

        task = MelonFirebaseProcess()

        sqlite = LangSQ(this)

        bundle = intent.extras!!
        color = bundle.getInt("COLOR")
        colorDark = bundle.getInt("DARK")
        title = bundle.getString("TITLE")!!

        setting_back.setOnClickListener {
            finish()
        }

        if (sqlite.read().contentEquals(LangSQ.THAI)) {
            setting_back_text_a.text = "กลับ"
            setting_back_text_b.text = "กลับ"

            setting_a1_text.text = "เปลี่ยนรูปภาพประจำตัว"
            setting_a2_text.text = "เปลี่ยนชื่อ"
            setting_a3_text.text = "ติดต่อผู้ดูแล"
            setting_a4_text.text = "เกี่ยวกับ"
            setting_a5_text.text = "ลงชื่อออก"


        } else {
            setting_back_text_a.text = "Back"
            setting_back_text_b.text = "Back"

            setting_a1_text.text = "Change image profile"
            setting_a2_text.text = "Change name"
            setting_a3_text.text = "Contact to admin"
            setting_a4_text.text = "About"
            setting_a5_text.text = "Sign out"
        }

        setting_a3_layout.setOnClickListener {
            //https://m.me/LanguageandApplication


        }

        setting_a5_layout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        FSTool(window).loadFunction()

    }

}
