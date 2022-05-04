package io.hotplane.hotplane

import com.google.gson.annotations.SerializedName


data class NewScreen (

    @SerializedName("name"  ) var name  : String              = "",
    @SerializedName("version" ) var version : String              = "",
    @SerializedName("content" ) var content : String              = ""

)