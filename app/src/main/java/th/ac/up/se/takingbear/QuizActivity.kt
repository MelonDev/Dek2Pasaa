package th.ac.up.se.takingbear

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.up.se.tkbcontrol.Data.PeopleInfo

import kotlinx.android.synthetic.main.activity_quiz.*
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.se.takingbear.Data.*
import th.ac.up.se.takingbear.SQLite.ChapterSQ
import th.ac.up.se.takingbear.Tools.FSTool
import th.ac.up.se.thaicardgame.DataArray.Quiz

class QuizActivity : AppCompatActivity() {

    lateinit var bundle: Bundle
    var color: Int = 0
    var title: String = ""
    var id: Int = -1
    var colorDark: Int = 0
    private var masterKey: String = ""
    private var key: String = ""

    private var position = -1

    private var weight: Int = 0
    private var height: Int = 0

    private lateinit var data: ArrayList<TestInfo>
    lateinit var sq: LangSQ


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        FSTool(window).loadFunction()

        bundle = intent.extras!!
        color = bundle.getInt("COLOR")
        colorDark = bundle.getInt("DARK")
        masterKey = bundle.getString("MSKEY")

        //key = bundle.getString("KEY")
        position = bundle.getInt("POSITION")

        //position = y

        quiz_back.setOnClickListener {
            finish()
        }

        weight = DeviceUtills(this.applicationContext).getScreenWidth()
        height = DeviceUtills(this.applicationContext).getScreenHeight()

        var a = weight / 40
        quiz_message.textSize = a.toFloat()

        //data = Quiz().getChapter(0, x)
        //var card = data[y]
        data = ArrayList()

        sq = LangSQ(this)

        if (sq.read().contentEquals(LangSQ.THAI)) {
            quiz_back_text_a.text = "กลับ"
            quiz_back_text_b.text = "กลับ"
        } else {
            quiz_back_text_a.text = "Back"
            quiz_back_text_b.text = "Back"
        }


