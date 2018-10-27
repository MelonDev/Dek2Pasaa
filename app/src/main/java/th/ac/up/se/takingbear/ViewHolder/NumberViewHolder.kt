package th.ac.up.se.takingbear.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.number_card.view.*

class NumberViewHolder(itemview:View) : RecyclerView.ViewHolder(itemview){

    var card = itemview.number_card_layout
    var nameA = itemview.number_card_text_A
    var nameB = itemview.number_card_text_B
    var star = itemview.number_card_star
    var main = itemview.number_card_main

}