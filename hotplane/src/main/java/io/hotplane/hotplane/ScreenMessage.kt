package io.hotplane.hotplane

import com.google.gson.annotations.SerializedName

data class ScreenMessage (

    @SerializedName("message" ) var message : String?           = null,
    @SerializedName("success"    ) var success   : Boolean = false,
    @SerializedName("can_update"    ) var canUpdate   : Boolean = false

)