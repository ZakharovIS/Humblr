package com.example.humblr.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.humblr.databinding.FragmentFriendsBinding
import com.example.humblr.domain.FriendsAdapter
import com.example.humblr.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    lateinit var friendsAdapter: FriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                userViewModel.state
                    .collect {
                        when(it) {
                            State.Error -> {
                                binding.errorLayout.visibility = View.VISIBLE
                                binding.loadingLayout.visibility = View.GONE
                            }
                            State.Loading -> {
                                binding.loadingLayout.visibility = View.VISIBLE
                                binding.errorLayout.visibility = View.GONE
                            }
                            State.Success -> {
                                binding.errorLayout.visibility = View.GONE
                                binding.loadingLayout.visibility = View.GONE
                                if (userViewModel.myFriends.value != null) {
                                    friendsAdapter = FriendsAdapter(userViewModel.myFriends.value!!)
                                    binding.RVFriendsList.adapter = friendsAdapter
                                }
                            }
                        }
                    }
            }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}