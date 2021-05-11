package Fragment

import Adapter.FollowingAdapter
import ViewModel.FollowingViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_following.*
import submission.submission2_3.R


class FollowingFragment : Fragment() {

    private lateinit var adapter: FollowingAdapter
    private lateinit var followingVM: FollowingViewModel

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)
        Log.d("fragmentfollow", "${username}")

        showLoading(true)
        showRecyclerView()
        //showLoading(true)

        followingVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)

        if (username != null) {
            Log.d(TAG,username)
            followingVM.setFollowing(username)
        }
        followingVM.getFollowing().observe(viewLifecycleOwner) {
            adapter.setData(it)
            showLoading(false)
        }
        return
    }


    private fun showRecyclerView() {
        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()

        rv_following_user.layoutManager = LinearLayoutManager(context)
        rv_following_user.adapter = adapter
        rv_following_user.setHasFixedSize(true)
    }
    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFollow?.visibility = View.VISIBLE
        } else {
            progressBarFollow?.visibility = View.GONE
        }
    }
}