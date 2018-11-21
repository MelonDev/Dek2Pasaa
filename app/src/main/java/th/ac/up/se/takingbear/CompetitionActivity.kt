package th.ac.up.se.takingbear

import android.app.Activity
import android.os.Bundle
import android.util.Log

import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.up.se.tkbcontrol.Data.PeopleInfo
import kotlinx.android.synthetic.main.activity_competition.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import th.ac.up.se.takingbear.Adapter.ComAdapter
import th.ac.up.se.takingbear.Tools.CardAnimation

import th.ac.up.se.takingbear.Tools.FSTool
import th.ac.up.se.takingbear.Tools.MelonFirebaseProcess

class CompetitionActivity : AppCompatActivity() {

    private var weight: Int = 0
    private var height: Int = 0

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ComAdapter


    lateinit var bundle: Bundle
    var color: Int = 0
    var title: String = ""
    var id: Int = -1
    var colorDark: Int = 0

    lateinit var sqlite: LangSQ

    lateinit var data: ArrayList<PeopleInfo>
    private lateinit var task: MelonFirebaseProcess

    fun selector(p: PeopleInfo): Int {
        return p.score
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_competition)

        FSTool(window).loadFunction()


        bundle = intent.extras!!
        color = bundle.getInt("COLOR")
        colorDark = bundle.getInt("DARK")
        title = bundle.getString("TITLE")!!
        id = bundle.getInt("ID")

        weight = DeviceUtills(this.applicationContext).getScreenWidth()
        height = DeviceUtills(this.applicationContext).getScreenHeight()

        task = MelonFirebaseProcess()

        /*
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

*/

        data = ArrayList()

