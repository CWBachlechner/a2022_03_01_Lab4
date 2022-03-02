package com.example.a2022_03_01_lab4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val objectIds = arrayOf(10554,324290,22387,24861,199404,437749,503010)

    private val urlFragment = "https://collectionapi.metmuseum.org/public/collection/v1/objects/"

    private val clientTextView: TextView
        get () = findViewById(R.id.clientTextView)

    private val nextButton: Button
        get () = findViewById(R.id.nextImageButton)

    private val imageView: ImageView
        get() = findViewById(R.id.imageView)

    private var nextObject: Int = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nextButton.setOnClickListener {
            nextObject = (nextObject+1) % objectIds.size
            val objectID = objectIds[nextObject].toString()
            val fullURL = " $urlFragment$objectID"
            getText(fullURL)
        }
    }

    private fun getText(url: String)
    {
        val queue = Volley.newRequestQueue((this))
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val responseObj = JSONObject(response)
                clientTextView.text = "Title: ${responseObj.get("title")}" + "\n Department: ${responseObj.get("department")}" + " \n Culture: ${responseObj.get("culture")}" + " \n Date: ${responseObj.get("objectDate")}"
                getImage(responseObj.get("primaryImage").toString())
            }, { clientTextView.text = "That didn't work!" })
        queue.add(stringRequest)
    }

    private fun getImage(url: String)
    {
        val queue = Volley.newRequestQueue((this))
        val imageRequest = ImageRequest(url,
            { imageRequest ->
                imageView.setImageBitmap(imageRequest)
            },
            0, 0, ImageView.ScaleType.CENTER_CROP, null,
            { error ->
                Log.d("ImageError", "Unable to complete request")
            }
        )
        queue.add(imageRequest)
    }
}