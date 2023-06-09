package com.example.humblr.ui.detailpost

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import com.example.humblr.R
import com.example.humblr.databinding.FragmentDetailPostBinding
import com.example.humblr.domain.CommentDetailPostAdapter
import com.example.humblr.domain.HeaderDetailPostAdapter
import com.example.humblr.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPostFragment : Fragment() {

    private var _binding: FragmentDetailPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailPostViewModel by viewModels()
    val args: DetailPostFragmentArgs by navArgs()
    lateinit var headerAdapter: HeaderDetailPostAdapter
    lateinit var commentDetailPostAdapter: CommentDetailPostAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createMenu()
        _binding = FragmentDetailPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.loadPost(args.postUrl)

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.state
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
                                if (viewModel.postDetail.value != null) {
                                    headerAdapter = HeaderDetailPostAdapter(
                                        viewModel.postDetail.value!!.first,
                                        { String -> onUserClick(String) },
                                        { String, Boolean -> onDownloadClick(String, Boolean) })
                                    commentDetailPostAdapter = CommentDetailPostAdapter(viewModel.postDetail.value!!.second)
                                    binding.RVPostAndComments.adapter =
                                        ConcatAdapter(headerAdapter, commentDetailPostAdapter)
                                    setTitle(viewModel.postDetail.value!!.first.data.subreddit_name_prefixed)
                                }
                            }
                        }
                    }
            }

        binding.refreshButton.setOnClickListener {
            viewModel.loadPost(args.postUrl)
        }

        return root
    }


    private fun createMenu() {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.menu_post_detail, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> findNavController().popBackStack()
                    R.id.info_button -> {
                        val action = DetailPostFragmentDirections
                            .actionDetailPostFragmentToSubredditDialogFragment(
                                viewModel.postDetail.value!!.first.data.subreddit_name_prefixed
                            )
                        findNavController().navigate(action)
                    }

                }
                return true
            }
        }, viewLifecycleOwner)
    }

    private fun setTitle(title: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.setTitle(title)
    }

    private fun onUserClick(item: String) {
        val action = DetailPostFragmentDirections.actionDetailPostFragmentToUserDialogFragment(item)
        findNavController().navigate(action)
    }

    private fun onDownloadClick(item: String, isDownloaded: Boolean) {
        if (isDownloaded) {
            viewModel.unsavePost(item)
            Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show()

        } else {
            viewModel.savePost(item)
            Toast.makeText(context, "Post saved", Toast.LENGTH_SHORT).show()
        }
    }
}