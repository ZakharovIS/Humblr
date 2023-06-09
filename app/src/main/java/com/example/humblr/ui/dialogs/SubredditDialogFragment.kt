package com.example.humblr.ui.dialogs

import android.content.Intent
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
import com.example.humblr.R
import com.example.humblr.databinding.DialogSubredditInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubredditDialogFragment : DialogFragment() {

    private var _binding: DialogSubredditInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SubredditDialogViewModel by viewModels()
    private val args: SubredditDialogFragmentArgs by navArgs()

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
        _binding = DialogSubredditInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.loadSubredditInfo(args.subredditName)
        binding.closeIcon.setOnClickListener {
            findNavController().popBackStack()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.subredditInfo.collect {
                if (it != null) {
                    binding.nameSubreddit.text = it.display_name_prefixed
                    binding.titleSubreddit.text = it.title
                    binding.descriptionSubreddit.text = it.public_description
                    binding.isSubscribed = it.user_is_subscriber
                    binding.subscribeButton.setOnClickListener { view ->
                        if (binding.isSubscribed == true) {
                            viewModel.unSubscribe(it.name)
                            binding.isSubscribed = false
                        } else if (binding.isSubscribed == false) {
                            viewModel.subscribe(it.name)
                            binding.isSubscribed = true
                        }
                    }
                    binding.share.setOnClickListener {view->
                        share("https://www.reddit.com/" + it.display_name_prefixed)
                    }
                }
            }
        }



        return root
    }

    private fun share(url: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                url
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}