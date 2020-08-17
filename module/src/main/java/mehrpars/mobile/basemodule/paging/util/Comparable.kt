package mehrpars.mobile.basemodule.paging.util

interface Comparable {

    fun objectEqualsTo(item: Comparable): Boolean

    fun contentEqualsTo(item: Comparable): Boolean
}