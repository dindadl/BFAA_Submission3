package submission.submission2_3

import Adapter.UserAdapter
import Model.UserData
import ViewModel.MainViewModel
import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import submission.submission2_3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var mainVM: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)


        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        mainVM = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        mainVM.getUser().observe(this, { userItems ->
            if (userItems.size > 0) {
                showLoading(true)
                adapter.setData(userItems)
                showLoading(false)
            } else {
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        }
        )

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchbar

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getDataUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        showRecyclerList()
    }




    private fun showRecyclerList() {
        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.adapter = adapter
        rv_user.setHasFixedSize(true)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserData) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: UserData) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(intent)
    }

    private fun getDataUser(username: String) {
        if (username.isEmpty()) return
        showLoading(true)
        mainVM.setUser(username)
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

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarMain?.visibility = View.VISIBLE
        } else {
            progressBarMain?.visibility = View.GONE
        }
    }
}
