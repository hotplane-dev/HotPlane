package io.hotplane.hotplane

import com.google.gson.annotations.SerializedName


data class Data (

    @SerializedName("count"  ) var count  : Int?              = null,
    @SerializedName("version"  ) var version  : String              = "",
    @SerializedName("base"  ) var base  : String              = "",
    @SerializedName("events" ) var events : ArrayList<Event> = arrayListOf()

)