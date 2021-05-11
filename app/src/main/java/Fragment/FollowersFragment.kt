package Fragment

import Adapter.FollowersAdapter
import ViewModel.FollowersViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_followers.*
import submission.submission2_3.R

class FollowersFragment : Fragment() {

    private lateinit var adapter: FollowersAdapter
    private lateinit var followersVM: FollowersViewModel

    companion object{
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)

        showLoading(true)

        showRecyclerView()


        followersVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)
        if (username != null) {
            followersVM.setFollowers(username.toString())
        }
        followersVM.getFollowers().observe(viewLifecycleOwner){
                adapter.setData(it)
                showLoading(false) }
    }

    private fun showRecyclerView() {

        adapter = FollowersAdapter()
        adapter.notifyDataSetChanged()

        rv_followers_user.layoutManager = LinearLayoutManager(context)
        rv_followers_user.adapter = adapter

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFollowz?.visibility = View.VISIBLE
        } else {
            progressBarFollowz?.visibility = View.GONE
        }
    }
}