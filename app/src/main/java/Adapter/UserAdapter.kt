package Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*
import submission.submission2_3.R
import Model.UserData

class UserAdapter  : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var listData = ArrayList<UserData>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: ArrayList<UserData>) {
        listData.clear()
        listData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int =listData.size

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserData) {
            with(itemView) {
                Glide.with(this)
                    .load(user.avatar)
                    .into(avatar_img)
                txt_username.text = user.username
                itemView.setOnClickListener{ onItemClickCallback?.onItemClicked(user)}
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserData)
    }
}