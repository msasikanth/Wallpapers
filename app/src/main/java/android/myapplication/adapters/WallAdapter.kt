package android.myapplication.adapters

import android.app.Activity
import android.content.Intent
import android.myapplication.R
import android.myapplication.models.Wallpapers
import android.myapplication.ui.DetailView
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.wall_item.view.*
import java.util.*

class WallAdapter(val itemData: ArrayList<Wallpapers>?, val mActivity: Activity) : RecyclerView.Adapter<WallAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wall_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val jsonData = itemData?.get(position) as Wallpapers

        Glide.with(mActivity).load(jsonData.Link).into(holder.wallpaper)
        holder.name?.text = jsonData.Name

        holder.itemView.setOnClickListener {
            val intent = Intent (mActivity, DetailView::class.java)
            intent.putExtra("wallpaper", jsonData.BigWall)
            intent.putExtra("name", jsonData.Name)
            val bundle: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, holder.itemView,"wallpaper")
            mActivity.startActivity(intent, bundle.toBundle())
        }

    }

    override fun getItemCount(): Int {
        if (itemData != null) return itemData.size
        return 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val wallpaper: ImageView? = itemView.wallPaper
        val name: TextView? = itemView.name
    }
}
