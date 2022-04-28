package io.hotplane.hotplane

import com.google.gson.annotations.SerializedName


data class Data (

    @SerializedName("count"  ) var count  : Int?              = null,
    @SerializedName("events" ) var events : ArrayList<Event> = arrayListOf()

)