        FirebaseDatabase.getInstance().reference.child("Lessons").child(masterKey).child("Tests").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("", "")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {

                    data.clear()

                    var count = 0

                    p0.children.forEach {

                        val test = it.getValue(TestInfo::class.java)!!
                        data.add(test)
                        //cardToAny(test)

                        count += 1

                        if (count == p0.children.count()) {

                            data.sortBy { selector(it) }

                            Log.e("NAME","M ${data[position].key}")

                            cardToAny(data[position])

                        }

                    }


                } else {
                    data.clear()
                }
            }
        })


    }


    fun selector(p: TestInfo): Int {
        return p.number
    }

    private fun cardToAny(card: TestInfo) {

        quiz_popup_layout.visibility = View.GONE


        if (card.cover.isNotEmpty()) {
            quiz_image_layout.visibility = View.VISIBLE
            //Picasso.get().load(card.cover).into(quiz_image)
            Glide.with(this).load(card.cover).into(quiz_image)
        } else {
            quiz_image_layout.visibility = View.GONE
        }

        if (sq.read().contentEquals(LangSQ.THAI)) {
            //quiz_message.text = "ข้อที่ ${position + 1}\n\n${card.question}"
            quiz_message.text = "ข้อที่ ${card.number}\n\n${card.quesThai}"

        } else {
            //quiz_message.text = "No. ${position + 1}\n\n${card.question}"
            quiz_message.text = "No. ${card.number}\n\n${card.quesEng}"

        }




        quiz_choice_a_text.text = card.ansOne
        quiz_choice_b_text.text = card.ansTwo

        quiz_choice_a.setOnClickListener {
            if (card.answer == 0) {
                showWaiting()
                if (position == data.size - 1) {
                    //finishPopup()
                    addScore(1)
                } else {
                    //passPopup()
                    addScore(0)

                }
            } else {
                failPopup()
            }

        }

        quiz_choice_b.setOnClickListener {
            if (card.answer == 1) {
                showWaiting()
                if (position == data.size - 1) {
                    //finishPopup()
                    addScore(1)
                } else {
                    //passPopup()
                    addScore(0)

                }
            } else {
                failPopup()
            }
        }


    }


    private fun failPopup() {
        if (sq.read().contentEquals(LangSQ.THAI)) {
            popup(0, "ยังไม่ถูกนะ", R.drawable.ic_question_mark, R.color.color_game_blue, "ลองอีกครั้ง")
        } else {
            popup(0, "Incorrect", R.drawable.ic_question_mark, R.color.color_game_blue, "Try again")

        }
    }

    private fun passPopup() {

        //Log.e("POPUP","PASSED")


        //val postValues = HashMap<String, Any>()

        val save1 = CheckTest()
        save1.apply {
            this.passed = true
            this.opened = true
            this.key = data[position].key
        }

        val u = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        /*postValues.put("/Peoples/$u/History/${data[position]}", save1)

        val save2 = CheckTest()
        save2.apply {
            this.passed = false
            this.opened = true
            this.key = data[position + 1].key
        }

        postValues.put("/Peoples/$u/History/${data[position + 1]}", save2)
*/
        FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("History").child(data[position].masterKey).child(data[position].key).setValue(save1).addOnSuccessListener {
            if (sq.read().contentEquals(LangSQ.THAI)) {
                popup(1, "ถูกต้อง", R.drawable.ic_star, R.color.colorAmber, "ข้อต่อไป")
            } else {
                popup(1, "Correct", R.drawable.ic_star, R.color.colorAmber, "Next")

            }
        }


    }

    private fun finishPopup() {

        //Log.e("POPUP","FINISH")

        //val postValues = HashMap<String, Any>()

        val save1 = CheckTest()
        save1.apply {
            this.passed = true
            this.opened = true
            this.key = data[position].key
        }

        val u = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        //postValues.put("/${data[position]}", save1)


        FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("History").child(data[position].masterKey).child(data[position].key).setValue(save1).addOnSuccessListener {
            if (sq.read().contentEquals(LangSQ.THAI)) {
                popup(2, "สำเร็จ", R.drawable.trophy, R.color.colorAmber, "รับทราบ")
            } else {
                popup(2, "Congratulations", R.drawable.trophy, R.color.colorAmber, "OK")

            }

        }




    }

    private fun addScore(id :Int){
        val u = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("Info").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value != null){

                    /*
                    val profile = p0.getValue(PeopleInfo::class.java)!!

                    profile.score = profile.score + 1

                    FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("Info").setValue(profile).addOnSuccessListener {
                        if(id == 0){
                            passPopup()
                        }else if(id == 1){
                            finishPopup()
                        }
                    }
                    */

                    if(id == 0){
                        passPopup()
                    }else if(id == 1){
                        finishPopup()
                    }

                }
            }
        })
    }


    private fun popup(id: Int, title: String, image: Int, color: Int, button: String) {
        stopWaiting()

        quiz_popup_layout.visibility = View.VISIBLE
        //Picasso.get().load(image).into(quiz_popup_image)
        quiz_popup_image.setImageDrawable(ContextCompat.getDrawable(this, image))

        /*
        if (id != 2) {
            quiz_popup_image.setColorFilter(ContextCompat.getColor(this, color), android.graphics.PorterDuff.Mode.SRC_IN)
        }
        */

        quiz_popup_image.setColorFilter(ContextCompat.getColor(this, color), android.graphics.PorterDuff.Mode.SRC_IN)


        quiz_popup_title_a.text = title
        quiz_popup_title_b.text = title

        quiz_popup_title_b.setTextColor(ContextCompat.getColor(this, color))

        quiz_popup_text_btn.text = button

        //var sqlite = ChapterSQ(this, ChapterSQ.convertName(x), data.size)

        quiz_popup_text_btn.setOnClickListener {
            quiz_popup_layout.visibility = View.GONE

            if (id == 1) {

                //sqlite.passedQuiz(position, true)

                position += 1
                cardToAny(data[position])


            } else if (id == 2) {
                //sqlite.passedQuiz(position, true)

                finish()
            }
        }


    }

    private fun showWaiting(){
        if (sq.read().contentEquals(LangSQ.THAI)) {
            quiz_loading_text.text = "กำลังตรวจ"
        } else {
            quiz_loading_text.text = "Checking answers"
        }
        quiz_loading_popup_layout.visibility = View.VISIBLE
    }

    private fun stopWaiting(){
        quiz_loading_popup_layout.visibility = View.GONE
    }


    override fun onResume() {
        super.onResume()

        FSTool(window).loadFunction()
    }

}
