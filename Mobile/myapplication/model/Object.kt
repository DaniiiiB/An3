package com.example.myapplication.model

import android.content.ContentValues
import com.example.myapplication.db.Constants
import java.io.Serializable

data class MyObject(
    var id: Int?,
    val name: String,
    val description: String,
    val price: Int,
    val quantity: Int
) : Serializable
{
    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put(Constants.FeedEntry.COLUMN_ID, this.id)
        values.put(Constants.FeedEntry.COLUMN_NAME, this.name)
        values.put(Constants.FeedEntry.COLUMN_DESCRIPTION, this.description)
        values.put(Constants.FeedEntry.COLUMN_PRICE, this.price)
        values.put(Constants.FeedEntry.COLUMN_QUANTITY, this.quantity)
        return values
    }
}