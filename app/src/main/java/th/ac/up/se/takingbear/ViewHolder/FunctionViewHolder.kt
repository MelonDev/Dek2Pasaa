package th.ac.up.se.takingbear.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.function_card_land.view.*

class FunctionViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){

    val textA = itemView.function_textview_a
    val cardIn = itemView.function_card_in
    val cardOut = itemView.function_card_out
    val main = itemView.function_main_layout
    val image = itemView.function_image

}