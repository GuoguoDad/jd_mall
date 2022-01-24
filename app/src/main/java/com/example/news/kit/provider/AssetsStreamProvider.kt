package com.example.news.kit.provider

import android.content.Context
import ir.mirrajabi.okhttpjsonmock.providers.InputStreamProvider
import java.io.InputStream

class AssetsStreamProvider(var context: Context): InputStreamProvider {
    override fun provide(path: String): InputStream? {
        return context.assets.open("api/$path")
    }
}