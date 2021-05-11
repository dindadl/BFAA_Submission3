package submission.submission2_3

import Adapter.SectionPagerAdapter
import Helper.MappingHelper
import Model.UserData
import ViewModel.DetailViewModel
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import database.DatabaseContract.FavoriteColumns.Companion.AVATAR
import database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import database.FavoriteHelper
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_followers.view.*
import kotlinx.android.synthetic.main.item_follows.*
import kotlinx.android.synthetic.main.item_user.*
import submission.submission2_3.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailVM: DetailViewModel
    private lateinit var uriWithId: Uri
    private var position: Int = 0
    private lateinit var favoriteHelper: FavoriteHelper
    private var userDetail: UserData? = null
    private var favStat = false

    companion object {

        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

    }

    private fun setTitle(username: String) {
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = username
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        showLoading(true)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        userDetail = intent.getParcelableExtra<UserData>(EXTRA_USER) as UserData


        detailVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)

        detailVM.setDetail(userDetail!!.username)
        detailVM.getDetail().observe(this, { user ->
            if (user != null) {
                showLoading(false)
                //  detail_username.text = user.username
                detail_company.text = user.company
                detail_location.text = user.location
                repository_det.text = user.repository.toString()
                following_det.text = user.following.toString()
                followers_det.text = user.followers.toString()
            }
            showLoading(false) })

        Glide.with(this@DetailActivity)
            .load(userDetail!!.avatar)
            .into(detail_avatar)
        detail_username.text = userDetail!!.username
        detail_company.text = userDetail!!.company
        detail_location.text = userDetail!!.location
        repository_det.text = userDetail!!.repository.toString()
        following_det.text = userDetail!!.following.toString()
        followers_det.text = userDetail!!.followers.toString()

        setTitle(userDetail!!.username)

        sectionConfig(userDetail!!.username)

        checkFav()
        setFavData()
        fab_favorite.setOnClickListener(this)
       // Log.d("Favorite data: ", favorite.toString())

    }


    private fun sectionConfig(username: String) {
        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = username
        view_pager?.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                return true
            }
            R.id.action_menu_1 -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_fav -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkFav() {
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + userDetail?.username)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        val myFav = MappingHelper.mapCursorToArrayList(cursor)
        for (data in myFav) {
            if (userDetail?.username == data.username) {
                favStat = true
                fab_favorite.setImageResource(R.drawable.fav_blue)

            }
        }
    }

    private fun setFavData() {
        if (userDetail != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)

            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + userDetail?.username)
            val cursor = contentResolver?.query(uriWithId, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                userDetail = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
        }
    }


    override fun onClick(v: View) {
        if (v.id == R.id.fab_favorite) {
            if (favStat) {
                contentResolver.delete(uriWithId, null, null)
                showToast(userDetail?.username + " " + this.getString(R.string.removefav))
                fab_favorite.setImageResource(R.drawable.fav_no_blue)
                favStat = false
            } else {
                val values = ContentValues()
                values.put(USERNAME, userDetail?.username)
                values.put(AVATAR, userDetail?.avatar)
                fab_favorite.setImageResource(R.drawable.fav_blue)
                contentResolver.insert(CONTENT_URI, values)
                // userDetail?.username
                val result = contentResolver.insert(CONTENT_URI, values)
                favStat = true
                if (result?.lastPathSegment?.toInt()!! > 0) {
                    showToast(userDetail?.username + "  " + this.getString(R.string.addFav))
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }


    private fun setFavIcon(favStat: Boolean){
        if (favStat){
            fab_favorite.setImageResource(R.drawable.fav_blue)
        } else{
           fab_favorite.setImageResource(R.drawable.fav_no_blue)
        }
    }
    fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(applicationContext, message, duration).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarDetail?.visibility = View.VISIBLE
        } else {
            progressBarDetail?.visibility = View.GONE
        }

    }

}







