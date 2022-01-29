package com.example.main

import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import com.example.common.abs.BaseApplication
import okhttp3.OkHttpClient

class MainApplication: BaseApplication(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(this))
                    .build()
            }
            .build()
    }

    override fun init() {
    }

}