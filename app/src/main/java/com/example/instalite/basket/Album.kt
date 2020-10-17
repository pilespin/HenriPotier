package com.example.henripotier.basket

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Album(val id: Int,
            val userId: Int,
            val title: String = "") : Parcelable {

    override fun toString(): String {
        return ("Album, id: $id,   userId: $userId,    title: $title")
    }
}