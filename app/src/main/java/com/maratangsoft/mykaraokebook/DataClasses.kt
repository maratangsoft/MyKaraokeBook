package com.maratangsoft.mykaraokebook

data class SongItem(
    val brand: String,
    val no: String,
    val title: String,
    val singer: String,
    val release: String?,
    var memo: String?
)

data class NaverResult(
    val items: MutableList<NaverItem>
)
data class NaverItem(
    var title: String,
    val mapx: Int,
    val mapy: Int ){
}