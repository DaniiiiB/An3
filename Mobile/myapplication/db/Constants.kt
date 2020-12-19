package com.example.myapplication.db
import android.provider.BaseColumns


class Constants
constructor() {
    object FeedEntry: BaseColumns {
        const val TABLE_NAME = "objects"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PRICE = "price"
        const val COLUMN_QUANTITY = "quantity"
    }
}