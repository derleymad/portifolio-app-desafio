package com.github.derleymad.portifolio_app.utils

import android.accounts.NetworkErrorException
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.model.Bio
import com.github.derleymad.portifolio_app.model.SearchBio
import com.github.derleymad.portifolio_app.ui.MainActivity
import org.json.JSONObject
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class BioRequest(private val callback: MainActivity) {

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    interface Callback {
        fun onPreExecute()
        fun onResult(bio: Bio)
        fun onFailure(message: String)
    }

    fun execute(url: String) {
        callback.onPreExecute()
        executor.execute {
            var urlConnection: HttpsURLConnection? = null
            var stream: InputStream? = null

            try {
                val requestURL = URL(url)
                urlConnection = requestURL.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 2000

                val statusCode = urlConnection.responseCode
                Log.e("teste", statusCode.toString())
                if (statusCode != 200) {
                    throw NetworkErrorException(callback.getString(R.string.bio_not_found))
                }
                stream = urlConnection.inputStream
                val jsonAsString = stream.bufferedReader().use { it.readText() }

                val bio = toBio(jsonAsString)
                handler.post { callback.onResult(bio) }

            } catch (e: NetworkErrorException) {
                val message = e.message ?: callback.getString(R.string.uknown_error)
                Log.e("Teste", message, e)
                handler.post { callback.onFailure(message) }
            } finally {
                urlConnection?.disconnect()
                stream?.close()
            }
        }
    }

    private fun toBio(jsonAsString: String): Bio {
        val jsonRoot = JSONObject(jsonAsString)
        return Bio(
            login = jsonRoot.getString("login"),
            id = jsonRoot.getInt("id"),
            avatar_url = jsonRoot.getString("avatar_url"),
            repos_url = jsonRoot.getString("repos_url"),
            company = jsonRoot.getString("company"),
            location = jsonRoot.getString("location"),
            bio = jsonRoot.getString("bio"),
            public_repos = jsonRoot.getInt("public_repos"),
            followers = jsonRoot.getInt("followers"),
            following = jsonRoot.getInt("following")
        )
    }
}