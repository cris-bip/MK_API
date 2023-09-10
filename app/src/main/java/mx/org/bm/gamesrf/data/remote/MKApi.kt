package mx.org.bm.gamesrf.data.remote

import mx.org.bm.gamesrf.data.remote.model.CharacterDetailDto
import mx.org.bm.gamesrf.data.remote.model.CharacterItemDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MKApi {

    @GET("fighters")
    fun getFightersApiary(): Call<List<CharacterItemDto>>

    @GET("fighters/detail/{id}")
    fun getFighterDetailApiary(
        @Path("id") id: String?
    ): Call<CharacterDetailDto>
}