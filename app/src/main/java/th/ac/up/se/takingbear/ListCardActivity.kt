package th.ac.up.se.takingbear

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_list_card.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import th.ac.up.se.takingbear.Adapter.ChapterAdapter
import th.ac.up.se.takingbear.Adapter.NewLessonAdapter
import th.ac.up.se.takingbear.Adapter.NumberAdapter
import th.ac.up.se.takingbear.Data.Chapter
import th.ac.up.se.takingbear.SQLite.ChapterSQ
import th.ac.up.se.takingbear.Tools.CardAnimation
import th.ac.up.se.takingbear.Tools.FSTool
import th.ac.up.se.thaicardgame.DataArray.Lesson
import th.ac.up.se.thaicardgame.DataArray.Quiz

class ListCardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    lateinit var bundle: Bundle
    var color: Int = 0
    var title: String = ""
    var id: Int = -1
    var colorDark: Int = 0

    lateinit var sqlite :LangSQ

    private lateinit var fullScreen: FSTool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_card)

        fullScreen = FSTool(this.window)
        //supportPostponeEnterTransition()

        sqlite = LangSQ(this)

        bundle = intent.extras
        color = bundle.getInt("COLOR")
        colorDark = bundle.getInt("DARK")
        title = bundle.getString("TITLE")
        id = bundle.getInt("ID")


        list_card_title_A.text = title
        list_card_title_B.text = title

        chapter_card_layout.setBackgroundColor(ContextCompat.getColor(this, color))
        list_card_back_btn.setOnClickListener {
            //supportFinishAfterTransition()
            finish()
        }

        list_popup_layout.visibility = View.GONE


    }


    override fun onStart() {
        super.onStart()

        fullScreen.loadFunction()

        recy()


    }

    fun recy() {

        if (id == 0 || id == 1) {
            recyclerView = QuickRecyclerView(this
                    , list_card_recyclerview
                    , "linear"
                    , 1
                    , "horizontal"
                    , false
                    , "alway"
                    , "high")
                    .recyclerView()
            //adapter = CustomPlanAdapter(this, arrCustom)

            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)

            recyclerView.clipToPadding = false
            recyclerView.setPadding(dpsToPixels(this, 30), dpsToPixels(this, 100), dpsToPixels(this, 10), 0)

            recyclerView.layoutAnimation = CardAnimation(this).horizontalAnimation()



            if (id == 0) {
                var data = if(sqlite.read().contentEquals(LangSQ.THAI)){
                    Lesson().getThaiChapter()
                }else {
                    Lesson().getEngChapter()
                }
                var adapter = ChapterAdapter(this.colorDark, this.color, "LAND", data)
                recyclerView.adapter = adapter
            } else if (id == 1) {
                var data = if(sqlite.read().contentEquals(LangSQ.THAI)){
                    Quiz().getThaiChapter()
                }else {
                    Quiz().getEngChapter()
                }
                var adapter = ChapterAdapter(this.colorDark, this.color, "LAND", data)
                recyclerView.adapter = adapter
            }


        } else if (id == 100) {
            //Log.e("COLOR",colorDark.toString())
            recyclerView = QuickRecyclerView(this
                    , list_card_recyclerview
                    , "grid"
                    , 2
                    , "horizontal"
                    , false
                    , "alway"
                    , "high")
                    .recyclerView()
            //adapter = CustomPlanAdapter(this, arrCustom)

            var position = bundle.getInt("CHAPTER")

            var d = Quiz().getChapter(0, position)

            var sqlite = ChapterSQ(this, ChapterSQ.convertName(position), d.size)
            //Toast.makeText(this,sqlite.getSize().toString(),Toast.LENGTH_SHORT).show()

            //Log.e(d.size.toString(),position.toString())


            //sqlite.deleteAll()
            //sqlite.passedQuiz(3,true)
            //sqlite.clearAll()

            var data = sqlite.getData()


            /*data.forEach {
                Log.e(it.number.toString(),it.passed.toString())
            }*/

            //Toast.makeText(this,sqlite.getSize().toString(),Toast.LENGTH_SHORT).show()
            //Toast.makeText(this,data.size.toString(),Toast.LENGTH_SHORT).show()

            //Log.e("TY",data.size.toString())

            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)

            var adapter = NumberAdapter(this, d, data, color, colorDark, position)
            recyclerView.setPadding(dpsToPixels(this, 30), dpsToPixels(this, 100), dpsToPixels(this, 10), 0)
            recyclerView.clipToPadding = false

            recyclerView.layoutAnimation = CardAnimation(this).fastHorizontalAnimation()

            recyclerView.adapter = adapter
        } else if (id == 101) {
            //Log.e("COLOR",colorDark.toString())
            recyclerView = QuickRecyclerView(this
                    , list_card_recyclerview
                    , "linear"
                    , 1
                    , "horizontal"
                    , false
                    , "alway"
                    , "high")
                    .recyclerView()
            //adapter = CustomPlanAdapter(this, arrCustom)

            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)

            var position = bundle.getInt("CHAPTER")
            var data = Lesson().getChapter(position)

            var w = DeviceUtills(this).getScreenWidth()
            var h = DeviceUtills(this).getScreenHeight()
            var ws = pixelsToDps(this, w)

            var wss = (ws / 2) - 20 - 10

            //var width = w/2 - dpsToPixels(this,40)
            //var height = (width/3)*4

            //var height = h - dpsToPixels(this,120)
            //var width = (height/4)*3

            var x = bundle.getInt("POS")


            var adapter = NewLessonAdapter(sqlite.read(),x, this.colorDark, this.color, "LAND", data)
            recyclerView.setPadding(dpsToPixels(this, 30), dpsToPixels(this, 100), dpsToPixels(this, 10), 0)
            recyclerView.clipToPadding = false

            recyclerView.layoutAnimation = CardAnimation(this).horizontalAnimation()

            recyclerView.adapter = adapter
        }


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
