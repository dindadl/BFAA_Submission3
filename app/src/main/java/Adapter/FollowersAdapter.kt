package Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_follows.view.*
import submission.submission2_3.R
import Model.UserData

class FollowersAdapter: RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {

    private val listFollowersUser = ArrayList<UserData>()

    fun setData(item: ArrayList<UserData>) {
        listFollowersUser.clear()
        listFollowersUser.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int): FollowersViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_follows, viewGroup, false)
        return FollowersViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(listFollowersUser[position])
    }

    override fun getItemCount(): Int = listFollowersUser.size

    inner class FollowersViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserData) {
            with(itemView) {
                Glide.with(this)
                    .load(user.avatar)
                    .into(avatar_img_follows)
                txt_username_follows.text = user.username
            }
        }

    }
}