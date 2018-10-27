package th.ac.up.se.takingbear

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_quiz.*
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.se.takingbear.Data.QuizCard
import th.ac.up.se.takingbear.SQLite.ChapterSQ
import th.ac.up.se.takingbear.Tools.FSTool
import th.ac.up.se.thaicardgame.DataArray.Quiz

class QuizActivity : AppCompatActivity() {

    lateinit var bundle: Bundle
    var color: Int = 0
    var title: String = ""
    var id: Int = -1
    var colorDark: Int = 0
    private var x: Int = 0
    private var y: Int = 0

    private var position = -1

    private var weight: Int = 0
    private var height: Int = 0

    private lateinit var data: ArrayList<QuizCard>
    lateinit var sq: LangSQ


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        FSTool(window).loadFunction()

        bundle = intent.extras
        color = bundle.getInt("COLOR")
        colorDark = bundle.getInt("DARK")
        x = bundle.getInt("CHAP")
        y = bundle.getInt("POS")

        position = y

        quiz_back.setOnClickListener {
            finish()
        }

        weight = DeviceUtills(this.applicationContext).getScreenWidth()
        height = DeviceUtills(this.applicationContext).getScreenHeight()

        var a = weight / 40
        quiz_message.textSize = a.toFloat()

        data = Quiz().getChapter(0, x)
        var card = data[y]

        sq = LangSQ(this)

        if (sq.read().contentEquals(LangSQ.THAI)) {
            quiz_back_text_a.text = "กลับ"
            quiz_back_text_b.text = "กลับ"
        } else {
            quiz_back_text_a.text = "Back"
            quiz_back_text_b.text = "Back"
        }

        cardToAny(card)


    }

    private fun cardToAny(card: QuizCard) {

        quiz_popup_layout.visibility = View.GONE

        if (card.image != 0) {
            quiz_image_layout.visibility = View.VISIBLE
            Picasso.get().load(card.image).into(quiz_image)
        } else {
            quiz_image_layout.visibility = View.GONE
        }

        if(sq.read().contentEquals(LangSQ.THAI)){
            quiz_message.text = "ข้อที่ ${position + 1}\n\n${card.question}"
        }else {
            quiz_message.text = "No. ${position + 1}\n\n${card.question}"
        }




        quiz_choice_a_text.text = card.choiceA
        quiz_choice_b_text.text = card.choiceB

        quiz_choice_a.setOnClickListener {
            if (card.answer == 0) {
                if (position == data.size - 1) {
                    finishPopup()
                } else {
                    passPopup()
                }
            } else {
                failPopup()
            }
        }

        quiz_choice_b.setOnClickListener {
            if (card.answer == 1) {
                if (position == data.size - 1) {
                    finishPopup()
                } else {
                    passPopup()
                }
            } else {
                failPopup()
            }
        }


    }

    private fun failPopup() {
        if (sq.read().contentEquals(LangSQ.THAI)) {
            popup(0, "ยังไม่ถูกนะ", R.drawable.ic_question_mark, R.color.color_game_blue, "ลองอีกครั้ง")
        }else {
            popup(0, "Incorrect", R.drawable.ic_question_mark, R.color.color_game_blue, "Try again")

        }
    }

    private fun passPopup() {
        if (sq.read().contentEquals(LangSQ.THAI)) {
            popup(1, "ถูกต้อง", R.drawable.ic_star, R.color.colorAmber, "ข้อต่อไป")
        } else {
            popup(1, "Correct", R.drawable.ic_star, R.color.colorAmber, "Next")

        }


    }

    private fun finishPopup() {
        if (sq.read().contentEquals(LangSQ.THAI)) {
            popup(2, "สำเร็จ", R.drawable.trophy, R.color.colorAmber, "รับทราบ")
        } else {
            popup(2, "Congratulations", R.drawable.trophy, R.color.colorAmber, "OK")

        }
    }

    private fun popup(id: Int, title: String, image: Int, color: Int, button: String) {
        quiz_popup_layout.visibility = View.VISIBLE
        //Picasso.get().load(image).into(quiz_popup_image)
        quiz_popup_image.setImageDrawable(ContextCompat.getDrawable(this, image))

        if (id != 2) {
            quiz_popup_image.setColorFilter(ContextCompat.getColor(this, color), android.graphics.PorterDuff.Mode.SRC_IN)
        }

        quiz_popup_title_a.text = title
        quiz_popup_title_b.text = title

        quiz_popup_title_b.setTextColor(ContextCompat.getColor(this, color))

        quiz_popup_text_btn.text = button

        var sqlite = ChapterSQ(this, ChapterSQ.convertName(x), data.size)

        quiz_popup_text_btn.setOnClickListener {
            quiz_popup_layout.visibility = View.GONE

            if (id == 1) {

                sqlite.passedQuiz(position, true)

                position += 1

                cardToAny(data[position])


            } else if (id == 2) {
                sqlite.passedQuiz(position, true)

                finish()
            }
        }


    }

    override fun onResume() {
        super.onResume()

        FSTool(window).loadFunction()
    }

}
