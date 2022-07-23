package com.github.derleymad.portifolio_app.utils

import android.accounts.NetworkErrorException
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.derleymad.portifolio_app.model.Repos
import com.github.derleymad.portifolio_app.ui.SearchActivity
import org.json.JSONArray
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class RepoRequest (private val callback: SearchActivity){

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    interface Callback{
        fun onPreExecute()
        fun onResult(repos:List<Repos>)
        fun onFailure(message: String)
    }

    fun execute(url:String){
        callback.onPreExecute()
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
                if(statusCode>400){
                    throw NetworkErrorException("Erro ao buscar bio")
                }
                stream = urlConnection.inputStream
                val jsonAsString = stream.bufferedReader().use { it.readText() }

                val repos = toRepos(jsonAsString)
                handler.post{callback.onResult(repos)}

            }catch (e:NetworkErrorException){
                val message = e.message ?: "Erro desconhecido"
                Log.e("Teste",message,e)
                handler.post { callback.onFailure(message)  }
            }finally {
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
            val avatarUrl = jsonOwner.getString("avatar_url")
            val language = jsonRepo.getString("language")
            var description = jsonRepo.getString("description")

            repos.add(
                Repos(
                    id = id,
                    name = name,
                    avatarUrl = avatarUrl,
                    language = language,
                    description = description
                )
            )
        }
        return repos
    }
}