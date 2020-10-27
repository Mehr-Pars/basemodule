package mehrpars.mobile.sample.ui.list_sample

import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.list_item_rectangle_red.view.*
import mehrpars.mobile.sample.R

/**
 * Created by Ali Vatanparast
 */

class RectangleBlueItem(private val onRemoveClick: (item: RectangleBlueItem) -> Unit) : Item() {

    override fun bind(
        viewHolder: com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder,
        position: Int
    ) {
        viewHolder.itemView.txtTitle.setText((10..99).random().toString())
        viewHolder.itemView.icClose.setOnClickListener {
            onRemoveClick(this@RectangleBlueItem)
        }
    }

    override fun getLayout() = R.layout.list_item_rectangle_blue
}