package android.myapplication.ui

import android.app.ProgressDialog
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.myapplication.R
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.transition.Transition
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.INVISIBLE
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.detail_view.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DetailView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_view)

        val link = intent.extras.getString("wallpaper")
        val name = intent.extras.getString("name")

        title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        apply.hide()

        Glide.with(this).load(link).asBitmap().listener(object : RequestListener<String, Bitmap> {
            override fun onException(e: Exception, model: String, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Bitmap, model: String, target: Target<Bitmap>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                detailProgress.visibility = GONE
                apply.show()
                return false
            }
        }).into(bigWall)

        apply.setOnClickListener {
            bitmapAsync(link).execute()
        }

    }

    inner class bitmapAsync(src: String) : AsyncTask<Void, Void, Bitmap>() {

        val url: String = src
        val progressDialog: ProgressDialog = ProgressDialog(this@DetailView)

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.setMessage("Applying Wallpaper")
            progressDialog.isIndeterminate = true
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

        override fun doInBackground(vararg p0: Void?): Bitmap {
            val url: URL = URL(url)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val bitmap: Bitmap = BitmapFactory.decodeStream(input)

            //Applying Wallpaper in background
            val wallpaperManage: WallpaperManager = WallpaperManager.getInstance(this@DetailView)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManage.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                wallpaperManage.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
            } else {
                wallpaperManage.setBitmap(bitmap)
            }

            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
            Snackbar.make(detailView, "Wallpaper Applied", Snackbar.LENGTH_LONG).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        apply.hide()
        supportFinishAfterTransition()
    }

}
