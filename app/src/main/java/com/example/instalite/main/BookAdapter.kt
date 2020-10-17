package com.example.henripotier.main

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.henripotier.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.book_row.view.*


class BookAdapter(val context : Activity, val allBooks: MutableList<Book>): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.book_row, parent, false)
        return ViewHolder(view)
    }

    fun refreshButton(holder: ViewHolder, position: Int) {
        if (allBooks[position].isInBasket) {
            holder.buy.text = "OK ${allBooks[position].price} €"
        } else {
            holder.buy.text = "ACHETER ${allBooks[position].price} €"
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(allBooks[position].cover).into(holder.cover)
//        holder.cover = allBooks[position].cover

        holder.isbn.text = "isbn: ${allBooks[position].isbn} "
        holder.title.text = allBooks[position].title
        holder.synopsis.text = allBooks[position].synopsis.drop(2).dropLast(2)
        refreshButton(holder, position)
        holder.buy.setOnClickListener {
            allBooks[position].isInBasket = !allBooks[position].isInBasket
            refreshButton(holder, position)
        }

    }

    override fun getItemCount() = allBooks.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val isbn: TextView = itemView.isbn
        val title: TextView = itemView.title
        val buy: Button = itemView.buy
//        val basket: LinearLayout = itemView.basket
        val cover: ImageView = itemView.cover
        val synopsis: TextView = itemView.synopsis
        val card: CardView = itemView.card
    }

}