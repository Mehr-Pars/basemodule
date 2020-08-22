package mehrpars.mobile.basemodule.paging.util

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Ali Arasteh
 */

/**
 *  [Comparable] is helper class used in [DiffUtil] pattern for comparing two objects
 *  objects used in recycler list must implement [Comparable] or implement their own strategy for
 *  comparing objects.
 * */
interface Comparable {

    /**
     * Called to check whether two objects represent the same item.
     * For example, if your items have unique ids, this method should check their id equality.
     * */
    fun objectEqualsTo(item: Comparable): Boolean

    /**
     * Called to check whether two items have the same data.
     * This information is used to detect if the contents of an item have changed.
     * */
    fun contentEqualsTo(item: Comparable): Boolean
}