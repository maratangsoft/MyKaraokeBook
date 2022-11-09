package com.maratangsoft.mykaraokebook

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SQLiteDB(val context:Context) {
    private val db: SQLiteDatabase = context.openOrCreateDatabase("FavoriteDB", Context.MODE_PRIVATE, null)
    private val tableName = "favorite"
    private val colId = "id"
    private val colBrand = "brand"
    private val colNo = "_no"
    private val colTitle = "title"
    private val colSinger = "singer"
    private val colRelease = "_release"
    private val colMemo = "memo"

    init {
        val columns = StringBuilder()
            .append("$colId        INTEGER         NOT NULL PRIMARY KEY AUTOINCREMENT")
            .append("$colBrand     VARCHAR(10)     NOT NULL,")
            .append("$colNo        VARCHAR(6)      NOT NULL,")
            .append("$colTitle     TEXT            NOT NULL,")
            .append("$colSinger    TEXT            NOT NULL,")
            .append("$colRelease   VARCHAR(10),")
            .append("$colMemo      TEXT").toString()
        db.execSQL("CREATE TABLE IF NOT EXISTS $tableName ($columns)")
    }

    fun insertDB(brand:String, no:String, title:String, singer:String, release:String?){
        val cv = ContentValues()
        cv.put(colBrand, brand)
        cv.put(colNo, no)
        cv.put(colTitle, title)
        cv.put(colSinger, singer)
        cv.put(colRelease, release)

        db.insert(tableName, null, cv)
    }

    fun deleteDB(no:String){
        db.delete(tableName, "$colNo=$no AND $colBrand=$brand", null)
    }

    fun loadDB(itemList:MutableList<Item>, adapter:RecyclerView.Adapter<ViewHolder>?){
        itemList.clear()
        adapter?.notifyDataSetChanged()

        val cursor = when (brand){
            BRAND_TJ -> db.rawQuery("SELECT * FROM $tableName WHERE brand=$BRAND_TJ", null)
            else -> db.rawQuery("SELECT * FROM $tableName WHERE brand=$BRAND_KY", null)
        }
        while (cursor.moveToNext()){
            val brand = cursor.getString(1)
            val no = cursor.getString(2)
            val title = cursor.getString(3)
            val singer = cursor.getString(4)
            val release:String? = cursor.getString(5)
            val memo:String? = cursor.getString(6)
            itemList.add(Item(brand, no, title, singer, release, memo))
        }
        cursor.close()
        adapter?.notifyDataSetChanged()
    }

    fun isFavorited(no:String): Boolean{
        val checkFav = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM $tableName WHERE _no=$no AND brand=$brand", null)
        return (checkFav != 0L)
    }
}