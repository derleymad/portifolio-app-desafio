package com.github.derleymad.portifolio_app.utils

import android.accounts.NetworkErrorException
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.model.Repos
import com.github.derleymad.portifolio_app.ui.MainActivity
import org.json.JSONArray
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class RepoRequest(private val callback: MainActivity) {

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    interface Callback {
        fun onPreExecuteRepo()
        fun onResultRepo(repos: List<Repos>)
        fun onFailureRepo(message: String)
    }

    fun execute(url: String) {
        callback.onPreExecuteRepo()
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

                val repos = toRepos(jsonAsString)
                handler.post { callback.onResultRepo(repos) }

            } catch (e: NetworkErrorException) {
                val message = e.message ?: callback.getString(R.string.uknown_error)
                Log.e("Teste", message, e)
                handler.post { callback.onFailureRepo(message) }
            } finally {
                urlConnection?.disconnect()
                stream?.close()
            }
        }
    }

    private fun toRepos(jsonAsString: String): MutableList<Repos> {
        var repos = mutableListOf<Repos>()

        val jsonRoot = JSONArray(jsonAsString)

        for (i in 0 until jsonRoot.length()) {
            val jsonRepo = jsonRoot.getJSONObject(i)
            val jsonOwner = jsonRepo.getJSONObject("owner")
            val id = jsonRepo.getInt("id")
            val name = jsonRepo.getString("name")
            val fullName = jsonRepo.getString("full_name")
            val avatarUrl = jsonOwner.getString("avatar_url")
            val language = jsonRepo.getString("language")
            var description = jsonRepo.getString("description")

            repos.add(
                Repos(
                    id = id,
                    name = name,
                    fullName = fullName,
                    avatarUrl = avatarUrl,
                    language = language,
                    description = description
                )
            )
        }
        return repos
    }
}