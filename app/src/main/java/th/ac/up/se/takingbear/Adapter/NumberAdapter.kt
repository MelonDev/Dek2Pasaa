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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
import java.util.*
import kotlin.collections.ArrayList

class NumberAdapter(var fragmentActivity: FragmentActivity, var data: ArrayList<TestInfo>, var checkData: ArrayList<ChapterCheck>, var color: Int, var colorDark: Int) : RecyclerView.Adapter<NumberViewHolder>() {

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

        holder.close()


        val check = ArrayList<String>()
        checkData.forEach {
            check.add(it.key)
        }

        val bool: Boolean = data[position].key in check

        //Log.e("SS $position",data[position].key.toString())


        if (position == 0) {

            //Log.e("0","0")

            if (bool) {
                val it = checkData[check.indexOf(data[position].key)]
                if (it.passed) {
                    holder.star(position)
                    //Log.e("0","1")

                } else if (it.opened) {
                    holder.open(position)
                    //Log.e("0","2")

                } else {
                    holder.close()
                    //Log.e("0","3")

                }
            } else {
                holder.open(position)
                //Log.e("0","4")

            }

        } else {

            //Log.e(position.toString(),"0")


            val lastBool: Boolean = data[position - 1].key in check

            if (bool){
                val it = checkData[check.indexOf(data[position].key)]
                if (it.passed) {
                    holder.star(position)
                    //Log.e(position.toString(),"1")

                } else if (it.opened) {
                    holder.open(position)
                    //Log.e(position.toString(),"2")

                } else {
                    holder.close()
                    //Log.e(position.toString(),"3")

                }
            }else if(lastBool) {
                holder.open(position)
                //Log.e(position.toString(),"4")

            }else {
                holder.close()
                //Log.e(position.toString(),"5")

            }


        }





        /*

        val a = Arrays.asList(data)


        val b = ArrayList<String>()
        checkData.forEach {
            b.add(it.key)
        }

        holder.close()

        val bool: Boolean = data[position].key in b
        var bool2: Boolean = false

        var statusOpen = false

        if (data.size > 1) {
            if (position > 0) {
                bool2 = data[position - 1].key in b

                if (bool) {
                    val it = checkData[b.indexOf(data[position].key)]
                    if (it.passed) {
                        holder.star()
                        statusOpen = true
                    } else if (it.opened) {
                        holder.open()
                        statusOpen = true

                    } else {
                        holder.close()
                        statusOpen = false


                    }
                } else {
                    if (bool2) {
                        holder.open()
                        statusOpen = true

                    } else {
                        holder.close()
                        statusOpen = false

                    }
                }

            } else {
                if (bool) {
                    val it = checkData[b.indexOf(data[position].key)]
                    if (it.passed) {
                        holder.star()
                        statusOpen = true

                    } else if (it.opened) {
                        holder.open()
                        statusOpen = true

                    } else {
                        holder.close()
                        statusOpen = false
                    }

                } else {
                    holder.open()
                    statusOpen = true


                }
            }
        } else if (data.size == 1) {
            if (bool) {
                val it = checkData[b.indexOf(data[position].key)]
                if (it.passed) {
                    holder.star()
                    statusOpen = true

                } else if (it.opened) {
                    holder.open()
                    statusOpen = true

                } else {
                    holder.close()
                    statusOpen = false

                }
            } else {
                holder.open()
                statusOpen = true

            }
        }

        holder.card.setOnClickListener {
            if (statusOpen) {
                notLock(position)
            }else {
                locked()
            }
        }


        Log.e(position.toString(), bool.toString())

*/

        /*
        var c = 0
        if (checkData.size > 0) {

            checkData.forEach {
                c += 1

                if (it.key.contentEquals(data[position].key)) {
                    if (it.passed) {
                        holder.star()

                    } else if (it.opened) {
                        holder.open()

                    } else {
                        holder.close()

                    }

                    if (c < data.size - 1) {
                        checkData.forEach {
                            if (!it.key.contentEquals(data[position + 1].key)) {
                                    holder.open()

                            }

                        }
                    }

                }


                /*
                if (it.key.contentEquals(data[position].key)) {


                    if (it.passed) {
                        holder.star()
                    } else if (it.opened) {
                        holder.open()
                    } else {
                        holder.close()
                    }

                } else {

                    if (position == 0) {
                        holder.open()
                    } else {
                        holder.close()
                    }
                }
                */

            }
        } else {
            if (position == 0) {
                holder.open()

                holder.card.setOnClickListener {

                    notLock(0)

                }

                //Log.e(position.toString(), "OPEN")

            } else {
                holder.close()

                //Log.e(position.toString(), "CLOSE")

                holder.card.setOnClickListener {

                    locked()

                }

            }
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

    private fun locked() {

        fragmentActivity.apply {

            var sqlite = LangSQ(this)
            if (sqlite.read().contentEquals(LangSQ.THAI)) {
                this.list_popup_title_b.text = "ข้อนี้ล็อคอยู่"
                this.list_popup_text_btn.text = "รับทราบ"
            } else {
                this.list_popup_title_b.text = "Locked"
                this.list_popup_text_btn.text = "OK"
            }

            this.list_popup_layout.visibility = View.VISIBLE

            this.list_popup_text_btn.setOnClickListener {
                this.list_popup_layout.visibility = View.GONE
            }

        }
    }

    private fun notLock(position: Int) {
        var intent = Intent(context, QuizActivity::class.java)
        val d = data[position]
        intent.apply {
            putExtra("COLOR", color)
            //putExtra("CHAP", chapter)
            putExtra("MSKEY", d.masterKey)
            putExtra("KEY", d.key)
            putExtra("POSITION", position)
            putExtra("DARK", colorDark)
        }
        context.startActivity(intent)
    }

    private fun NumberViewHolder.star(position: Int) {
        this.star.visibility = View.VISIBLE
        this.nameB.setPadding(0, dpsToPixels(30), dpsToPixels(10), dpsToPixels(0))
        this.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        //holder.nameB.setTextColor(ContextCompat.getColor(context,R.color.colorText))
        this.nameB.setTextColor(ContextCompat.getColor(context, colorDark))

        this.card.setOnClickListener {

            notLock(position)

        }
    }

    private fun NumberViewHolder.open(position: Int) {

        this.star.visibility = View.GONE
        this.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        //holder.nameB.setTextColor(ContextCompat.getColor(context,R.color.colorText))
        this.nameB.setTextColor(ContextCompat.getColor(context, colorDark))

        this.card.setOnClickListener {

            notLock(position)

        }

    }

    private fun NumberViewHolder.close() {
        this.card.setCardBackgroundColor(ContextCompat.getColor(context, colorDark))
        //holder.nameB.setTextColor(ContextCompat.getColor(context,R.color.colorWhite))
        this.star.visibility = View.GONE

        this.card.setOnClickListener {

            locked()

        }
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