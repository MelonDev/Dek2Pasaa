package th.ac.up.se.takingbear.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import th.ac.up.se.takingbear.Data.MainFunctionData
import th.ac.up.se.takingbear.ListCardActivity
import th.ac.up.se.takingbear.R
import th.ac.up.se.takingbear.ViewHolder.ListFunctionViewHolder
import android.util.TypedValue
import android.widget.RelativeLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.se.takingbear.CompetitionActivity
import th.ac.up.se.takingbear.ViewHolder.FunctionViewHolder


class ListFunctionAdapter(var activity: FragmentActivity, val data :ArrayList<MainFunctionData>) : RecyclerView.Adapter<FunctionViewHolder>(){

    private lateinit var context :Context

    lateinit var view: View

    var weight: Int = 0
    var height: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.function_card_land,parent,false)
        this.context = parent.context

        context = parent.context


        weight = DeviceUtills(context).getScreenWidth()
        height = DeviceUtills(context).getScreenHeight()

        return FunctionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FunctionViewHolder, position: Int) {
        val slot = data[position]

        //holder.textA.text = slot.name
        val sqlite = LangSQ(activity)
        var title = ""
        if(sqlite.read().contentEquals(LangSQ.THAI)){
            holder.textA.text = slot.nameThai
            title = slot.nameThai
        }else {
            holder.textA.text = slot.nameEng
            title = slot.nameEng
        }

        holder.cardIn.setCardBackgroundColor(ContextCompat.getColor(context,slot.color))
        holder.cardOut.setCardBackgroundColor(ContextCompat.getColor(context,slot.colorDark))

        var hei = height - dpsToPixels(150)
        var he = (height / 2.3).toInt()

        holder.cardOut.layoutParams = RelativeLayout.LayoutParams(he, hei)
        holder.main.layoutParams = RelativeLayout.LayoutParams(he + dpsToPixels(16), hei)

        val h = (height/4.5).toInt()

        val layoutParams = RelativeLayout.LayoutParams(h,h)

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)


        holder.image.setImageDrawable(ContextCompat.getDrawable(context,slot.icon))
        holder.image.layoutParams = layoutParams
        //holder.image.setPadding(0,0,0,dpsToPixels(100))

        holder.textA.textSize = (he/10).toFloat()



        if(slot.id != 2 && slot.id != 3){
            holder.cardOut.setOnClickListener {

                var intent = Intent(this.context,ListCardActivity::class.java)
                intent.apply {
                    putExtra("COLOR",slot.color)
                    putExtra("TITLE",title)
                    putExtra("ID",slot.id)
                    putExtra("DARK",slot.colorDark)
                }
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.activity, holder.cardIn, "chapter_card")

                context.startActivity(intent,options.toBundle())
            }
        }else if(slot.id == 2){
            holder.cardOut.setOnClickListener {
                var intent = Intent(this.context,CompetitionActivity::class.java)
                intent.apply {
                    putExtra("COLOR",slot.color)
                    putExtra("TITLE",title)
                    putExtra("ID",slot.id)
                    putExtra("DARK",slot.colorDark)
                }
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.activity, holder.cardIn, "chapter_card")
                context.startActivity(intent,options.toBundle())
            }
        }


        /* holder.indicator.visibility = View.GONE
         holder.card_layout.visibility = View.GONE



         if(slot.id == -1){
             holder.indicator.visibility = View.GONE

         }else {
             holder.name_tv.text = slot.name
             holder.name_tv_shadow.text = slot.name

             holder.card_layout.visibility = View.VISIBLE
             holder.card_layout.setCardBackgroundColor(ContextCompat.getColor(context,slot.color))
             holder.card_layout_dark.setCardBackgroundColor(ContextCompat.getColor(context,slot.colorDark))

             holder.card_layout_dark.setOnClickListener {

                 var intent = Intent(this.context,ListCardActivity::class.java)
                 intent.apply {
                     putExtra("COLOR",slot.color)
                     putExtra("TITLE",slot.name)
                     putExtra("ID",slot.id)
                     putExtra("DARK",slot.colorDark)
                 }
                 val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.activity, holder.card_layout, "chapter_card")

                 context.startActivity(intent,options.toBundle())
             }

         }


 */


    }

    private fun dpsToPixels(dps: Int): Int {
        val r = context.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }
}