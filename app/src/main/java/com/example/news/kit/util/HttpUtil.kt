package com.example.news.kit.util

import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

open class HttpUtil {
    private var retrofit: Retrofit

    init {
        retrofit = buildRetrofit("http://localhost:8090")
    }

    companion object {
        const val TIME_OUT_CONNECT = 5000L
        const val TIME_OUT_READ = 20000L
        const val TIME_OUT_WRITE = 20000L

        val instance: HttpUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { HttpUtil() }
    }

    /**
     * 获取API服务
     */
    fun <T> service(service: Class<T>): T{
        return retrofit.create(service)
    }

    private fun buildRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(toGson()))
        .client(okClient())
        .baseUrl(baseUrl)
        .build()


    private fun okClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder().connectTimeout(TIME_OUT_CONNECT, TimeUnit.MILLISECONDS)
            .readTimeout(TIME_OUT_READ, TimeUnit.MILLISECONDS)
            .writeTimeout(TIME_OUT_WRITE, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(baseInterceptor())
        return builder.build()
    }

    /**
     * 设置头
     */
    private fun baseInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("platform", "android")
                .method(originalRequest.method, originalRequest.body)

            chain.proceed(requestBuilder.build())
        }
    }

    /**
     * >= jdk1.8 localDateTime
     *
     */
    private fun toGson(): Gson {
        val builder = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls() //输出null
            .setPrettyPrinting()//格式化输出

        //localDateTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                val instant = Instant.ofEpochMilli(json.asJsonPrimitive.asLong)
                LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            })
        }
        return builder.create()
    }
}