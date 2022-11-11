package com.maratangsoft.mykaraokebook

import android.Manifest

var brand = "tj"
var rowCount = 50
val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

const val BRAND_TJ: String = "tj"
const val BRAND_KY: String = "kumyoung"

//프래그먼트 이름
const val FRAG_SEARCH = 1
const val FRAG_NEW_SONG = 2
const val FRAG_POPULAR = 3

//FavoriteFragment DB 정렬기준
const val SORT_TITLE = "title"
const val SORT_SINGER = "singer"
const val SORT_NO = "_no"

//SearchFragment 검색조건
const val QUERY_TITLE = "song"
const val QUERY_SINGER = "singer"
const val QUERY_NO = "no"