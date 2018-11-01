package th.ac.up.se.takingbear

import android.os.Bundle

import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_competition.*
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills

import th.ac.up.se.takingbear.Tools.FSTool

class CompetitionActivity : AppCompatActivity() {

    private var weight: Int = 0
    private var height: Int = 0

    lateinit var bundle: Bundle
    var color: Int = 0
    var title: String = ""
    var id: Int = -1
    var colorDark: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_competition)

        FSTool(window).loadFunction()


        bundle = intent.extras
        color = bundle.getInt("COLOR")
        colorDark = bundle.getInt("DARK")
        title = bundle.getString("TITLE")
        id = bundle.getInt("ID")

        weight = DeviceUtills(this.applicationContext).getScreenWidth()
        height = DeviceUtills(this.applicationContext).getScreenHeight()

        val h = height - dpsToPixels(80)

        //com_card_layout.layoutParams = RelativeLayout.LayoutParams(h, h)
        //com_card_layout.setPadding(0,dpsToPixels(80),0,0)
        val params = RelativeLayout.LayoutParams(h, h)
        params.setMargins(0, dpsToPixels(80), 0, 0)
        com_card_layout.layoutParams = params


        val hs = h - dpsToPixels(60)
        val cp = RelativeLayout.LayoutParams(hs, hs)
        cp.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE)
        com_circle_out.layoutParams = cp

        //com_circle_out.layoutParams = RelativeLayout.LayoutParams(hs, hs)
        com_circle_out.radius = (hs/2).toFloat()

        val hsi = hs - dpsToPixels(20)
        val cpi = RelativeLayout.LayoutParams(hsi, hsi)
        cpi.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE)


        com_circle_in.layoutParams = cpi
        com_circle_in.radius = (hsi/2).toFloat()

        val cst = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        cst.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE)
        cst.setMargins(0,((hsi/40)*1),0,0)
        com_text_score.textSize = (h/6).toFloat()
        com_text_score.layoutParams = cst


        val cs = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        cs.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE)
        cs.setMargins(0,((hsi/20)*11),0,0)
        com_text_score_text.textSize = (h/15).toFloat()
        com_text_score_text.layoutParams = cs

        com_back.setOnClickListener {
            finish()
        }



    }

    override fun onStart() {
        super.onStart()
        FSTool(window).loadFunction()

    }

    private fun dpsToPixels(dps: Int): Int {
        val r = this.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }

}
