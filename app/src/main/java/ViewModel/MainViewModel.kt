package ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import Model.UserData

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainViewModel : ViewModel() {

    private val listUser = MutableLiveData<ArrayList<UserData>>()

    fun setUser(username: String) {
        val listItems = ArrayList<UserData>()
        val tokens = "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"
        val url = "https://api.github.com/search/users?q=$username"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token: $tokens")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = responseBody?.let { String(it) }
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")
                    for (i in 0 until list.length()) {
                        val item = list.getJSONObject(i)
                        val user = UserData()
                        user.avatar = item.getString("avatar_url")
                        user.username = item.getString("login")
                        listItems.add(user)
                    }
                    listUser.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("MainViewModel", "on Success: ${e.message.toString()}")
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<UserData>> {
        return listUser
    }
}

