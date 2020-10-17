package com.example.henripotier.basket

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.henripotier.R
import com.example.henripotier.main.Book
import kotlinx.android.synthetic.main.activity_basket.*
import kotlinx.android.synthetic.main.activity_main.loading
import org.json.JSONObject


class BasketActivity : AppCompatActivity() {

    fun getNbBookbInBasket(books: MutableList<Book>): Int {
        var nb = 0
        for (b in books) {
            if (b.isInBasket) {
                nb++
            }
        }
        return nb
    }

    fun getBestRemise(ttRemise: Map<String, Any>): Any? {
        var bestRemise: Double = 0.0
        var bestRemiseKey = ""
        for (i in ttRemise) {
            if (bestRemiseKey == "")
                bestRemiseKey = i.key
            Log.i("---------LOG---------", "Find Best: ${i.key} -- ${i.value}")
            val cur = i.value as List<Any>
            val newRemise = cur[0] as Double
            if (newRemise > bestRemise) {
                bestRemise = newRemise
                bestRemiseKey = i.key
            }
        }
        Log.i("---------LOG---------", "Find Real Best: $bestRemiseKey, ${ttRemise[bestRemiseKey]}")
        return (ttRemise[bestRemiseKey])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        try {
            val books = intent.extras?.getParcelableArray("books")!!.toMutableList() as MutableList<Book>
            Log.i("---------LOG---------", "LOAD BOOKS: $books")

            var ttAmount: Double = 0.0
            var ttRemisePercentage: Double = 0.0
            var ttRemiseMinus: Double = 0.0
            var ttRemiseSlice: Double = 0.0
            var ttRemise = mutableMapOf<String, Any>()
            var allIsbn = mutableListOf<String>()

            if (getNbBookbInBasket(books) <= 0) {
                 Log.i("---------LOG---------", "LOAD BOOKS IS EMPTY")
                title = "Panier $ttAmount €"
                loading.text = "Le panier est vide"
                return
            }
//            Log.i("---------LOG---------", "LOAD BOOKS IS NOT EMPTY")

            for (i in books) {
                val b = i as Book
                if (b.isInBasket) {
                    Log.i("---------LOG---------", "LOAD BOOKS: $b")
                    allIsbn.add(b.isbn)
                    ttAmount += b.price
                }
            }
            title = "Basket $ttAmount €"
            val allIsbnUrl = allIsbn.toString().drop(1).dropLast(1).replace("\\s".toRegex(), "")
//            Log.i("---------LOG---------", "allisbn = $allIsbn")
//            Log.i("---------LOG---------", "allisbn = $allIsbnUrl")

//          Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            val url = "http://henri-potier.xebia.fr/books/$allIsbnUrl/commercialOffers"
            Log.i("---------LOG---------", "allisbn = $url")

            val jsonObjectRequest = StringRequest(url,
                { response ->
//                    Log.i("---------LOG---------", "offer: $response")
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray("offers")
                    for (i in 0 until jsonArray.length()){
                        val json: JSONObject = jsonArray.getJSONObject(i)
                        val offer = JSONObject(json.toString())
                        Log.i("---------LOG---------", "offer: ${offer}")
                        val type = offer.get("type")
                        Log.i("---------LOG---------", "offer: ${type}")
                        when (type) {
                            "percentage" -> {
                                val value = offer.getDouble("value")
                                ttRemisePercentage = (ttAmount * value) / 100
                                ttRemise["percentage"] = listOf(ttRemisePercentage, "$value % soit $ttRemisePercentage € de remise")
                            }
                            "minus" -> {
                                ttRemise["minus"] = listOf(offer.getDouble("value"), "-${offer.getDouble("value")} € soit ${offer.getDouble("value")} € de remise")

                            }
                            "slice" -> {
                                val value = offer.getDouble("value")
                                val sliceValue = offer.getDouble("sliceValue")
                                val nbSlice = (ttAmount / sliceValue).toInt()
                                ttRemiseSlice = nbSlice * value
                                ttRemise["slice"] = listOf(ttRemiseSlice, "$value € par $sliceValue € d'achat soit $ttRemiseSlice € de remise")
                            }
                        }
                    }

                    loading.visibility = View.GONE
//                    Log.i("---------LOG---------", "All Remise: $ttRemise")
                    val bestRemise = getBestRemise(ttRemise) as List<Any>
                    val ttBasketRemised = ttAmount - bestRemise[0] as Double
                    title = "Panier $ttBasketRemised €"
                    remise.text = "${bestRemise[1]}"

//                    Log.i("---------LOG---------", "Best Remise: $bestRemise")
//                    Log.i("---------LOG---------", "Best Remise Price: ${bestRemise[0]}")
                    listBooks.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    listBooks.adapter = BasketAdapter(this, books)
                },
                { error ->
                    loading.text = getString(R.string.errorLoading)
                    Log.i("---------LOG---------", "Error loading json: $error")

                }
            )
            // Add the request to the RequestQueue.
            jsonObjectRequest.setShouldCache(true)
            queue.add(jsonObjectRequest)

        } catch (e: Exception) {
            loading.text = getString(R.string.errorLoading)
            return
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