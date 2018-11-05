package th.ac.up.se.takingbear.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chapter_card_land.view.*

class ChapterViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){

    var nameA = itemView.chapter_card_name_A
    var nameB = itemView.chapter_card_name_B
    var card = itemView.chapter_card_layout
    var image = itemView.chapter_card_image
    var overlay =  itemView.chapter_card_overlay
    var main = itemView.chapter_card_main_layout
    var second = itemView.chapter_card_layout_second

    var priceBack = itemView.chapter_paid_back
    var priceText = itemView.chapter_paid_price
    var priceLabel = itemView.chapter_paid_label
    var priceInside = itemView.chapter_paid_label_inside
}