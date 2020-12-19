package com.example.myapplication.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.myapplication.db.Constants
import com.example.myapplication.model.MyObject

class Datasource constructor(
    var readableDatabase: SQLiteDatabase,
    var writableDatabase: SQLiteDatabase
){
    companion object {
        private var instance: Datasource? = null
        fun getInstance(rdb: SQLiteDatabase, wdb: SQLiteDatabase): Datasource? {
            if (instance == null) instance =
                Datasource(rdb, wdb)
            return instance
        }

    }

//    fun initialise() {
//        objects.add(MyObject(1, "Broom", "Used to swipe dust", 100, 1))
//        objects.add(MyObject(2, "Chair", "One can sit on it", 200, 2))
//        objects.add(MyObject(3, "Armchair", "One can comfortably sit on it", 300, 3))
//        objects.add(MyObject(4, "Tissues", "One can use those to clean stuff", 400, 4))
//        objects.add(MyObject(5, "Cup", "Only for coffee, no tea", 500, 5))
//        objects.add(MyObject(6, "Spoon", "Better than a fork in most cases", 600, 6))
//    }

    fun loadMyObjects(): List<MyObject> {
        val data: MutableList<MyObject> = ArrayList<MyObject>()
        val cursor = readableDatabase.query(
            Constants.FeedEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
        )
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.FeedEntry.COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.FeedEntry.COLUMN_NAME))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(Constants.FeedEntry.COLUMN_DESCRIPTION))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.FeedEntry.COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.FeedEntry.COLUMN_QUANTITY))
            val obj = MyObject(id, name, description, price, quantity)
            data.add(obj)
        }
        return data
    }

    fun delete(pos: Int) {
        print(this.loadMyObjects())
        val selection: String = Constants.FeedEntry.COLUMN_ID + " LIKE?"
        val selectionArgs = arrayOf(Integer.toString(pos!!))
        writableDatabase.delete(Constants.FeedEntry.TABLE_NAME, selection, selectionArgs)
    }

    fun add(newMyObject: MyObject): Long {
        return writableDatabase.insert(Constants.FeedEntry.TABLE_NAME, null, newMyObject.getContentValues())
    }

    fun update(newMyObject: MyObject) {
        val contentValues = newMyObject.getContentValues()
        val selection: String = Constants.FeedEntry.COLUMN_ID.toString() + " LIKE ?"
        val selectionArgs = arrayOf(Integer.toString(newMyObject.id!!))
        writableDatabase.update(
            Constants.FeedEntry.TABLE_NAME,
            contentValues,
            selection,
            selectionArgs
        )
    }
}