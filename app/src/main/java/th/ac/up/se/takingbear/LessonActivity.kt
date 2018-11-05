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
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
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
import kotlin.collections.ArrayList


class LessonActivity : AppCompatActivity(){

    private lateinit var bundle: Bundle
    private var color: Int = 0
    private var colorDark: Int = 0
    private var msKey: String = ""
    private var key: String = ""
    private var weight: Int = 0
    private var height: Int = 0

    private var play = false

    private lateinit var data: ArrayList<LessonCard>
    private lateinit var dataWord: ArrayList<WordInfo>


    private var position = -1

    private lateinit var media: MediaPlayer
    private var current = 0

    private var start = false

    private var pre = ""
    private var post = ""

    private var currentPosition = 0

    var mediaPlayer: MediaPlayer? = null


    lateinit var sqlite: LangSQ

    var thai :String = ""
    var eng :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        FSTool(window).loadFunction()

        bundle = intent.extras!!
        color = bundle.getInt("COLOR")
        colorDark = bundle.getInt("DARK")
        msKey = bundle.getString("MSKEY")!!
        key = bundle.getString("KEY")!!

        pre = bundle.getString("PRE")!!
        post = bundle.getString("POST")!!


        //Log.e(pre, post)
        //Log.e("KEY",key)

        dataWord = ArrayList()

        lesson_popup_layout.visibility = View.GONE


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

        onload()

        //cardToAny(card)


        lesson_eng_mic_btn.setOnClickListener {
            callVoiceRecognition()
        }

        lesson_thai_mic_btn.setOnClickListener {
            callThaiVoiceRecognition()
        }

        lesson_forward_btn.setOnClickListener {
            takePost()
            /*
            if (position != data.size - 1) {
                position += 1
                cardToAny(data[position])
                play = false
                if (start) {
                    media.stop()
                }

            }
            */
        }