        recyclerView = QuickRecyclerView(this
                , com_recyclerview
                , "linear"
                , 1
                , "horizontal"
                , false
                , "alway"
                , "high")
                .recyclerView()

        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)

        recyclerView.clipToPadding = false
        recyclerView.setPadding(dpsToPixels(this, 30), dpsToPixels(this, 100), dpsToPixels(this, 10), 0)

        recyclerView.layoutAnimation = CardAnimation(this).horizontalAnimation()

        adapter = ComAdapter(this, this.colorDark, this.color, "LAND", data)
        recyclerView.adapter = adapter

        com_back.setOnClickListener {
            finish()
        }


        sqlite = LangSQ(this)
        if (sqlite.read().contentEquals(LangSQ.THAI)) {
            com_back_text_a.text = "กลับ"
            com_back_text_b.text = "กลับ"
        } else {
            com_back_text_a.text = "Back"
            com_back_text_b.text = "Back"
        }
        loadData()
        val u = FirebaseAuth.getInstance().currentUser!!.uid.toString()


        FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("History").addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Log.e("", "")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {

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

                        FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("History").child(a).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("", "")
                            }

                            override fun onDataChange(p1: DataSnapshot) {
                                if (p1.value != null) {
                                    score += p1.children.count()

                                    if (count == p0.children.count()) {
                                        if (sqlite.read().contentEquals(LangSQ.THAI)) {
                                            com_score_text.text = "คะแนนของฉัน: ${score}"
                                        } else {
                                            com_score_text.text = "My score: ${score}"
                                        }
                                    }
                                }
                            }
                        })

                    }

                } else {
                    if (sqlite.read().contentEquals(LangSQ.THAI)) {
                        com_score_text.text = "คะแนนของฉัน: 0"
                    } else {
                        com_score_text.text = "My score: 0"
                    }
                }
            }
        })


    }

    fun loadData() {

        task.startLoad()
        showProgress()

        FirebaseDatabase.getInstance().reference
                .child("Peoples").addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("", "")
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.value != null) {

                            if (task.isInActive()) {
                                task.activeNow()
                                loadPeople(p0)
                            } else {
                                task.overActive(p0)
                            }
                        } else {
                            stopProgress()
                            data.clear()
                            adapter.notifyDataSetChanged()
                        }
                    }
                })
    }

    fun loadPeople(dataSnapshot: DataSnapshot) {


        data.clear()

        var coun = 0

        var scoreArr = ArrayList<Int>()

        dataSnapshot.children.forEach {

            coun+=1

            val key = it.key.toString()
            FirebaseDatabase.getInstance().reference
                    .child("Peoples")
                    .child(key)
                    .child("Info")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p3: DatabaseError) {
                            Log.e("", "")
                        }

                        override fun onDataChange(p3: DataSnapshot) {
                            if (p3.value != null) {

                                val profile = p3.getValue(PeopleInfo::class.java)!!


                                //count += 1


                                FirebaseDatabase.getInstance().reference.child("Peoples").child(profile.key).child("History").addValueEventListener(object : ValueEventListener {

                                    override fun onCancelled(p0: DatabaseError) {
                                        Log.e("", "")
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        if (p0.value != null) {

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

                                                //Log.e("P ${profile.key}",count.toString())

                                                FirebaseDatabase.getInstance().reference.child("Peoples").child(profile.key).child("History").child(a).addListenerForSingleValueEvent(object : ValueEventListener {
                                                    override fun onCancelled(p0: DatabaseError) {
                                                        Log.e("", "")
                                                    }

                                                    override fun onDataChange(p1: DataSnapshot) {
                                                        if (p1.value != null) {
                                                            score += p1.children.count()

                                                            //Log.e("CO $coun","Count: ${count} Score:${score.toString()}")
                                                            //Log.e("L ${profile.key}",count.toString())


                                                            if (count == p0.children.count()) {

                                                                //Log.e("F ${profile.key}",count.toString())

                                                                var arr = ArrayList<String>()
                                                                data.forEach {
                                                                    arr.add(it.key)
                                                                }

                                                                val bool: Boolean = profile.key in arr

                                                                if(!bool){
                                                                    if(data.size < 10) {

                                                                        profile.score = score
                                                                        data.add(profile)

                                                                        //data.sortBy { selector(it) }
                                                                        data.sortByDescending { selector(it) }

                                                                        data.forEach {
                                                                            Log.e(it.name,it.score.toString())
                                                                        }

                                                                        adapter.notifyDataSetChanged()
                                                                    }
                                                                }


                                                                val p = task.loadOverActive()

                                                                if(coun == dataSnapshot.children.count()){
                                                                    if (p != null) {
                                                                        //Log.e("TEST","TEST")
                                                                        loadPeople(it!!)
                                                                    } else {
                                                                        stopProgress()
                                                                    }
                                                                }



                                                            }
                                                        }
                                                    }
                                                })

                                            }

                                        } else {

                                            if(data.size < 10){
                                                data.add(profile)

                                                data.sortByDescending { selector(it) }
                                                adapter.notifyDataSetChanged()

                                                val p = task.loadOverActive()

                                                if(coun == dataSnapshot.children.count()){
                                                    if (p != null) {
                                                        //Log.e("TEST","TEST")
                                                        loadPeople(it!!)
                                                    } else {
                                                        stopProgress()
                                                    }
                                                }
                                            }


                                        }
                                    }
                                })


                            }
                        }
                    })

            //Log.e("C",data.size.toString())

        }


    }

    override fun onStart() {
        super.onStart()
        FSTool(window).loadFunction()

    }

    private fun dpsToPixels(activity: Activity, dps: Int): Int {
        val r = activity.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }


    fun showProgress() {
        people_loading_popup_layout.visibility = View.VISIBLE

        if (sqlite.read().contentEquals(LangSQ.THAI)) {
            people_loading_text.text = "กำลังโหลด"
        } else {
            people_loading_text.text = "Loading"
        }

        //Log.e("COLOR",Color.RED.toString())

        //list_loading_text.setTextColor(ContextCompat.getColor(this,color))
    }

    fun stopProgress() {
        people_loading_popup_layout.visibility = View.GONE

    }


}
