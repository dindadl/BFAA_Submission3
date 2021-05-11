package ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import Model.UserData

class FollowingViewModel: ViewModel() {

    private val listFollowing = MutableLiveData<ArrayList<UserData>>()

    fun setFollowing(username: String) {
        val listItem = ArrayList<UserData>()
        val tokens = "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"
        val url = "https://api.github.com/users/$username/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $tokens")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it)
            }
                try {
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val item = responseArray.getJSONObject(i)
                        val user = UserData()
                        user.avatar = item.getString("avatar_url")
                        user.username = item.getString("login")
                        listItem.add(user)
                    }
                    listFollowing.postValue(listItem)
                } catch (e: Exception) {
                    Log.d("FollowingVM", "${e.message.toString()}")
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure-FollowingVM", error?.message.toString())
            }
        })
    }

    fun getFollowing(): LiveData<ArrayList<UserData>> {
        return listFollowing
    }
}
