package com.example.humblr.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.humblr.databinding.FragmentPostsBinding
import com.example.humblr.domain.PostsAdapter
import com.example.humblr.utils.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!
    private val pagedAdapterNew = PostsAdapter(
        onClick = { String -> onItemClick(String) },
        onClickSubreddit = { String -> onRedditClick(String) },
        onClickUser = { String -> onUserClick(String) })
    private val pagedAdapterBest = PostsAdapter(
        onClick = { String -> onItemClick(String) },
        onClickSubreddit = { String -> onRedditClick(String) },
        onClickUser = { String -> onUserClick(String) })
    private val pagedAdapterSearch = PostsAdapter(
        onClick = { String -> onItemClick(String) },
        onClickSubreddit = { String -> onRedditClick(String) },
        onClickUser = { String -> onUserClick(String) })
    private val postsViewModel: PostsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.RVSubredditsList.adapter = pagedAdapterNew
        bindAdapters()



        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.RVSubredditsList.adapter = pagedAdapterBest
            } else {
                binding.RVSubredditsList.adapter = pagedAdapterNew
            }
        }

        binding.searchView.setOnQueryTextFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                binding.maskLayoutTransparent.visibility = View.VISIBLE
            } else {
                binding.maskLayoutTransparent.visibility = View.GONE
            }
        }

        binding.refreshButton.setOnClickListener {
            refreshAdapters()
        }

        binding.swipeRefresh.setOnRefreshListener {
            refreshAdapters()
        }

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {

                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    binding.maskLayoutTransparent.visibility = View.GONE
                    postsViewModel.loadSearchPosts(query)
                    binding.RVSubredditsList.adapter = pagedAdapterSearch
                    viewLifecycleOwner.lifecycleScope
                        .launch {
                            postsViewModel.pagedPostsSearch
                                .collect() {
                                    pagedAdapterSearch.submitData(it)
                                }
                        }
                    return true
                }
            }
        )

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            postsViewModel.state.collect() {
                binding.swipeRefresh.isRefreshing = postsViewModel.state.value == State.Loading
                refreshUI(it)
            }
        }

        bindLoadStateToState()

        return root
    }

    private fun bindLoadStateToState() {
        pagedAdapterNew.addLoadStateListener { loadStates ->
            postsViewModel.updateLoadingState(loadStates.refresh)
        }
        pagedAdapterBest.addLoadStateListener { loadStates ->
            postsViewModel.updateLoadingState(loadStates.refresh)
        }
        pagedAdapterSearch.addLoadStateListener { loadStates ->
            postsViewModel.updateLoadingState(loadStates.refresh)
        }

    }

    private fun bindAdapters() {
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                postsViewModel.pagedPostsNew
                    .collect() {
                        pagedAdapterNew.submitData(it)
                    }
            }
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                postsViewModel.pagedPostsBest
                    .collect() {
                        pagedAdapterBest.submitData(it)
                    }
            }
    }

    private fun refreshAdapters() {
        if (binding.searchView.isDirty) {
            pagedAdapterSearch.refresh()
        } else if (binding.switchButton.isChecked) {
            pagedAdapterBest.refresh()
        } else {
            pagedAdapterNew.refresh()
        }
    }

    private fun refreshUI(it: State) {
        when (it) {
            State.Error -> {
                binding.errorLayout.visibility = View.VISIBLE
            }
            State.Loading -> {
                binding.errorLayout.visibility = View.GONE
            }
            State.Success -> {
                binding.errorLayout.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClick(item: String) {
        val action = PostsFragmentDirections.actionNavigationSubredditsToDetailPostFragment(item)
        findNavController().navigate(action)
    }

    private fun onRedditClick(item: String) {
        val action =
            PostsFragmentDirections.actionNavigationSubredditsToSubredditDialogFragment(item)
        findNavController().navigate(action)
    }

    private fun onUserClick(item: String) {
        val action =
            PostsFragmentDirections.actionNavigationSubredditsToUserDialogFragment(item)
        findNavController().navigate(action)
    }
}