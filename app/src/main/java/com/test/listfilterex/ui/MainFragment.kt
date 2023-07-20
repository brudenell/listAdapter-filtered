package com.test.listfilterex.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.listfilterex.MainActivity
import com.test.listfilterex.Post
import com.test.listfilterex.R
import com.test.listfilterex.databinding.FragmentMainBinding
import com.test.listfilterex.databinding.RowMainBinding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator

class MainFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentMainBinding

    lateinit var postAdapter: PostAdapter
    lateinit var animatedAdapter: AlphaInAnimationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        binding = FragmentMainBinding.inflate(layoutInflater)

        binding.run {
            toolbarMain.run {
                title = "MainFragment"
                setTitleTextColor(mainActivity.getColor(R.color.title_gray))
                inflateMenu(R.menu.main_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menuItemMainEdit -> {
                            findNavController().navigate(R.id.action_mainFragment_to_inputFragment)
                        }
                    }
                    false
                }
            }

            chipGroupMain.run {
                setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        R.id.chipMainAll -> {
                            mainActivity.selectedCategory = "전체"
                            postAdapter.getFilter().filter("전체")
                        }
                        R.id.chipMainMovie -> {
                            mainActivity.selectedCategory = "영화"
                            postAdapter.filter.filter("영화")
                        }
                        R.id.chipMainMusic -> {
                            mainActivity.selectedCategory = "음악"
                            postAdapter.filter.filter("음악")
                        }
                        R.id.chipMainPic -> {
                            mainActivity.selectedCategory = "사진"
                            postAdapter.filter.filter("사진")
                        }
                    }
                }
            }

            postAdapter = PostAdapter()
            //animatedAdapter = AlphaInAnimationAdapter(postAdapter)

            recyclerViewMain.run {
                adapter = SlideInRightAnimationAdapter(postAdapter).apply {
                    setFirstOnly(false)
                }
                layoutManager = LinearLayoutManager(mainActivity)
                //itemAnimator = SlideInRightAnimator()
                addItemDecoration(DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL))
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (mainActivity.selectedCategory == "전체")
            postAdapter.filter.filter("전체")
    }

    val diffUtil = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    inner class PostAdapter: ListAdapter<Post, PostAdapter.PostViewHolder>(diffUtil), Filterable {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            rowMainBinding.root.layoutParams = params

            return PostViewHolder(rowMainBinding)
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            holder.bind(currentList[position])
        }

        inner class PostViewHolder(val rowMainBinding: RowMainBinding): RecyclerView.ViewHolder(rowMainBinding.root) {
            fun bind(post: Post) {
                rowMainBinding.textViewRowCategory.text = post.category
                rowMainBinding.textViewRowTitle.text = post.title
                rowMainBinding.imageViewRow.setImageBitmap(post.bitmap)

                rowMainBinding.root.setOnClickListener {
                    mainActivity.postList.remove(post)
                    when (mainActivity.selectedCategory) {
                        "전체" -> {
                            val newList = mainActivity.postList.toList()
                            postAdapter.submitList(newList)
                        }
                        "영화" -> {
                            postAdapter.filter.filter("영화")
                        }
                        "음악" -> {
                            postAdapter.filter.filter("음악")
                        }
                        "사진" -> {
                            postAdapter.filter.filter("사진")
                        }
                    }
                }
            }
        }

        var filteredPostList = listOf<Post>()
        var itemFilter = ItemFilter()

        override fun getFilter(): Filter {
            return itemFilter
        }

        inner class ItemFilter : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()

                val filteredList = mutableListOf<Post>()
                when (constraint) {
                    "전체" -> {
                        results.values = mainActivity.postList.toList()
                        results.count = mainActivity.postList.size

                        return results
                    }
                    "영화" -> {
                        for (post in mainActivity.postList) {
                            if (post.category == "영화")
                                filteredList.add(post)
                        }
                    }
                    "음악" -> {
                        for (post in mainActivity.postList) {
                            if (post.category == "음악")
                                filteredList.add(post)
                        }
                    }
                    "사진" -> {
                        for (post in mainActivity.postList) {
                            if (post.category == "사진")
                                filteredList.add(post)
                        }
                    }
                }

                results.values = filteredList
                results.count = filteredList.size

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredPostList = results?.values as List<Post>
                submitList(filteredPostList)
            }
        }
    }
}