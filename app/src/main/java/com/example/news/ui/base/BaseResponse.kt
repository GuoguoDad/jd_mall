package com.example.news.ui.base

data class BaseResponse<T> (val code: String, val msg: String, val data: T)