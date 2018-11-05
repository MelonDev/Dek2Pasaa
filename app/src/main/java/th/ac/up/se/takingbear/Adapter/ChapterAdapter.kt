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
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.up.se.tkbcontrol.Data.PeopleInfo
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.se.takingbear.Data.CardObj
import th.ac.up.se.takingbear.Data.Chapter
import th.ac.up.se.takingbear.ListCardActivity
import th.ac.up.se.takingbear.R
import th.ac.up.se.takingbear.ViewHolder.ChapterViewHolder
import java.math.RoundingMode
import java.text.DecimalFormat

class ChapterAdapter(val fragment: ListCardActivity, var colorDark: Int, var color: Int, var orientation: String, var data: ArrayList<Chapter>) : RecyclerView.Adapter<ChapterViewHolder>() {

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


        val slot = data[position]

        holder.card.setCardBackgroundColor(ContextCompat.getColor(context, colorDark))

        holder.nameA.text = data[position].name
        holder.nameB.text = data[position].name

        if (slot.info.type.contentEquals("FREE")) {
            unSetPrice(holder, position)
        } else {

            FirebaseDatabase.getInstance().reference
                    .child("Peoples")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
                    .child("Coupons").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("", "")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.value != null) {
                                var mD = ArrayList<String>()
                                var count = 0
                                p0.children.forEach {
                                    mD.add(it.key.toString())
                                    count += 1
                                    if (count == p0.children.count()) {
                                        val bool: Boolean = data[position].info.key in mD

                                        if (!bool) {
                                            setPrice(holder, slot)
                                        } else {
                                            unSetPrice(holder,position)
                                        }
                                    }
                                }
                            } else {
                                setPrice(holder, slot)
                            }
                        }
                    })


        }

        //Log.e("p",position.toString())


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

    fun setPrice(holder: ChapterViewHolder, slot: Chapter) {
        holder.priceBack.visibility = View.VISIBLE

        holder.priceLabel.setBackgroundColor(ContextCompat.getColor(fragment, colorDark))
        holder.priceInside.setBackgroundColor(ContextCompat.getColor(fragment, color))


        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING

        //holder.priceText.setTextColor(ContextCompat.getColor(fragment,colorDark))
        val sqlite = LangSQ(fragment)
        if (sqlite.read().contentEquals(LangSQ.THAI)) {

            holder.priceText.text = "${df.format(slot.info.price)}\nบาท"


        } else {
            holder.priceText.text = "${df.format(slot.info.price)}\nBaht"

        }
    }

    fun unSetPrice(holder: ChapterViewHolder, position: Int) {
        holder.priceBack.visibility = View.GONE

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
    }

    private fun dpsToPixels(dps: Int): Int {
        val r = context.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }


}