package com.aries.common.base

data class BaseResponse<T> (val code: String, val msg: String, val data: T)