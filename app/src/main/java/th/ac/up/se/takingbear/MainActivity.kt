package th.ac.up.se.takingbear

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import th.ac.up.se.takingbear.Adapter.ListFunctionAdapter
import th.ac.up.se.takingbear.Data.MainFunctionData
import th.ac.up.se.takingbear.Tools.CardAnimation
import th.ac.up.se.takingbear.Tools.FSTool

class MainActivity : AppCompatActivity() {

    private lateinit var fullScreen :FSTool
    private lateinit var recyclerView: RecyclerView
    private lateinit var sqlite : LangSQ
    private lateinit var adapter: ListFunctionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fullScreen = FSTool(this.window)

        sqlite = LangSQ(this)

        val data = ArrayList<MainFunctionData>()
        data.apply {

            //add(MainFunctionData("",-1,-1,-1))
            add(MainFunctionData("บทเรียน","Lessons",R.color.colorRedDark,R.color.color_game_red_dark,0,R.drawable.ic_open_book))
            add(MainFunctionData("ทดสอบ","Tests",R.color.color_game_blue,R.color.color_game_blue_dark,1,R.drawable.ic_school_material))
            add(MainFunctionData("แข่งขัน","Competition",R.color.color_game_green,R.color.color_game_green_dark,2,R.drawable.ic_muscles))
            add(MainFunctionData("ตั้งค่า","Settings",R.color.color_game_black,R.color.color_game_black_dark,3,R.drawable.ic_settings))


        }

        changeLang(sqlite.read())

        recyclerView = QuickRecyclerView(this
                , list_function_recyclerview
                , "linear"
                , 1
                , "horizontal"
                , false
                , "alway"
                , "high")
                .recyclerView()

        adapter = ListFunctionAdapter(this,data)


        main_lang_thai.setOnClickListener {
            sqlite.update(LangSQ.THAI)
            adapter.notifyDataSetChanged()
            changeLang(sqlite.read())

            //Toast.makeText(this,sqlite.read(),Toast.LENGTH_SHORT).show()
        }

        main_lang_eng.setOnClickListener {
            sqlite.update(LangSQ.ENG)
            adapter.notifyDataSetChanged()
            changeLang(sqlite.read())

            //Toast.makeText(this,sqlite.read(),Toast.LENGTH_SHORT).show()

        }

    }

    private fun changeLang(lang:String){

        if(lang.contentEquals(LangSQ.THAI)){
            main_lang_thai.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.thai_flag))
            main_lang_eng.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.eng_flag_dark))

        }else {
            main_lang_thai.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.thai_flag_dark))
            main_lang_eng.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.eng_flag))
        }
    }

    override fun onStart() {
        super.onStart()

        fullScreen.loadFunction()

        if(FirebaseAuth.getInstance().currentUser == null){


            val intent = Intent(this, GoogleSignInActivity::class.java)
            //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

            startActivity(intent)

            finish()
        }


        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)


        recyclerView.setPadding(dpsToPixels(this, 40), dpsToPixels(this, 120), dpsToPixels(this, 20), dpsToPixels(this, 0))

        recyclerView.clipToPadding = false
        recyclerView.layoutAnimation = CardAnimation(this).horizontalAnimation()
        recyclerView.adapter = adapter


    }

    override fun onResume() {
        super.onResume()

        fullScreen.loadFunction()



    }

    private fun pixelsToDps(context: Context, pixels: Int): Int {
        val r = context.resources
        return Math.round(pixels / (r.displayMetrics.densityDpi / 160f))
    }

    private fun dpsToPixels(activity: Activity, dps: Int): Int {
        val r = activity.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }


}
