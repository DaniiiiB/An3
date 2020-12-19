package com.example.myapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class Helper(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "objects.db"
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Constants.FeedEntry.TABLE_NAME + " (" +
                    Constants.FeedEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    Constants.FeedEntry.COLUMN_NAME + " TEXT," +
                    Constants.FeedEntry.COLUMN_DESCRIPTION + " TEXT," +
                    Constants.FeedEntry.COLUMN_PRICE + " INTEGER," +
                    Constants.FeedEntry.COLUMN_QUANTITY + " INTEGER)"
        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Constants.FeedEntry.TABLE_NAME
    }
}