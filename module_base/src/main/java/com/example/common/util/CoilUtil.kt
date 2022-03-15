package com.example.common.util

import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.common.BaseApplication

object CoilUtil {
    private var imageLoader: ImageLoader? = null
    fun getImageLoader(): ImageLoader {
        if (imageLoader == null) {
            imageLoader = ImageLoader.Builder(BaseApplication.getContext())
                .components {
                    if (Build.VERSION.SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                    add(VideoFrameDecoder.Factory())
                }
                .memoryCache {
                    MemoryCache.Builder(BaseApplication.getContext())
                        .maxSizePercent(0.25)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(BaseApplication.getContext().cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.02)
                        .build()
                }
                .build()
        }
        return imageLoader!!
    }
}