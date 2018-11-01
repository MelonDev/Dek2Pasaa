package th.ac.up.se.takingbear.Adapter

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_list_card.*
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.se.takingbear.Data.ChapterCheck
import th.ac.up.se.takingbear.Data.QuizCard
import th.ac.up.se.takingbear.Data.TestInfo
import th.ac.up.se.takingbear.QuizActivity
import th.ac.up.se.takingbear.R
import th.ac.up.se.takingbear.SQLite.ChapterSQ
import th.ac.up.se.takingbear.ViewHolder.NumberViewHolder
import th.ac.up.se.thaicardgame.DataArray.Quiz

class NumberAdapter(var fragmentActivity: FragmentActivity, var data: ArrayList<TestInfo>, var color: Int, var colorDark: Int) : RecyclerView.Adapter<NumberViewHolder>() {

    lateinit var context: Context
    var weight: Int = 0
    var height: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.number_card, parent, false)

        context = parent.context

        weight = DeviceUtills(context).getScreenWidth()
        height = DeviceUtills(context).getScreenHeight()

        return NumberViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {

        holder.open()

        /*
        if (check[position].passed) {
            holder.star()
        } else if (check[position].opened) {
            holder.open()
        } else {
            holder.close()
        }
        */

        /*
        if(position <= 3){
            if(position != 3){
                holder.star.visibility = View.VISIBLE
                holder.nameB.setPadding(0,dpsToPixels(30),dpsToPixels(10),dpsToPixels(0))
            }else {
                holder.star.visibility = View.GONE
            }
            holder.card.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorWhite))
            //holder.nameB.setTextColor(ContextCompat.getColor(context,R.color.colorText))
            holder.nameB.setTextColor(ContextCompat.getColor(context,this.colorDark))
        }else {
            holder.card.setCardBackgroundColor(ContextCompat.getColor(context,this.colorDark))
            //holder.nameB.setTextColor(ContextCompat.getColor(context,R.color.colorWhite))
            holder.star.visibility = View.GONE

        }
        */





        //ชั่วคราว
        /*
        holder.card.setOnClickListener {


            var q = Quiz().getChapter(0, chapter)
            var check = ChapterSQ(fragmentActivity, ChapterSQ.convertName(chapter), q.size)
            var data = check.getData()

            if (!data[position].opened) {

                fragmentActivity.apply {

                    var sqlite = LangSQ(this)
                    if(sqlite.read().contentEquals(LangSQ.THAI)){
                        this.list_popup_title_b.text = "ข้อนี้ล็อคอยู่"
                        this.list_popup_text_btn.text = "รับทราบ"
                    }else {
                        this.list_popup_title_b.text = "Locked"
                        this.list_popup_text_btn.text = "OK"
                    }

                    this.list_popup_layout.visibility = View.VISIBLE

                    this.list_popup_text_btn.setOnClickListener {
                        this.list_popup_layout.visibility = View.GONE
                    }

                }


            } else {

                var intent = Intent(context, QuizActivity::class.java)
                intent.apply {
                    putExtra("COLOR", color)
                    putExtra("CHAP", chapter)
                    putExtra("POS", position)
                    putExtra("DARK", colorDark)
                }
                context.startActivity(intent)
            }
        }

*/




        //holder.nameA.text = (position+1).toString()
        holder.nameB.text = (position + 1).toString()


        var hei = height - dpsToPixels(120)
        var he = (hei / 11) * 5

        holder.card.layoutParams = RelativeLayout.LayoutParams(height / 4, he)
        holder.main.layoutParams = RelativeLayout.LayoutParams((height / 4) + 40, hei / 2)

        val x = ((he / 7) * 2)

        holder.nameB.textSize = x.toFloat()
        holder.star.layoutParams = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (he / 7) * 3)
        holder.star.setPadding(0, height / 20, 0, 0)


        //holder.main.layoutParams = RelativeLayout.LayoutParams(he + dpsToPixels(16), hei)


        //TextViewCompat.setAutoSizeTextTypeWithDefaults(holder.nameA,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        //TextViewCompat.setAutoSizeTextTypeWithDefaults(holder.nameB,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)


        //TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(holder.nameA,40,120,10,TypedValue.COMPLEX_UNIT_SP)
        //TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(holder.nameB,40,200,10,TypedValue.COMPLEX_UNIT_SP)

        //var s = TextViewCompat.getAutoSizeTextAvailableSizes(holder.nameA)
        //var a = TextViewCompat.getAutoSizeStepGranularity(holder.nameA)
        //val size = holder.nameA.textSize
        //holder.nameB.textSize = a.toFloat()


    }

    private fun NumberViewHolder.star() {
        this.star.visibility = View.VISIBLE
        this.nameB.setPadding(0, dpsToPixels(30), dpsToPixels(10), dpsToPixels(0))
        this.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        //holder.nameB.setTextColor(ContextCompat.getColor(context,R.color.colorText))
        this.nameB.setTextColor(ContextCompat.getColor(context, colorDark))
    }

    private fun NumberViewHolder.open() {

        this.star.visibility = View.GONE
        this.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        //holder.nameB.setTextColor(ContextCompat.getColor(context,R.color.colorText))
        this.nameB.setTextColor(ContextCompat.getColor(context, colorDark))

    }

    private fun NumberViewHolder.close() {
        this.card.setCardBackgroundColor(ContextCompat.getColor(context, colorDark))
        //holder.nameB.setTextColor(ContextCompat.getColor(context,R.color.colorWhite))
        this.star.visibility = View.GONE
    }

    private fun pixelsToDps(context: Context, pixels: Int): Int {
        val r = context.resources
        return Math.round(pixels / (r.displayMetrics.densityDpi / 160f))
    }

    private fun dpsToPixels(dps: Int): Int {
        val r = context.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }
}