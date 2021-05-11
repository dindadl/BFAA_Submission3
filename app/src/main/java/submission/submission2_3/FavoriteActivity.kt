package submission.submission2_3

import Adapter.FavoriteAdapter
import Helper.MappingHelper
import Model.UserData
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import submission.submission2_3.databinding.ActivityMainBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.title = "Favorite User"
        showLoading(true)

        rv_fav.layoutManager = LinearLayoutManager(this)
        rv_fav.setHasFixedSize(true)


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoriteAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavoriteAsync()
        } else {
            savedInstanceState.getParcelableArrayList<UserData>(EXTRA_STATE)?.also { adapter.listFav = it }
        }

        showFavRecyclerView()


    }

    private fun loadFavoriteAsync() {
        showLoading(false)
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
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
        adapter = FavoriteAdapter()
        rv_fav.adapter = adapter
        rv_fav.layoutManager = LinearLayoutManager(this)
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
            R.id.action_menu_1 ->{
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)

    }
    fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(applicationContext, message, duration).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFav.visibility = View.VISIBLE
        } else {
            progressBarFav.visibility = View.GONE
        }
    }
    override fun onResume() {
        super.onResume()
        loadFavoriteAsync()
    }

}