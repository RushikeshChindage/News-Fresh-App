package com.example.newsfresh

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       recycleView.layoutManager = LinearLayoutManager(this)
        fetchData()
       val adapter = NewsListAdapter( this)
        recycleView.adapter = adapter
       // loadMeme()
    }

//    private fun loadMeme(){
//        // Instantiate the RequestQueue.
//        progressBar.visibility = View.VISIBLE
//
//        val url = "https://www.google.com"
//
//// Request a string response from the provided URL.
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.GET, url, null,
//            Response.Listener{ response ->
//                currentImageUrl = response.getString("url")
//                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Drawable>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        progressBar.visibility = View.GONE
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable?,
//                        model: Any?,
//                        target: Target<Drawable>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                       progressBar.visibility = View.GONE
//                        return false
//                    }
//                }).into(recycleView)
//            },
//            Response.ErrorListener {
//
//
//            })
//
//// Add the request to the RequestQueue.
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
//    }

    private fun fetchData() {
        val url = "http://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=d1b24532f1d34214a2bf3f319583ca57"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener{
            val newsJSONArray = it.getJSONArray("articles")
            val newsArray = ArrayList<News>()
            for (i in 0 until newsJSONArray.length()){
                val newsJsonObject = newsJSONArray.getJSONObject(i)
                val news = News(
                    newsJsonObject.getString("title"),
                    newsJsonObject.getString("author"),
                    newsJsonObject.getString("url"),
                    newsJsonObject.getString("urlToImage")
                )
                    newsArray.add(news)
            }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {


            }
        )

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
      //  Toast.makeText(this, "clicked item is $item", Toast.LENGTH_LONG).show()

        val builder = CustomTabsIntent.Builder();
        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }


}