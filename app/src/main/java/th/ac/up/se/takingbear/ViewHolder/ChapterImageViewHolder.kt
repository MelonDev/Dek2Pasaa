package th.ac.up.se.takingbear.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.chapter_card_image.view.*
import kotlinx.android.synthetic.main.chapter_card_land.view.*

class ChapterImageViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){

    var nameA = itemView.chapter_card_image_name_A
    var nameB = itemView.chapter_card_image_name_B
    var card = itemView.chapter_card_image_layout
    var image = itemView.chapter_card_image_image
    var overlay =  itemView.chapter_card_image_overlay
    var main = itemView.chapter_card_image_main_layout
    var second = itemView.chapter_card_image_layout_second

}