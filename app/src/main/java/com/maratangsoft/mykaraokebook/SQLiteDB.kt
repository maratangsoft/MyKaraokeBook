package com.maratangsoft.mykaraokebook

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
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
        createTable()
    }

    private fun createTable(){
        val schema = StringBuilder()
            .append("$colId        INTEGER         NOT NULL PRIMARY KEY AUTOINCREMENT,")
            .append("$colBrand     VARCHAR(10)     NOT NULL,")
            .append("$colNo        VARCHAR(6)      NOT NULL,")
            .append("$colTitle     TEXT            NOT NULL,")
            .append("$colSinger    TEXT            NOT NULL,")
            .append("$colRelease   VARCHAR(10),")
            .append("$colMemo      TEXT").toString()
        db.execSQL("CREATE TABLE IF NOT EXISTS $tableName ($schema)")
    }

    fun insertDB(brand:String, no:String, title:String, singer:String, release:String?=null){
        val cv = ContentValues()
        cv.put(colBrand, brand)
        cv.put(colNo, no)
        cv.put(colTitle, title)
        cv.put(colSinger, singer)
        cv.put(colRelease, release)

        db.insert(tableName, null, cv)
        Toast.makeText(context, "북마크했습니다.", Toast.LENGTH_SHORT).show()
    }

    fun deleteDB(no:String){
        db.delete(tableName, "$colNo='$no' AND $colBrand='$brand'", null)
        Toast.makeText(context, "북마크를 취소했습니다.", Toast.LENGTH_SHORT).show()
    }

    fun loadDB(itemList:MutableList<SongItem>, adapter:RecyclerView.Adapter<ViewHolder>?, sort:String?){
        itemList.clear()
        adapter?.notifyDataSetChanged()

        val cursor =    if (sort != null) db.rawQuery("SELECT * FROM $tableName WHERE brand='$brand' ORDER BY $sort", null)
                        else db.rawQuery("SELECT * FROM $tableName WHERE brand='$brand'", null)

        while (cursor.moveToNext()){
            val brand = cursor.getString(1)
            val no = cursor.getString(2)
            val title = cursor.getString(3)
            val singer = cursor.getString(4)
            val release:String? = cursor.getString(5)
            val memo:String? = cursor.getString(6)
            itemList.add(SongItem(brand, no, title, singer, release, memo))
        }
        cursor.close()
        adapter?.notifyDataSetChanged()
    }

    fun isFavorited(no:String): Boolean{
        val checkFav = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM $tableName WHERE _no='$no' AND brand='$brand'", null)
        return (checkFav > 0L)
    }

    fun updateMemo(no:String, memo:String?=null){
        val cv = ContentValues()
        cv.put("memo", memo)
        db.update(tableName, cv, "$colNo='$no' AND $colBrand='$brand'", null)
    }

    fun clearDB(){
        db.execSQL("DROP TABLE $tableName")
        createTable()
    }
}