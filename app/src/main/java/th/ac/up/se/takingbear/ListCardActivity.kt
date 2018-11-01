package th.ac.up.se.takingbear

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_list_card.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import th.ac.up.se.takingbear.Adapter.ChapterAdapter
import th.ac.up.se.takingbear.Adapter.NewLessonAdapter
import th.ac.up.se.takingbear.Adapter.NumberAdapter
import th.ac.up.se.takingbear.Data.*
import th.ac.up.se.takingbear.SQLite.ChapterSQ
import th.ac.up.se.takingbear.Tools.CardAnimation
import th.ac.up.se.takingbear.Tools.FSTool
import th.ac.up.se.takingbear.Tools.MelonFirebaseProcess
import th.ac.up.se.thaicardgame.DataArray.Lesson
import th.ac.up.se.thaicardgame.DataArray.Quiz

class ListCardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    lateinit var bundle: Bundle
    var color: Int = 0
    var title: String = ""
    var id: Int = -1
    var colorDark: Int = 0

    private lateinit var dataChapter: ArrayList<Chapter>
    private lateinit var dataCheck: ArrayList<ChapterCheck>
    private lateinit var dataLesson: ArrayList<WordInfo>

    private lateinit var dataNumber: ArrayList<TestInfo>


    private lateinit var adapterChapter: ChapterAdapter
    private lateinit var adapterNumber: NumberAdapter
    private lateinit var adapterWord: NewLessonAdapter


    private lateinit var firebase: DatabaseReference

    private lateinit var task: MelonFirebaseProcess

    lateinit var sqlite: LangSQ

    private lateinit var fullScreen: FSTool

    fun selector(p: Chapter): Int {
        return p.info.number
    }

    fun selector(p: TestInfo): Int {
        return p.number
    }

    fun selector(p: WordInfo): Int {
        return p.number
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_card)

        fullScreen = FSTool(this.window)
        //supportPostponeEnterTransition()

        sqlite = LangSQ(this)

        firebase = FirebaseDatabase.getInstance().reference

        task = MelonFirebaseProcess()

        dataChapter = ArrayList()
        dataCheck = ArrayList()
        dataLesson = ArrayList()

        dataNumber = ArrayList()


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


            /*
            dataChapter = if (sqlite.read().contentEquals(LangSQ.THAI)) {
                Lesson().getThaiChapter()
            } else {
                Lesson().getEngChapter()
            }*/


            adapterChapter = ChapterAdapter(this.colorDark, this.color, "LAND", dataChapter)
            recyclerView.adapter = adapterChapter

            task.startLoad()


            firebase.child("Lessons").addValueEventListener(object : ValueEventListener {


                override fun onCancelled(p0: DatabaseError) {
                    Log.e("", "")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {


                        if (task.isInActive()) {
                            task.activeNow()
                            ChapterLoadData(p0)
                        } else {
                            task.overActive(p0)
                        }


                    } else {
                        dataChapter.clear()
                    }
                }
            })

            //ChapterLoadData()


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

            var key = bundle.getString("CHAPTER")

            //var d = Quiz().getChapter(0, position)

            //var sqlite = ChapterSQ(this, ChapterSQ.convertName(position), d.size)
            //Toast.makeText(this,sqlite.getSize().toString(),Toast.LENGTH_SHORT).show()

            //Log.e(d.size.toString(),position.toString())


            //sqlite.deleteAll()
            //sqlite.passedQuiz(3,true)
            //sqlite.clearAll()

            //dataCheck = sqlite.getData()


            /*data.forEach {
                Log.e(it.number.toString(),it.passed.toString())
            }*/

            //Toast.makeText(this,sqlite.getSize().toString(),Toast.LENGTH_SHORT).show()
            //Toast.makeText(this,data.size.toString(),Toast.LENGTH_SHORT).show()

            //Log.e("TY",data.size.toString())

            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)

            adapterNumber = NumberAdapter(this, dataNumber, color, colorDark)
            recyclerView.setPadding(dpsToPixels(this, 30), dpsToPixels(this, 100), dpsToPixels(this, 10), 0)
            recyclerView.clipToPadding = false

            recyclerView.layoutAnimation = CardAnimation(this).fastHorizontalAnimation()

            recyclerView.adapter = adapterNumber


            task.startLoad()


            firebase.child("Lessons").child(key).child("Tests").addValueEventListener(object : ValueEventListener {


                override fun onCancelled(p0: DatabaseError) {
                    Log.e("", "")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {


                        if (task.isInActive()) {
                            task.activeNow()
                            QuizLoadData(p0)
                        } else {
                            task.overActive(p0)
                        }


                    } else {
                        dataNumber.clear()
                    }
                }
            })

            //recyclerView.adapter = adapter
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

            var key = bundle.getString("CHAPTER")
            //dataLesson = Lesson().getChapter(position)

            var w = DeviceUtills(this).getScreenWidth()
            var h = DeviceUtills(this).getScreenHeight()
            var ws = pixelsToDps(this, w)

            var wss = (ws / 2) - 20 - 10

            //var width = w/2 - dpsToPixels(this,40)
            //var height = (width/3)*4

            //var height = h - dpsToPixels(this,120)
            //var width = (height/4)*3

            //var x = bundle.getInt("POS")


            adapterWord = NewLessonAdapter(sqlite.read(), this.colorDark, this.color, "LAND", dataLesson)
            recyclerView.setPadding(dpsToPixels(this, 30), dpsToPixels(this, 100), dpsToPixels(this, 10), 0)
            recyclerView.clipToPadding = false

            recyclerView.layoutAnimation = CardAnimation(this).horizontalAnimation()

            recyclerView.adapter = adapterWord

            task.startLoad()


            firebase.child("Lessons").child(key).child("Words").addValueEventListener(object : ValueEventListener {


                override fun onCancelled(p0: DatabaseError) {
                    Log.e("", "")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {


                        if (task.isInActive()) {
                            task.activeNow()
                            WordLoadData(p0)
                        } else {
                            task.overActive(p0)
                        }


                    } else {
                        dataLesson.clear()
                    }
                }
            })


        }


    }

    override fun onResume() {
        super.onResume()

        fullScreen.loadFunction()


    }

    private fun ChapterLoadData(dataSnapshot: DataSnapshot) {

        var count = 0

        dataChapter.clear()

        //Log.e("SIZE",dataSnapshot.children.count().toString())


        dataSnapshot.children.forEach {

            val key = it.key.toString()
            //Log.e("KEY",key)


            firebase.child("Lessons").child(key).child("Info").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("", "")

                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.value != null) {


                        count += 1

                        val info = p0.getValue(LessonInfo::class.java)!!

                        if (sqlite.read().contentEquals(LangSQ.THAI)) {
                            val chapter: Chapter = Chapter(info.nameThai, info.cover)
                            chapter.info = info
                            dataChapter.add(chapter)

                            dataChapter.sortBy { selector(it) }
                            adapterChapter.notifyDataSetChanged()

                            //Log.e("size2",dataChapter.size.toString())

                        } else {
                            val chapter: Chapter = Chapter(info.nameEng, info.cover)
                            chapter.info = info
                            dataChapter.add(chapter)
                            dataChapter.sortBy { selector(it) }

                            adapterChapter.notifyDataSetChanged()

                            //Log.e("size3",dataChapter.size.toString())

                        }

                        dataChapter.sortBy { selector(it) }

                        adapterChapter.notifyDataSetChanged()


                        if (count == dataSnapshot.children.count()) {

                            val p = task.loadOverActive()

                            if (p != null) {
                                //Log.e("TEST","TEST")
                                ChapterLoadData(it!!)
                            }

                        }

                    }
                }
            })


        }


    }

    private fun QuizLoadData(dataSnapshot: DataSnapshot) {

        var count = 0

        dataNumber.clear()

        //Log.e("SIZE",dataSnapshot.children.count().toString())


        dataSnapshot.children.forEach {

            val key = it.key.toString()
            //Log.e("KEY",key)


            count += 1

            val info = it.getValue(TestInfo::class.java)!!

            if (sqlite.read().contentEquals(LangSQ.THAI)) {
                //val chapter: Chapter = Chapter(info.nameThai, info.cover)
                //chapter.info = info
                dataNumber.add(info)
                dataNumber.sortBy { selector(it) }

                adapterNumber.notifyDataSetChanged()

                //Log.e("size2",dataChapter.size.toString())

            } else {
                dataNumber.add(info)
                dataNumber.sortBy { selector(it) }
                adapterNumber.notifyDataSetChanged()

                //Log.e("size3",dataChapter.size.toString())

            }
            dataNumber.sortBy { selector(it) }

            adapterNumber.notifyDataSetChanged()


            if (count == dataSnapshot.children.count()) {

                val p = task.loadOverActive()

                if (p != null) {
                    //Log.e("TEST","TEST")
                    QuizLoadData(it!!)
                }

            }


        }


    }

    private fun WordLoadData(dataSnapshot: DataSnapshot) {

        var count = 0

        dataLesson.clear()

        //Log.e("SIZE",dataSnapshot.children.count().toString())


        dataSnapshot.children.forEach {

            val key = it.key.toString()
            //Log.e("KEY",key)
            count += 1

            val info = it.getValue(WordInfo::class.java)!!
            //Log.e("IMG","S ${info.key}")


            if (sqlite.read().contentEquals(LangSQ.THAI)) {
                //val chapter: Chapter = Chapter(info.nameThai, info.cover)
                //chapter.info = info
                dataLesson.add(info)

                dataLesson.sortBy { selector(it) }
                adapterWord.notifyDataSetChanged()


                //Log.e("size2",dataChapter.size.toString())

            } else {
                dataLesson.add(info)
                dataLesson.sortBy { selector(it) }

                adapterWord.notifyDataSetChanged()

                //Log.e("size3",dataChapter.size.toString())

            }

            dataLesson.sortBy { selector(it) }
            adapterWord.notifyDataSetChanged()


            if (count == dataSnapshot.children.count()) {

                val p = task.loadOverActive()

                if (p != null) {
                    //Log.e("TEST","TEST")
                    WordLoadData(it!!)
                }

            }


        }


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
