package mx.org.bm.gamesrf.data.remote.model

import com.google.gson.annotations.SerializedName

data class GameDto(
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("thumbnail")
    val thumbnail: String? = null,

    @SerializedName("title")
    val title: String? = null
)
