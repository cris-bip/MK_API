package mx.org.bm.gamesrf.data

import mx.org.bm.gamesrf.data.remote.MKApi
import mx.org.bm.gamesrf.data.remote.model.CharacterDetailDto
import mx.org.bm.gamesrf.data.remote.model.CharacterItemDto
import retrofit2.Call
import retrofit2.Retrofit

class MKRepository(private val retrofit: Retrofit) {

    private val mkApi: MKApi = retrofit.create(MKApi::class.java)

    fun getFighters(): Call<List<CharacterItemDto>> = mkApi.getFightersApiary()

    fun getCharacterDetail(id: String?): Call<CharacterDetailDto> = mkApi.getFighterDetailApiary(id)
}