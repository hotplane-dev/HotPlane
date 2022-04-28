package io.hotplane.hotplane

import com.google.gson.annotations.SerializedName


data class Event (

    @SerializedName("screen_id" ) var screenId : String?           = null,
    @SerializedName("points"    ) var points   : ArrayList<String> = arrayListOf()

)