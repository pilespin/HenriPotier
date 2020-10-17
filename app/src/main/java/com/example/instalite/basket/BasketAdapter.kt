package com.example.henripotier.basket

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.henripotier.R
import com.example.henripotier.main.Book
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.basket_row.view.*


class BasketAdapter(val context: Activity, val books: MutableList<Book>): RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.basket_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.text = books[position].title
            holder.isbn.text = "isbn: ${books[position].isbn} "
            holder.price.text = "${books[position].price.toString()} €"
            Picasso.get().load(books[position].cover).into(holder.cover)
    }

    override fun getItemCount() = books.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.title
        val cover: ImageView = itemView.cover
        val isbn: TextView = itemView.isbn
        val price: TextView = itemView.price
        val layout: LinearLayout = itemView.layout
    }

}