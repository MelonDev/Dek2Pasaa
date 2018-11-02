package th.ac.up.se.takingbear.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.se.takingbear.Data.LessonCard
import th.ac.up.se.takingbear.Data.WordInfo
import th.ac.up.se.takingbear.LessonActivity
import th.ac.up.se.takingbear.R
import th.ac.up.se.takingbear.ViewHolder.ChapterImageViewHolder

class NewLessonAdapter(val lang :String,var colorDark: Int, var color: Int, var orientation: String, var data: ArrayList<WordInfo>) : RecyclerView.Adapter<ChapterImageViewHolder>(){

    private lateinit var context: Context

    lateinit var view: View
    var weight: Int = 0
    var height: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chapter_card_image,parent,false)

        this.context = parent.context

        weight = DeviceUtills(context).getScreenWidth()
        height = DeviceUtills(context).getScreenHeight()

        return ChapterImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ChapterImageViewHolder, position: Int) {

        val slot = data[position]

        holder.card.setCardBackgroundColor(ContextCompat.getColor(context, colorDark))
        //holder.image.setImageDrawable(ContextCompat.getDrawable(context,slot.image))

        if(lang.contentEquals(LangSQ.THAI)){
            holder.nameA.text = slot.nameThai
            holder.nameB.text = slot.nameThai
        }else {
            holder.nameA.text = slot.nameEng
            holder.nameB.text = slot.nameEng
        }


        var hei = height - dpsToPixels(120)
        var he = height / 2

        if(slot.cover.isEmpty()){
            holder.second.setCardBackgroundColor(ContextCompat.getColor(context, colorDark))
        }else {
            holder.second.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        }

        holder.card.layoutParams = RelativeLayout.LayoutParams(he, hei)
        holder.main.layoutParams = RelativeLayout.LayoutParams(he + dpsToPixels(16), hei)

        if (data[position].cover.isNotEmpty()) {
            //Log.e("IMAGE",data[position].cover)
            Glide.with(context).load(data[position].cover).into(holder.image)
            //holder.overlay.setImageDrawable(ContextCompat.getDrawable(context,overlay))
            if (color == R.color.colorRedDark) {
                if (orientation == "PORT") {
                    holder.overlay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.m5))

                } else if (orientation == "LAND") {
                    holder.overlay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.m3))
                }
            } else if (color == R.color.color_game_blue) {
                if (orientation == "PORT") {
                    holder.overlay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.m6))

                } else if (orientation == "LAND") {
                    holder.overlay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.m4))

                }
            }
        }


        holder.card.setOnClickListener {

            var intent = Intent(this.context,LessonActivity::class.java)
            intent.apply {
                putExtra("COLOR",color)
                putExtra("MSKEY",slot.masterKey)
                putExtra("KEY",slot.key)
                putExtra("DARK",colorDark)



            }

            if(position > 0 && position < (data.size - 1)){
                intent.putExtra("PRE",data[position - 1].key)
                intent.putExtra("POST",data[position + 1].key)
            }else if(position > 0 && position == (data.size - 1)){
                intent.putExtra("PRE",data[position - 1].key)
                intent.putExtra("POST","NULL")
            } else if(position == 0 && position < (data.size - 1)){
                intent.putExtra("PRE","NULL")
                intent.putExtra("POST",data[position + 1].key)
            }else {
                intent.putExtra("PRE","NULL")
                intent.putExtra("POST","NULL")


            }

            //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.activity, holder.card_layout, "chapter_card")
            //context.startActivity(intent,options.toBundle())

            context.startActivity(intent)

        }


    }

    private fun dpsToPixels(dps: Int): Int {
        val r = context.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }
}