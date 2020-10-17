package com.example.henripotier.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Book(val isbn: String = "",
           val title: String = "",
           val price: Int,
           val cover: String = "",
           val synopsis: String = "",
           var isInBasket: Boolean = false) : Parcelable {

    override fun toString(): String {
        return ("User, isbn: $isbn,   title: $title,    price: $price,  isInBasket: $isInBasket,    cover: $cover,  synopsis: $synopsis")
    }
}