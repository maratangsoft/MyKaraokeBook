package com.maratangsoft.mykaraokebook

data class Item(
    val brand: String,
    val no: String,
    val title: String,
    val singer: String,
    val release: String,
    var memo: String?
)