        lesson_backward_btn.setOnClickListener {
            takePre()
            /*
            if (position != 0) {

                position -= 1
                cardToAny(data[position])
                play = false
                if (start) {
                    media.stop()
                }
            }*/
        }


    }

    private fun onload() {
        dataWord.clear()

        FirebaseDatabase.getInstance()
                .reference
                .child("Lessons")
                .child(msKey)
                .child("Words")
                //.child(key)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("", "")
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.value != null) {
                            //val card = p0.getValue(WordInfo::class.java)!!
                            //cardToAny(card)

                            var count = 0

                            //Log.e("SAD","sss ${p0.children.count()}")


                            p0.children.forEach {
                                val card = it.getValue(WordInfo::class.java)!!
                                dataWord.add(card)

                                count += 1

                                if (count == p0.children.count()) {

                                    dataWord.sortBy { selector(it) }

                                    checkPosition(true)

                                }
                            }

                        }
                    }
                })
    }

    private fun takePre() {
        if (currentPosition > 0 && currentPosition < dataWord.size) {

            //Log.e("POS1",currentPosition.toString())


            currentPosition -= 1

            checkPosition(false)
        }
    }

    private fun takePost() {
        if (currentPosition >= 0 && currentPosition < dataWord.size - 1) {

            //Log.e("POS2",currentPosition.toString())

            currentPosition += 1

            checkPosition(false)
        }
    }

    private fun checkPosition(toPos: Boolean) {

        dataWord.forEach {

            if (it.key == this.key) {
                val position = it.number - 1

                if (toPos) {
                    currentPosition = position
                }

                Log.e("POS", currentPosition.toString())


                if (currentPosition > 0 && currentPosition < (dataWord.size - 1)) {
                    pre = dataWord[currentPosition - 1].key
                    post = dataWord[currentPosition + 1].key
                } else if (currentPosition > 0 && currentPosition == (dataWord.size - 1)) {
                    pre = dataWord[currentPosition - 1].key
                    post = "NULL"
                } else if (currentPosition == 0 && currentPosition < (dataWord.size - 1)) {
                    pre = "NULL"
                    post = dataWord[currentPosition + 1].key
                } else {
                    pre = "NULL"
                    post = "NULL"
                }

                //Log.e(pre,post)

                cardToAny(dataWord[currentPosition])
            } else {
                //Log.e("TEST","TEST")
            }

        }

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



        if (dataWord.size == 1) {
            lesson_backward_btn.mAlpha(true)
            lesson_forward_btn.mAlpha(true)
        } else {
            if (currentPosition == 0) {
                lesson_backward_btn.mAlpha(true)
                lesson_forward_btn.mAlpha(false)
            } else if (currentPosition == dataWord.size - 1) {
                lesson_backward_btn.mAlpha(false)
                lesson_forward_btn.mAlpha(true)
            } else {
                lesson_backward_btn.mAlpha(false)
                lesson_forward_btn.mAlpha(false)

            }
        }


        var sA = SpannableStringBuilder()
        var sB = SpannableStringBuilder()
        var sC = SpannableStringBuilder()

        thai = card.nameThai
        eng = card.nameEng

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

        lesson_eng_btn_progress.visibility = View.GONE
        lesson_thai_btn_progress.visibility = View.GONE


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
            dataWord[currentPosition].thaiSound.playSound(lesson_thai_btn, lesson_thai_btn_image,lesson_thai_btn_progress)
        }

        lesson_eng_btn.setOnClickListener {
            dataWord[currentPosition].engSound.playSound(lesson_eng_btn, lesson_eng_btn_image,lesson_eng_btn_progress)
        }

        /*
    lesson_thai_btn.setOnClickListener
    {
        //card.audioThai.playSound(lesson_thai_btn, lesson_thai_btn_image)
    }


    lesson_eng_btn.setOnClickListener
    {
        //card.audioEng.playSound(lesson_eng_btn, lesson_eng_btn_image)
    }
*/

    }

    private fun String.playSound(cardView: CardView, image: ImageView,progress: ProgressBar) {

        if (this.isNotEmpty()) {
            //progress.visibility = View.VISIBLE
            start = true
            if (!play) {
                //media = MediaPlayer.create(this@LessonActivity, this)
                //media.start()
                play = true
                audioClick(true, cardView, image,progress)

                mediaPlayer = MediaPlayer()

                mediaPlayer!!.setDataSource(this)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()


                mediaPlayer!!.setOnCompletionListener {

                    mediaPlayer!!.stop()
                    mediaPlayer = null

                    play = false
                    audioClick(false, cardView, image,progress)

                }


            } else {
                play = false
                mediaPlayer!!.stop()
                mediaPlayer = null

                audioClick(false, lesson_thai_btn, lesson_thai_btn_image,lesson_thai_btn_progress)
                audioClick(false, lesson_eng_btn, lesson_eng_btn_image,lesson_eng_btn_progress)
            }
        }
    }

    private fun audioClick(action: Boolean, card: CardView, image: ImageView,progress :ProgressBar) {

        if (action) {
            progress.visibility = View.GONE
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
            image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause))
            image.setColorFilter(ContextCompat.getColor(this, R.color.colorRedDark), android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            progress.visibility = View.GONE
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
        val a = "$smallText\n$largeText"
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

    fun selector(p: WordInfo): Int {
        return p.number
    }


    private fun dpsToPixels(dps: Int): Int {
        val r = this.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }

    companion object {
        const val REQUEST_CODE_VOICE_RECOGNITION = 1001
        const val REQUEST_CODE_THAI_VOICE_RECOGNITION = 1002

    }

    private fun callVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "th-TH")
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")
        startActivityForResult(intent, REQUEST_CODE_VOICE_RECOGNITION)
    }

    private fun callThaiVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "th-TH")
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")
        startActivityForResult(intent, REQUEST_CODE_THAI_VOICE_RECOGNITION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_VOICE_RECOGNITION && resultCode == Activity.RESULT_OK) {
            val resultList = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val result = resultList!![0]
            showPopup(2,result)


        }else if(requestCode == REQUEST_CODE_THAI_VOICE_RECOGNITION && resultCode == Activity.RESULT_OK){
            val resultList = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val result = resultList!![0]
            showPopup(1,result)


        }
    }

    fun showPopup(id :Int,result :String){
        lesson_popup_layout.visibility = View.VISIBLE
        //Picasso.get().load(image).into(quiz_popup_image)
        //lesson_popup_image.setImageDrawable(ContextCompat.getDrawable(this, image))

        /*
        if (id != 2) {
            quiz_popup_image.setColorFilter(ContextCompat.getColor(this, color), android.graphics.PorterDuff.Mode.SRC_IN)
        }
        */



        //lesson_popup_title_a.text = title
        //lesson_popup_title_b.text = title


        //lesson_popup_text_btn.text = button

        /*
        if (id == 1) {

        } else if (id == 2) {

        }
        */

        if (sqlite.read().contentEquals(LangSQ.THAI)) {
            lesson_popup_text_btn.text = "ปิด"

        } else {
            lesson_popup_text_btn.text = "Close"

        }

        lesson_popup_title_c.text = result


        if (id == 1) {


            if(thai[thai.length - 1].equals('?')){
                val x = thai
                val y = x.substring(0,x.length - 1).toString()
                val bool = result.contentEquals(y)
                check(bool)

            }else {
                val bool = result.contentEquals(thai)

                check(bool)
            }





        } else if (id == 2) {


            if(eng[eng.length - 1].equals('?')){
                val x = eng
                val y = x.substring(0,x.length - 1).toString()
                val a = y.toLowerCase()
                val b = result.toLowerCase()
                val bool = b.contentEquals(a)
                check(bool)

            }else {

                val a = eng.toLowerCase()
                val b = result.toLowerCase()
                val bool = b.contentEquals(a)
                check(bool)
            }




        }

        //var sqlite = ChapterSQ(this, ChapterSQ.convertName(x), data.size)

        lesson_popup_text_btn.setOnClickListener {
            lesson_popup_layout.visibility = View.GONE
        }
    }

    fun popupC(){
        lesson_popup_image.setColorFilter(ContextCompat.getColor(this, R.color.color_game_blue), android.graphics.PorterDuff.Mode.SRC_IN)
        lesson_popup_title_b.setTextColor(ContextCompat.getColor(this, R.color.color_game_blue))
        //lesson_popup_image.setImageDrawable(ContextCompat.getDrawable(this, image))


    }

    fun popupInC(){
        lesson_popup_image.setColorFilter(ContextCompat.getColor(this, color), android.graphics.PorterDuff.Mode.SRC_IN)
        lesson_popup_title_b.setTextColor(ContextCompat.getColor(this, color))
        //lesson_popup_image.setImageDrawable(ContextCompat.getDrawable(this, image))


    }

    fun check(bool:Boolean){
        if (sqlite.read().contentEquals(LangSQ.THAI)) {
            if(bool){
                lesson_popup_title_b.text = "ถูกต้อง"
                popupC()
            }else{
                lesson_popup_title_b.text = "ไม่ถูกต้อง"
                popupInC()

            }
        } else {
            if(bool){
                lesson_popup_title_b.text = "Correct"
                popupC()

            }else{
                lesson_popup_title_b.text = "Incorrect"
                popupInC()

            }
        }
    }


    fun hidePopup(){

    }



}
