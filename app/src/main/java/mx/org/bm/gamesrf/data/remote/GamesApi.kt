package mx.org.bm.gamesrf.data.remote

import mx.org.bm.gamesrf.data.remote.model.GameDetailDto
import mx.org.bm.gamesrf.data.remote.model.GameDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface GamesApi {

    @GET
    fun getGames(
        @Url url: String?
    ): Call<List<GameDto>>

    @GET("cm/games/game_detail.php")
    fun getGameDetail(
        @Query("id") id: String?
    ): Call<GameDetailDto>

    // Para Apiary
    @GET("games/games_list")
    fun getGamesApiary(): Call<List<GameDto>>

    @GET("games/game_detail/{id}")
    fun getGameDetailApiary(
        @Path("id") id: String?
    ): Call<GameDetailDto>

}