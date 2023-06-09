package com.example.humblr.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.humblr.MainActivity
import com.example.humblr.R
import com.example.humblr.auth.LocalStorage
import com.example.humblr.databinding.FragmentUserBinding
import com.example.humblr.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
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
                                if (userViewModel.myData.value != null) {
                                    binding.name.text = userViewModel.myData.value!!.name
                                    binding.numKarma = userViewModel.myData.value!!.total_karma
                                    binding.numFriends = userViewModel.myData.value!!.num_friends
                                    Glide.with(binding.root.context)
                                        .load(userViewModel.myData.value!!.snoovatar_img)
                                        .placeholder(R.drawable.avatar_default_1)
                                        .into(binding.avatarPicture)
                                }
                            }
                        }
                    }
            }


        binding.logoutButton.setOnClickListener {
            LocalStorage.logout = true
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.buttonFriendsList.setOnClickListener {
            userViewModel.getMyFriends()
            findNavController().navigate(UserFragmentDirections.actionNavigationUserToFriendsFragment())
        }

        binding.buttonDeleteSaved.setOnClickListener {
            Toast.makeText(context, "Local data deleted", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}