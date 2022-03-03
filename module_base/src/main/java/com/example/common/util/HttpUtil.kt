package com.example.common.util

import android.os.Build
import android.util.Log
import com.example.common.provider.ShareContentProvider
import com.example.mock.provider.AssetsStreamProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import java.lang.Exception
import ir.mirrajabi.okhttpjsonmock.OkHttpMockInterceptor

open class HttpUtil {
    private var retrofit: Retrofit
    private var tag = "HttpUtil"

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
            .addInterceptor(OkHttpMockInterceptor(ShareContentProvider.getApplication()?.let {
                AssetsStreamProvider(it)
            },0))
            .addInterceptor(loggingInterceptor)
            .addInterceptor(errorInterceptor())
            .addNetworkInterceptor(baseInterceptor())
        return builder.build()
    }


    /**
     * 错误处理
     */
    private fun errorInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request: Request = chain.request()
            var response: Response? = null

            try {
                response = chain.proceed(request)
                if (response.isSuccessful) {
                    response
                }
                when (response.code) {
                    400 -> Log.e(tag, "BadRequest")
                    401 -> Log.e(tag, "Unauthorized")
                    403 -> Log.e(tag, "Forbidden")
                    404 -> Log.e(tag, "NotFound")
                    405 -> Log.e(tag, "MethodNotAllowed")
                    409 -> Log.e(tag, "Conflict")
                    500 -> Log.e(tag, "InternalServerError")
                    502 -> Log.e(tag, "BadGateway")
                    503 -> Log.e(tag, "ServiceUnavailable")
                }
            } catch (e: SocketTimeoutException) {
                Log.e(tag, "网络超时")
            } catch (e: Exception) {}
            response!!
        }
    }

    /**
     * 设置头
     */
    private fun baseInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
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