package com.example.common.base

data class BaseResponse<T> (val code: String, val msg: String, val data: T)