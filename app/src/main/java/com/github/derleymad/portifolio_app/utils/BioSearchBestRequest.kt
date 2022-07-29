package com.github.derleymad.portifolio_app.utils

import android.accounts.NetworkErrorException
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.derleymad.portifolio_app.R
import com.github.derleymad.portifolio_app.model.SearchBio
import com.github.derleymad.portifolio_app.ui.SearchActivity
import org.json.JSONObject
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class BioSearchBestRequest (private val callback: SearchActivity){

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    interface Callback{
        fun onPreExecuteBest()
        fun onResultBest(best: List<SearchBio>)
        fun onFailureBest(message: String)
    }

    fun execute(url:String){
        callback.onPreExecuteBest()
        executor.execute{
            var urlConnection: HttpsURLConnection? = null
            var stream : InputStream? = null

            try{
                val requestURL = URL(url)
                urlConnection = requestURL.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 2000

                val statusCode = urlConnection.responseCode
                Log.e("teste",statusCode.toString())
                if(statusCode!=200){
                    throw NetworkErrorException(callback.getString(R.string.bio_not_found))
                }
                stream = urlConnection.inputStream
                val jsonAsString = stream.bufferedReader().use { it.readText() }

                val best = toBest(jsonAsString)
                handler.post{callback.onResultBest(best)}

            }catch (e: NetworkErrorException){
                val message = e.message ?: callback.getString(R.string.uknown_error)
                Log.e("Teste",message,e)
                handler.post { callback.onFailureBest(message)  }
            }finally {
                urlConnection?.disconnect()
                stream?.close()
            }
        }
    }

    private fun toBest(jsonAsString: String): List<SearchBio> {
        val best = mutableListOf<SearchBio>()
        val jsonRoot = JSONObject(jsonAsString)

        val jsonItems = jsonRoot.getJSONArray("items")

        for (i in 0 until jsonItems.length()) {
            val userJson = jsonItems.getJSONObject(i)
            best.add(
                SearchBio(
                    login = userJson.getString("login"),
                    id = userJson.getInt("id"),
                    avatar_url = userJson.getString("avatar_url"),
                    url = userJson.getString("url")
                )
            )
        }
        return best
    }
}