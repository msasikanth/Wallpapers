package android.myapplication.ui

import android.app.FragmentManager
import android.content.Intent
import android.myapplication.R
import android.myapplication.adapters.WallAdapter
import android.myapplication.models.Wallpapers
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {

    val wallpapersList: ArrayList<Wallpapers>? = ArrayList()
    val aboutDialog: AboutDialog = AboutDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Wallpapers"

        WallpapersAsync(wallpapersList, activity_main).execute(resources.getString(R.string.json_link))

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_open_source -> {
                val intent: Intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://github.com/sasikanth01/Wallpapers-Kotlin")
                startActivity(intent)
            }

            R.id.action_about -> {
                val fragmentManager: FragmentManager= fragmentManager
                aboutDialog.show(fragmentManager, "about")
                aboutDialog.isCancelable = true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class WallpapersAsync(var wallpapersList: ArrayList<Wallpapers>?, val mainView: CoordinatorLayout) : AsyncTask<String, Void, Int>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg string1: String?): Int {
            var result = 0

            try {
                val url = URL(string1[0])
                val urlConnection = url.openConnection() as HttpURLConnection
                val statusCode = urlConnection.responseCode

                if (statusCode == 200) {

                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()
                    var line: String? = null

                    while ({ line = reader.readLine(); line }() != null) {
                        response.append(line)
                    }
                    parseResult(response.toString())
                    result = 1
                } else {
                    result = 0
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)

            if (wallpapersList != null) {
                val adapter: WallAdapter = WallAdapter(wallpapersList, this@MainActivity)
                Snackbar.make(mainView, "Loading Done", Snackbar.LENGTH_SHORT).show()
                recyclerView.adapter = adapter
                progressBar.visibility = GONE
            }

        }

        fun parseResult(result: String?) {
            val response: JSONObject = JSONObject(result)
            val posts: JSONArray = response.getJSONArray("All")

            for (i in 0..posts.length()) {
                val post: JSONObject = posts.optJSONObject(i)

                val wallInfo = Wallpapers(post.getString("name"), post.getString("small_url"), post.getString("url"))
                wallpapersList?.add(wallInfo)
            }

        }

    }
}