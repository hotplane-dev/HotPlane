package io.hotplane.hotplane

import com.google.gson.annotations.SerializedName

data class Message (

    @SerializedName("message" ) var message : String?           = null,
    @SerializedName("success"    ) var success   : Boolean = false

)