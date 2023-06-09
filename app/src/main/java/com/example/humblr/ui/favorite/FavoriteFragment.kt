package com.example.humblr.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.humblr.databinding.FragmentFavoriteBinding
import com.example.humblr.domain.PostsAdapter
import com.example.humblr.domain.SubredditsAdapter
import com.example.humblr.ui.posts.PostsFragmentDirections
import com.example.humblr.ui.posts.PostsViewModel
import com.example.humblr.utils.State
import com.example.humblr.utils.SwitchState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val pagedAdapterSubscribedPostsNew = PostsAdapter(
        onClick = { String -> onItemClick(String) },
        onClickSubreddit = { String -> onRedditClick(String) },
        onClickUser = { String -> onUserClick(String) })
    private val pagedAdapterSavedPosts = PostsAdapter(
        onClick = { String -> onItemClick(String) },
        onClickSubreddit = { String -> onRedditClick(String) },
        onClickUser = { String -> onUserClick(String) })
    private val pagedAdapterAllSubreddits = SubredditsAdapter() { String, Boolean -> onSubscribeClick(String,Boolean) }
    private val pagedAdapterSubscribedSubreddits = SubredditsAdapter() { String, Boolean -> onSubscribeClick(String,Boolean) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.RVFavoritesList.adapter = pagedAdapterSubscribedPostsNew

        bindAdapters()

        binding.refreshButton.setOnClickListener {
            refreshAdapters()
        }

        binding.swipeRefresh.setOnRefreshListener {
            refreshAdapters()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            favoriteViewModel.switchStateData
                .collect() {

                    switchAdapters(it)

                }
        }

        bindSwitchesToSwitchState()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            favoriteViewModel.state.collect() {
                binding.swipeRefresh.isRefreshing = favoriteViewModel.state.value == State.Loading
                refreshUI(it)
            }
        }

        bindLoadStateToState()

        return root
    }

    private fun bindLoadStateToState() {
        pagedAdapterSubscribedPostsNew.addLoadStateListener { loadStates ->
            favoriteViewModel.updateLoadingState(loadStates.refresh)
        }
        pagedAdapterSavedPosts.addLoadStateListener { loadStates ->
            favoriteViewModel.updateLoadingState(loadStates.refresh)
        }
        pagedAdapterAllSubreddits.addLoadStateListener { loadStates ->
            favoriteViewModel.updateLoadingState(loadStates.refresh)
        }
        pagedAdapterSubscribedSubreddits.addLoadStateListener { loadStates ->
            favoriteViewModel.updateLoadingState(loadStates.refresh)
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

    private fun refreshAdapters() {
        when (favoriteViewModel.switchStateData.value) {
            SwitchState.SUBREDDITS_ALL -> pagedAdapterAllSubreddits.refresh()
            SwitchState.POSTS_ALL -> pagedAdapterSubscribedPostsNew.refresh()
            SwitchState.SUBREDDITS_SAVED -> pagedAdapterSubscribedSubreddits.refresh()
            SwitchState.POSTS_SAVED -> pagedAdapterSavedPosts.refresh()
        }
    }

    private fun switchAdapters(it: SwitchState) {
        when (it) {
            SwitchState.SUBREDDITS_ALL -> {
                binding.RVFavoritesList.adapter = pagedAdapterAllSubreddits
            }
            SwitchState.POSTS_ALL -> {
                binding.RVFavoritesList.adapter = pagedAdapterSubscribedPostsNew
            }
            SwitchState.SUBREDDITS_SAVED -> {
                binding.RVFavoritesList.adapter = pagedAdapterSubscribedSubreddits
            }
            SwitchState.POSTS_SAVED -> {
                binding.RVFavoritesList.adapter = pagedAdapterSavedPosts
            }
        }
    }

    private fun bindSwitchesToSwitchState() {
        binding.switchButtonSubredditsPosts.setOnCheckedChangeListener { _, isChecked ->
            favoriteViewModel.updateSwitchState(
                isChecked,
                binding.switchButtonAllSaved.isChecked
            )
        }
        binding.switchButtonAllSaved.setOnCheckedChangeListener { _, isChecked ->
            favoriteViewModel.updateSwitchState(
                binding.switchButtonSubredditsPosts.isChecked,
                isChecked
            )
        }

    }


    private fun bindAdapters() {
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                favoriteViewModel.subscribedSubredditsPostsNew
                    .collect() {
                        pagedAdapterSubscribedPostsNew.submitData(it)
                    }
            }
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                favoriteViewModel.subscribedSubreddits
                    .collect() {
                        pagedAdapterSubscribedSubreddits.submitData(it)
                    }
            }
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                favoriteViewModel.allSubreddits
                    .collect() {
                        pagedAdapterAllSubreddits.submitData(it)
                    }
            }
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                favoriteViewModel.savedPosts
                    .collect() {
                        pagedAdapterSavedPosts.submitData(it)
                    }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClick(item: String) {
        val action = FavoriteFragmentDirections.actionNavigationFavoriteToDetailPostFragment(item)
        findNavController().navigate(action)
    }

    private fun onRedditClick(item: String) {
        val action =
            FavoriteFragmentDirections.actionNavigationFavoriteToSubredditDialogFragment(item)
        findNavController().navigate(action)
    }

    private fun onUserClick(item: String) {
        val action =
            FavoriteFragmentDirections.actionNavigationFavoriteToUserDialogFragment(item)
        findNavController().navigate(action)
    }

    private fun onSubscribeClick(item: String, isSubscribed: Boolean) {
        if(isSubscribed) {
            favoriteViewModel.unSubscribe(item)
        } else {
            favoriteViewModel.subscribe(item)
        }
    }

}