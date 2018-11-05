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
import th.ac.up.se.takingbear.CompetitionActivity
import th.ac.up.se.takingbear.Data.CardObj
import th.ac.up.se.takingbear.Data.Chapter
import th.ac.up.se.takingbear.ListCardActivity
import th.ac.up.se.takingbear.R
import th.ac.up.se.takingbear.ViewHolder.ChapterViewHolder
import th.ac.up.se.takingbear.ViewHolder.ComViewHolder
import java.math.RoundingMode
import java.text.DecimalFormat

class ComAdapter(val fragment: CompetitionActivity, var colorDark: Int, var color: Int, var orientation: String, var data: ArrayList<PeopleInfo>) : RecyclerView.Adapter<ComViewHolder>() {

    lateinit var context: Context
    lateinit var view: View
    var weight: Int = 0
    var height: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComViewHolder {

        view = LayoutInflater.from(parent.context).inflate(R.layout.people_card, parent, false)


        context = parent.context


        weight = DeviceUtills(context).getScreenWidth()
        height = DeviceUtills(context).getScreenHeight()

        return ComViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ComViewHolder, position: Int) {


        val slot = data[position]

        holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_game_green_dark))

        holder.nameA.text = data[position].name
        holder.nameB.text = data[position].name

if(position == 0){
    holder.top.visibility = View.VISIBLE
}else {

    if(data[position].key == data[0].key){
        holder.top.visibility = View.VISIBLE

    }else {
        holder.top.visibility = View.GONE

    }


}

        if (data[position].image.isNotEmpty()) {
            //Picasso.get().load(data[position].image).into(holder.image)
            Glide.with(context).load(data[position].image).into(holder.image)
            //holder.overlay.setImageDrawable(ContextCompat.getDrawable(context,overlay))

            holder.overlay.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.m7))

        }


        val sq = LangSQ(fragment)


        var hei = height - dpsToPixels(120)
        var he = height / 2

        holder.card.layoutParams = RelativeLayout.LayoutParams(he, hei)
        holder.main.layoutParams = RelativeLayout.LayoutParams(he + dpsToPixels(16), hei)
        //holder.nameA.textSize = dpsToPixels(30).toFloat()
        //holder.nameB.textSize = dpsToPixels(30).toFloat()

        val u = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        FirebaseDatabase.getInstance().reference.child("Peoples").child(slot.key).child("History").addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Log.e("", "")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {

                    /*
                    val profile = p0.getValue(PeopleInfo::class.java)!!

                    if (sq.read().contentEquals(LangSQ.THAI)) {
                        score_text.text = "คะแนนของฉัน: ${profile.score}"
                    } else {
                        score_text.text = "My score: ${profile.score}"
                    }
                    */

                    var score = 0

                    var count = 0

                    p0.children.forEach {

                        val a = it.key.toString()
                        count += 1

                        FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("History").child(a).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("", "")
                            }

                            override fun onDataChange(p1: DataSnapshot) {
                                if (p1.value != null) {
                                    score += p1.children.count()

                                    if (count == p0.children.count()) {
                                        if (sq.read().contentEquals(LangSQ.THAI)) {
                                            holder.score.text = "${score} คะแนน"
                                        } else {
                                            holder.score.text = "${score} Point"
                                        }
                                    }
                                }
                            }
                        })

                    }

                } else {
                    if (sq.read().contentEquals(LangSQ.THAI)) {
                        holder.score.text = "0 คะแนน"
                    } else {
                        holder.score.text = "0 Point"
                    }
                }
            }
        })


    }



    private fun dpsToPixels(dps: Int): Int {
        val r = context.resources

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }


}