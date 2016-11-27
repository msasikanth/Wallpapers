package android.myapplication.ui

import android.app.DialogFragment
import android.content.Intent
import android.myapplication.R
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.about_dialog.*


class AboutDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dialogView: View = inflater!!.inflate(R.layout.about_dialog, container, false)
        return dialogView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        googlePlus.setOnClickListener {
            val uri: Uri = Uri.parse(resources.getString(R.string.googlePlus))
            val gPlus: Intent = Intent(Intent.ACTION_VIEW, uri)
            gPlus.`package` = "com.google.android.apps.plus"
            startActivity(gPlus)
        }

        gitHub.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.gitHub))))
        }

        twitter.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.twitter_link))))
        }

    }

}
