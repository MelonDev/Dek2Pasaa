package th.ac.up.se.takingbear.ViewHolder

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_button_card.view.*

class ListFunctionViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){

    var name_tv :TextView = itemView.main_button_card_name_text
    var card_layout : CardView = itemView.main_button_card_layout
    var card_layout_dark :CardView = itemView.main_button_card_layout_dark


    var indicator :RelativeLayout = itemView.main_button_indicator

    var name_tv_shadow :TextView = itemView.main_button_card_name_text_shadow

}