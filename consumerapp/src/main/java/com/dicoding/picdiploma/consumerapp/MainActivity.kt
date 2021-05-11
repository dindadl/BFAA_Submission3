package com.dicoding.picdiploma.consumerapp

import Adapter.FavoriteAdapter
import com.dicoding.picdiploma.consumerapp.Helper.MappingHelper
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.dicoding.picdiploma.consumerapp.database.DatabaseContract
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity :  AppCompatActivity() {


private lateinit var adapter: FavoriteAdapter

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_favorite)

    supportActionBar?.title = "Favorite User"
    showLoading(true)
 loadFavorite()
    showFavRecyclerView()
}

private fun loadFavorite() {
    GlobalScope.launch(Dispatchers.Main) {
        val deferredFav = async(Dispatchers.IO) {
            val cursor = contentResolver?.query(DatabaseContract.FavoriteColumns.CONTENT_URI, null, null, null, null)
            MappingHelper.mapCursorToArrayList(cursor)
        }
        val fav = deferredFav.await()
        if (fav.size > 0) {
            adapter.listFav = fav
        } else {
            adapter.listFav = ArrayList()
            showToast("Tidak Ada Data Saat Ini")
        }
    }
}

private fun showFavRecyclerView() {
    showLoading(false)
    adapter = FavoriteAdapter()
    rv_fav.adapter = adapter
    rv_fav.layoutManager = LinearLayoutManager(this)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, message, duration).show()
}
    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarfav?.visibility = View.VISIBLE
        } else {
            progressBarfav?.visibility = View.GONE
        }
    }}