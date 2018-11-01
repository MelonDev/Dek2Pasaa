package th.ac.up.se.takingbear

import android.os.Bundle
import android.util.TypedValue

import kotlinx.android.synthetic.main.activity_lesson.*
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.se.takingbear.Data.LessonCard
import th.ac.up.se.takingbear.Tools.FSTool
import th.ac.up.se.thaicardgame.DataArray.Lesson
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import com.squareup.picasso.Picasso
import android.R.raw
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.util.Util.getUserAgent
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.se.takingbear.Data.WordInfo
import java.util.*


class LessonActivity : AppCompatActivity() {

    private lateinit var bundle: Bundle
    private var color: Int = 0
    private var colorDark: Int = 0
    private var msKey: String = ""
    private var key: String = ""
    private var weight: Int = 0
    private var height: Int = 0

    private var play = false

    private lateinit var data: ArrayList<LessonCard>

    private var position = -1

    private lateinit var media: MediaPlayer
    private var current = 0

    private var start = false

    lateinit var sqlite: LangSQ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        FSTool(window).loadFunction()

        bundle = intent.extras
        color = bundle.getInt("COLOR")
        colorDark = bundle.getInt("DARK")
        msKey = bundle.getString("MSKEY")
        key = bundle.getString("KEY")

        Log.e(msKey, key)

        //position = y

        //data = Lesson().getChapter(x + 1)
        //var card = data[y]

        sqlite = LangSQ(this)
        if (sqlite.read().contentEquals(LangSQ.THAI)) {
            list_card_title_A.text = "กลับ"
            list_card_title_B.text = "กลับ"
        } else {
            list_card_title_A.text = "Back"
            list_card_title_B.text = "Back"
        }

        lesson_layout.setBackgroundColor(ContextCompat.getColor(this, color))

        lesson_back.setOnClickListener {
            finish()
        }

        media = MediaPlayer()

        weight = DeviceUtills(this.applicationContext).getScreenWidth()
        height = DeviceUtills(this.applicationContext).getScreenHeight()

        var a = weight / 40
        lesson_text_A.textSize = a.toFloat()
        lesson_text_B.textSize = a.toFloat()
        lesson_text_C.textSize = a.toFloat()

