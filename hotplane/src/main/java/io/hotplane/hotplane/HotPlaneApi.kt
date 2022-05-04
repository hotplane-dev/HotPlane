package io.hotplane.hotplane

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface HotPlaneApi {
    @POST("api/event")
    suspend fun postEvents(@Header("Content-Type") content_type : String, @Header("Authorization") authorization: String, @Body data: Data) : Response<Message>

    @GET("api/screen")
    suspend fun getScreen(@Header("Content-Type") content_type : String, @Header("Authorization") authorization: String, @Query("name") name: String, @Query("version") version : String ) : Response<ScreenMessage>

    @POST("api/stringupload")
    suspend fun postScreen(@Header("Content-Type") content_type : String, @Header("Authorization") authorization: String, @Body data: NewScreen) : Response<Message>


    companion object {
        var retrofitService: HotPlaneApi? = null
        fun getInstance() : HotPlaneApi {
            if (retrofitService == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .readTimeout(1, TimeUnit.SECONDS)
                    .writeTimeout(3, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://209d-139-0-178-4.ngrok.io/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                retrofitService = retrofit.create(HotPlaneApi::class.java)
            }
            return retrofitService!!
        }

    }
}