package android.myapplication.ui

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.myapplication.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.detail_view.*


class DetailView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_view)

        val link = intent.extras.getString("wallpaper")
        val name = intent.extras.getString("name")

        title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Glide.with(this).load(link).asBitmap().listener(object : RequestListener<String, Bitmap> {
            override fun onException(e: Exception, model: String, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Bitmap, model: String, target: Target<Bitmap>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {

                apply.visibility = VISIBLE
                apply.setOnClickListener {
                    val wallpaperManage: WallpaperManager = WallpaperManager.getInstance(this@DetailView)
                    wallpaperManage.setBitmap(resource, null, true, WallpaperManager.FLAG_SYSTEM)
                    wallpaperManage.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK)

                    Toast.makeText(this@DetailView, "Applying Wallpaper", Toast.LENGTH_LONG).show()
                }

                detailProgress.visibility = GONE
                return false
            }
        }).into(bigWall)

    }
}
