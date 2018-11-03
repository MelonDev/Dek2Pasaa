package th.ac.up.se.takingbear

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.ProgressParams
import com.up.se.tkbcontrol.Data.PeopleInfo

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

    lateinit var sq: LangSQ


    private lateinit var waitDialog: DialogFragment


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

    fun selector(p: ChapterCheck): Int {
        return p.number
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_card)
        sq = LangSQ(this)


        fullScreen = FSTool(this.window)
        //supportPostponeEnterTransition()

        sqlite = LangSQ(this)

        firebase = FirebaseDatabase.getInstance().reference

        task = MelonFirebaseProcess()

        dataChapter = ArrayList()
        dataCheck = ArrayList()
        dataLesson = ArrayList()

        dataNumber = ArrayList()


        bundle = intent.extras!!
        color = bundle.getInt("COLOR")
        colorDark = bundle.getInt("DARK")
        title = bundle.getString("TITLE")!!
        id = bundle.getInt("ID")


        list_card_title_A.text = title
        list_card_title_B.text = title

        chapter_card_layout.setBackgroundColor(ContextCompat.getColor(this, color))
        list_card_back_btn.setOnClickListener {
            //supportFinishAfterTransition()
            finish()
        }

        score_card.setCardBackgroundColor(ContextCompat.getColor(this,colorDark))

        list_popup_layout.visibility = View.GONE

        val u = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("History").addValueEventListener(object :ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value != null){

                    /*
                    val profile = p0.getValue(PeopleInfo::class.java)!!

                    if (sq.read().contentEquals(LangSQ.THAI)) {
                        score_text.text = "คะแนนของฉัน: ${profile.score}"
                    } else {
                        score_text.text = "My score: ${profile.score}"
                    }
                    */

                    var score = 0

                    var count = 0

                    p0.children.forEach {

                        val a = it.key.toString()
                        count += 1

                        FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("History").child(a).addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("","")
                            }

                            override fun onDataChange(p1: DataSnapshot) {
                                if(p1.value != null){
                                    score += p1.children.count()

                                    if(count == p0.children.count()){
                                        if (sq.read().contentEquals(LangSQ.THAI)) {
                                            score_text.text = "คะแนนของฉัน: ${score}"
                                        } else {
                                            score_text.text = "My score: ${score}"
                                        }
                                    }
                                }
                            }
                        })

                    }

                } else {
                    score_text.text = "คะแนนของฉัน: 0"

                }
            }
        })

    }


    override fun onStart() {


        super.onStart()

        stopProgress()
        fullScreen.loadFunction()


        recy()


    }

    override fun onRestart() {
        super.onRestart()

        fullScreen.loadFunction()

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

            //showWaitDialog()
            showProgress()


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
                        stopProgress()
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

            adapterNumber = NumberAdapter(this, dataNumber, dataCheck, color, colorDark)
            recyclerView.setPadding(dpsToPixels(this, 30), dpsToPixels(this, 100), dpsToPixels(this, 10), 0)
            recyclerView.clipToPadding = false

            recyclerView.layoutAnimation = CardAnimation(this).fastHorizontalAnimation()

            recyclerView.adapter = adapterNumber


            task.startLoad()

            showProgress()

            val uk = FirebaseAuth.getInstance().currentUser!!.uid

            /*
            FirebaseDatabase.getInstance().reference.child("Peoples").child(uk).child("History").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("", "")
                }

                override fun onDataChange(p2: DataSnapshot) {
                    if (p2.value != null) {
*/
                        firebase.child("Lessons").child(key!!).child("Tests").addListenerForSingleValueEvent(object : ValueEventListener {


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
                                    stopProgress()
                                }
                            }
                        })

/*
                    }
                }
            })

*/
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
            val h = DeviceUtills(this).getScreenHeight()
            var ws = pixelsToDps(this, w)

            val wss = (ws / 2) - 20 - 10

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

            showProgress()




            firebase.child("Lessons").child(key!!).child("Words").addValueEventListener(object : ValueEventListener {


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
                        stopProgress()
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
                            } else {
                                //stopWaitDialog()
                                stopProgress()
                            }

                        }

                    } else {
                        stopProgress()
                    }
                }
            })


        }


    }

    private fun QuizLoadData(dataSnapshot: DataSnapshot) {

        var count = 0

        dataNumber.clear()

        //Log.e("SIZE",dataSnapshot.children.count().toString())

        //Log.e("D",dataSnapshot.children.count().toString())

        dataSnapshot.children.forEach {


            val slot = it.getValue(TestInfo::class.java)!!

            val key = slot.key
            val mkey = slot.masterKey
            //Log.e("KEY",mkey)

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

            //Log.e("KEY2 : ${count}","TESTs " + slot.masterKey)


            if (count == dataSnapshot.children.count()) {

                //Log.e("KEY3","TEST" + slot.masterKey)
                val uk = FirebaseAuth.getInstance().currentUser!!.uid
                //Log.e("UK",uk)

                val a = FirebaseDatabase.getInstance().reference.child("Peoples").child(uk).child("History").child(mkey)

                //Log.e("RES ${mkey}",a.ref.toString())

                a.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("", "")
                    }

                    override fun onDataChange(p1: DataSnapshot) {
                        if (p1.value != null) {


                            var c = 0

                            dataCheck.clear()

                            //Log.e("DSA", p1.children.count().toString())

                            p1.children.forEach { itP1 ->


                                val slot = itP1.getValue(CheckTest::class.java)!!

                                //Log.e("DSAMM ${itP1.key} : ${p1.key}", "T ${slot.key}")


                                val s = ChapterCheck(0, slot.passed, slot.passed)
                                s.key = slot.key

                                dataCheck.add(s)

                                c += 1

                                if (c == p1.children.count()) {
                                    adapterNumber.notifyDataSetChanged()

                                    val p = task.loadOverActive()

                                    //Log.e("DSAX", dataCheck.size.toString())


                                    if (p != null) {
                                        //Log.e("TEST","TEST")
                                        QuizLoadData(itP1!!)
                                    } else {
                                        //stopWaitDialog()
                                        stopProgress()
                                    }

                                }
                            }


                        } else {
                            dataCheck.clear()
                            adapterNumber.notifyDataSetChanged()

                            val p = task.loadOverActive()

                            if (p != null) {
                                //Log.e("TEST","TEST")
                                QuizLoadData(it!!)
                            } else {
                                //stopWaitDialog()
                                stopProgress()
                            }
                        }


                    }
                })


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
                } else {
                    //stopWaitDialog()
                    stopProgress()
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

    fun showWaitDialog() {
        waitDialog = CircleDialog.Builder()
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setProgressText("กำลังโหลด...")
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(supportFragmentManager)

    }

    fun stopWaitDialog() {
        waitDialog.dismiss()
    }

    fun showProgress() {
        list_loading_popup_layout.visibility = View.VISIBLE
        list_loading_text

        if (sq.read().contentEquals(LangSQ.THAI)) {
            list_loading_text.text = "กำลังโหลด"
        } else {
            list_loading_text.text = "Loading"
        }

        //Log.e("COLOR",Color.RED.toString())

        //list_loading_text.setTextColor(ContextCompat.getColor(this,color))
    }

    fun stopProgress() {
        list_loading_popup_layout.visibility = View.GONE

    }


}
