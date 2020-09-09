package mehrpars.mobile.sample.ui.list_sample

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.list_item_rectangle_red.view.*
import mehrpars.mobile.sample.R

/**
 * Created by Ali Vatanparast
 */

class RectangleGreenItem(private val onRemoveClick: (item: RectangleGreenItem) -> Unit)  : Item() {

    override fun getLayout() = R.layout.list_item_rectangle_green

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtTitle.setText((10..99).random().toString())
        viewHolder.itemView.icClose.setOnClickListener {
            onRemoveClick(this@RectangleGreenItem)
        }
    }
}