package mehrpars.mobile.sample.ui.list_sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.groupiex.plusAssign
import com.xwray.groupie.kotlinandroidextensions.Item
import mehrpars.mobile.sample.R
import mehrpars.mobile.sample.databinding.FragmentSampleListBinding
import mehrpars.mobile.sample.ui.home.HomeViewModel

/**
 * Created by Ali Vatanparast
 */

class SampleListFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentSampleListBinding

    private val groupAdapter: GroupAdapter<GroupieViewHolder> by lazy {
        GroupAdapter<GroupieViewHolder>().apply {
            setOnItemClickListener(onItemClickListener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sample_list, container, false)

        binding.cardView01.setOnClickListener {
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = 1
        }

        binding.cardView02.setOnClickListener {
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = 2
        }

        binding.cardView03.setOnClickListener {
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = 3
        }

        binding.recyclerView.also {
            it.adapter = groupAdapter
        }
        groupAdapter += RectangleAddItem()

        return binding.root
    }

    private val onItemClickListener = OnItemClickListener { item, view ->
        when (item) {
            is RectangleAddItem -> {
                groupAdapter += getRandomRectangle()
            }
        }
    }

    fun getRandomRectangle(): Item {
        val list = mutableListOf<Item>()

        list.add(RectangleBlueItem(onRemoveClick = {
            groupAdapter.remove(it)
        }))


        list.add(RectangleRedItem(onRemoveClick = {
            groupAdapter.remove(it)
        }))

        list.add(RectangleGreenItem(onRemoveClick = {
            groupAdapter.remove(it)
        }))

        return list.get((0..2).random())
    }
}