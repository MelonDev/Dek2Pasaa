package th.ac.up.se.takingbear.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.se.takingbear.Data.Chapter
import th.ac.up.se.takingbear.ListCardActivity
import th.ac.up.se.takingbear.R
import th.ac.up.se.takingbear.ViewHolder.ChapterViewHolder

class ChapterAdapter(var colorDark: Int, var color: Int, var orientation: String, var data: ArrayList<Chapter>) : RecyclerView.Adapter<ChapterViewHolder>() {

    lateinit var context: Context
    lateinit var view: View
    var weight: Int = 0
    var height: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {

        view = LayoutInflater.from(parent.context).inflate(R.layout.chapter_card_land, parent, false)


        context = parent.context


        weight = DeviceUtills(context).getScreenWidth()
        height = DeviceUtills(context).getScreenHeight()

        return ChapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {



        holder.card.setCardBackgroundColor(ContextCompat.getColor(context, colorDark))

        holder.nameA.text = data[position].name
        holder.nameB.text = data[position].name


        //Log.e("p",position.toString())

        holder.card.setOnClickListener {
            if (color == R.color.colorRedDark) {
                val intent = Intent(context, ListCardActivity::class.java)
                intent.putExtra("COLOR", color)
                intent.putExtra("TITLE", data[position].name)
                intent.putExtra("ID", 101)
                intent.putExtra("DARK", colorDark)
                intent.putExtra("POS", position)
                intent.putExtra("CHAPTER", data[position].info.key)

                context.startActivity(intent)
            } else if (color == R.color.color_game_blue) {

                val intent = Intent(context, ListCardActivity::class.java)
                intent.putExtra("COLOR", color)
                intent.putExtra("TITLE", data[position].name)
                intent.putExtra("ID", 100)
                intent.putExtra("DARK", colorDark)
                intent.putExtra("POS", data[position].info.key)
                intent.putExtra("CHAPTER", data[position].info.key)

                context.startActivity(intent)
            }
        }


//orientation.contentEquals("LAND") &&


        if (data[position].image.isNotEmpty()) {
            //Picasso.get().load(data[position].image).into(holder.image)
            Glide.with(context).load(data[position].image).into(holder.image)
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


        var hei = height - dpsToPixels(120)
        var he = height / 2

        holder.card.layoutParams = RelativeLayout.LayoutParams(he, hei)
        holder.main.layoutParams = RelativeLayout.LayoutParams(he + dpsToPixels(16), hei)
        //holder.nameA.textSize = dpsToPixels(30).toFloat()
        //holder.nameB.textSize = dpsToPixels(30).toFloat()


    }

    private fun dpsToPixels(dps: Int): Int {
        val r = context.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }


}