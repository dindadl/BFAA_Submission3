package Adapter

import Model.UserData
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*
import submission.submission2_3.DetailActivity
import submission.submission2_3.R

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(user: UserData)
    }
    var listFav = ArrayList<UserData>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                listFav.clear()
            }
            listFav.addAll(listFavorite)
            notifyDataSetChanged()
        }

    inner class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserData) {
            with(itemView) {
                Glide.with(context)
                    .load(user.avatar)
                    .into(avatar_img)
                txt_username.text = user.username
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)

                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USER, user)
                    intent.putExtra(DetailActivity.EXTRA_FAVORITE, "UserFavorite")
                    context.startActivity(intent)


                }
            }
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
    return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFav[position])
    }

    override fun getItemCount(): Int = listFav.size
}