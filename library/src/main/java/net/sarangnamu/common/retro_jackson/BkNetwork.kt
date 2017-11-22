package net.sarangnamu.common.retro_jackson

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2017. 11. 22.. <p/>
 */

class Retro {
    companion object {
        fun instance(baseUrl: String, logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = logLevel

            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create(Json.instance.mapper))
                    .client(client)
                    .build()
        }

        fun builder(baseUrl: String, logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): Retrofit.Builder {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = logLevel

            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create(Json.instance.mapper))
                    .client(client)
        }
    }
}

class Json private constructor() {
    private object Holder { val INSTANCE = Json() }
    val mapper = jacksonObjectMapper()

    companion object {
        val instance: Json by lazy { Holder.INSTANCE }
    }

    init {
        with (mapper) {
            configure(JsonParser.Feature.ALLOW_COMMENTS, true)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        }
    }

    fun string(obj: Any): String {
        return mapper.writeValueAsString(obj)
    }
}
