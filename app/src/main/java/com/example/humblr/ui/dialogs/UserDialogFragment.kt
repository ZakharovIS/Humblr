package com.example.humblr.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.humblr.R
import com.example.humblr.databinding.DialogUserInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UserDialogFragment : DialogFragment() {

    private var _binding: DialogUserInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserDialogViewModel by viewModels()
    val args: UserDialogFragmentArgs by navArgs()

    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogUserInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.loadUserInfo(args.userName)
        binding.closeIcon.setOnClickListener {
            findNavController().popBackStack()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userInfo.collect() {
                if (it != null) {
                    binding.name.text = it.name
                    Glide.with(binding.root.context)
                        .load(it.snoovatar_img)
                        .placeholder(R.drawable.avatar_default_1)
                        .fitCenter()
                        .into(binding.avatar)
                    binding.karma = it.total_karma.toString()
                    binding.sinceData = SimpleDateFormat(
                        "dd-MM-yyyy", Locale.US
                    ).format(it.created!! * 1000).toString()
                    binding.isFriend = it.is_friend
                    binding.subscribeButton.setOnClickListener {view->
                        if (binding.isFriend == true) {
                            viewModel.deleteFriend(it.name)
                            binding.isFriend = false
                        } else {
                            viewModel.beFriends(it.name)
                            binding.isFriend = true
                        }
                    }

                }
            }
        }


        return root
    }
}