        FirebaseDatabase.getInstance()
                .reference
                .child("Lessons")
                .child(msKey)
                .child("Words")
                .child(key)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("", "")
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.value != null) {
                            val card = p0.getValue(WordInfo::class.java)!!
                            cardToAny(card)
                        }
                    }
                })

        //cardToAny(card)

        /*
        lesson_forward_btn.setOnClickListener {
            if (position != data.size - 1) {
                position += 1
                cardToAny(data[position])
                play = false
                if (start) {
                    media.stop()
                }

            }
        }

        lesson_backward_btn.setOnClickListener {
            if (position != 0) {
                position -= 1
                cardToAny(data[position])
                play = false
                if (start) {
                    media.stop()
                }
            }
        }*/


    }

    private fun cardToAny(card: WordInfo) {


        if (card.cover.isNotEmpty()) {
            //Picasso.get().load(card.cover).into(lesson_main_image)
            Glide.with(this)
                    .load(card.cover)
                    .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                    Log.e("", "")
                    return false
                }

                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                    Log.e("S", "S")
                    //do something when picture already loaded
                    return false
                }
            })
                    .into(lesson_main_image)


            lesson_main_card_image.visibility = View.VISIBLE

        } else {
            lesson_main_card_image.visibility = View.GONE
        }

        if (card.read.isEmpty()) {
            lesson_text_A.visibility = View.GONE
        } else {
            lesson_text_A.visibility = View.VISIBLE
        }

        /*
        if (position == 0) {
            lesson_backward_btn.mAlpha(true)
            lesson_forward_btn.mAlpha(false)
        } else if (position == data.size - 1) {
            lesson_backward_btn.mAlpha(false)
            lesson_forward_btn.mAlpha(true)
        } else {
            lesson_backward_btn.mAlpha(false)
            lesson_forward_btn.mAlpha(false)

        }
        */


        var sA = SpannableStringBuilder()
        var sB = SpannableStringBuilder()
        var sC = SpannableStringBuilder()

        if (sqlite.read().contentEquals(LangSQ.THAI)) {

            lesson_backward_text.text = "ก่อนหน้า"
            lesson_forward_text.text = "ถัดไป"

            sA = spacialText("คำอ่าน: ", card.read)
            sB = spacialText("ภาษาไทย: ", card.nameThai)
            sC = spacialText("ภาษาอังกฤษ: ", card.nameEng)
        } else {

            lesson_backward_text.text = "Previous"
            lesson_forward_text.text = "Next"

            sA = spacialText("Pronunciation: ", card.read)
            sB = spacialText("Thai: ", card.nameThai)
            sC = spacialText("English: ", card.nameEng)
        }

        lesson_text_A.text = sA
        lesson_text_B.text = sB
        lesson_text_C.text = sC

        if (card.engSound.isEmpty()) {
            lesson_eng_btn.visibility = View.GONE
        } else {
            lesson_eng_btn.visibility = View.VISIBLE
            //audioClick(false, lesson_eng_btn, lesson_eng_btn_image)
        }

        if (card.thaiSound.isEmpty()) {
            lesson_thai_btn.visibility = View.GONE
        } else {
            lesson_thai_btn.visibility = View.VISIBLE
            //audioClick(false, lesson_thai_btn, lesson_thai_btn_image)

        }


        lesson_thai_btn.setOnClickListener {
            //card.audioThai.playSound(lesson_thai_btn, lesson_thai_btn_image)
        }


        lesson_eng_btn.setOnClickListener {
            //card.audioEng.playSound(lesson_eng_btn, lesson_eng_btn_image)
        }

    }

    private fun Int.playSound(cardView: CardView, image: ImageView) {
        if (this != 0) {
            start = true
            if (!play) {
                media = MediaPlayer.create(this@LessonActivity, this)
                media.start()
                play = true
                audioClick(true, cardView, image)


                media.setOnCompletionListener {
                    play = false
                    audioClick(false, cardView, image)
                    //Toast.makeText(this@LessonActivity,"STOP",Toast.LENGTH_SHORT).show()
                }
            } else {
                play = false
                media.stop()

                audioClick(false, lesson_thai_btn, lesson_thai_btn_image)
                audioClick(false, lesson_eng_btn, lesson_eng_btn_image)
            }
        }
    }

    private fun audioClick(action: Boolean, card: CardView, image: ImageView) {

        if (action) {
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause))
            image.setColorFilter(ContextCompat.getColor(this, R.color.colorRedDark), android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorRedDark))
            image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_speaker))
            image.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN)
        }

    }

    private fun CardView.mAlpha(boolean: Boolean) {
        if (boolean) {
            this.alpha = 0.5f
            this.setCardBackgroundColor(ContextCompat.getColor(this@LessonActivity, R.color.colorBlack))
        } else {
            this.alpha = 1f
            this.setCardBackgroundColor(ContextCompat.getColor(this@LessonActivity, R.color.colorWhite))
        }
    }

    private fun SpannableStringBuilder.addSpan(line: String, word: String, size: Double): SpannableStringBuilder {
        if (line.isNotEmpty() && word.isNotEmpty()) {
            this@addSpan.setSpan(RelativeSizeSpan(size.toFloat()), line.indexOf(word), line.indexOf(word) + (word).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return this@addSpan
    }

    private fun spacialText(smallText: String, largeText: String): SpannableStringBuilder {
        val a = "$smallText$largeText"
        val sA = SpannableStringBuilder(a)
        sA.apply {
            addSpan(a, smallText, 0.7)
            addSpan(a, largeText, 1.0)
        }
        return sA
    }

    override fun onResume() {
        super.onResume()

        FSTool(window).loadFunction()
    }

    private fun dpsToPixels(dps: Int): Int {
        val r = this.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }

}
