package th.ac.up.se.takingbear.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.people_card.view.*

class ComViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){

    var nameA = itemView.people_card_name_A
    var nameB = itemView.people_card_name_B
    var score = itemView.people_score

    var card = itemView.people_card_layout
    var image = itemView.people_card_image
    var overlay =  itemView.people_card_overlay
    var main = itemView.people_card_main_layout
    var second = itemView.people_card_layout_second

    var top = itemView.people_top_icon

}