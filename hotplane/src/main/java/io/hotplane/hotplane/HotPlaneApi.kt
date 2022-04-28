package io.hotplane.hotplane

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface HotPlaneApi {
    @POST("event")
    suspend fun postEvents(@Header("Content-Type") content_type : String, @Header("Authorization") authorization: String, @Body data: Data) : Response<Message>

    companion object {
        var retrofitService: HotPlaneApi? = null
        fun getInstance() : HotPlaneApi {
            if (retrofitService == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://4d76-139-0-178-4.ngrok.io/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                retrofitService = retrofit.create(HotPlaneApi::class.java)
            }
            return retrofitService!!
        }

    }
}