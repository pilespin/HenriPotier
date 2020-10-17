package com.example.henripotier.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.henripotier.R
import com.example.henripotier.basket.BasketActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "La bibliothèque d'Henri Potier"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val allBooks = mutableListOf<Book>()

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "http://henri-potier.xebia.fr/books"
        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                Log.i("---------LOG---------", "GET: ${response.length()} books")
                for (i in 0 until response.length()) {
                    val json: JSONObject = response.getJSONObject(i)
                    val book = Book(
                        json.get("isbn").toString(),
                        json.get("title").toString(),
                        json.getInt("price"),
                        json.get("cover").toString(),
                        json.get("synopsis").toString()
                    )
                    allBooks.add(book)
//                    Log.w("---------LOG---------", user.toString())

                }
                loading.visibility = View.GONE
                listUsers.layoutManager = StaggeredGridLayoutManager(
                    1,
                    StaggeredGridLayoutManager.VERTICAL
                )
                listUsers.adapter = BookAdapter(this, allBooks)
            },
            { error ->
                loading.text = getString(R.string.errorLoading)
                Log.i("---------LOG---------", "Error loading json: $error")
            }
        )

        // Add the request to the RequestQueue.
        jsonObjectRequest.setShouldCache(true)
        queue.add(jsonObjectRequest)

        basket.setOnClickListener {
            Log.i("---------LOG---------", "GO BASKET")
            val intent = Intent(this, BasketActivity::class.java)
            intent.putExtra("books", allBooks.filter { it.isInBasket }.toTypedArray() )
            startActivity(intent)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}