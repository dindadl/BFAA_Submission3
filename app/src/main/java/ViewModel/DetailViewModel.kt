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

@Suppress("NAME_SHADOWING")
class DetailViewModel: ViewModel() {

    private val detailUser = MutableLiveData<UserData>()

    fun setDetail(username: String){
        val tokens = "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"
        val url = "https://api.github.com/users/${username}"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $tokens")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObjects = JSONObject(result)
                    val user = UserData()
                    //user.name = responseObjects.getString("name")
                    user.username = responseObjects.getString("login")
                    user.company = responseObjects.getString("company")
                    user.location = responseObjects.getString("location")
                    user.repository = responseObjects.getInt("public_repos")
                    user.following = responseObjects.getInt("following")
                    user.followers = responseObjects.getInt("followers")

                    //val user = UserData(
                      //      name,
                        //    username,
                          //  company,
                            //location,
                            //repository,
                            //following,
                            //followers,
                    //)
                    detailUser.postValue(user)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
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

fun getDetail(): LiveData<UserData> {
    return detailUser
}
}
