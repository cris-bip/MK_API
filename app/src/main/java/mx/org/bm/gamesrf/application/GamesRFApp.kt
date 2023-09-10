package mx.org.bm.gamesrf.application

import android.app.Application
import mx.org.bm.gamesrf.data.GameRepository
import mx.org.bm.gamesrf.data.remote.RetrofitHelper

class GamesRFApp: Application() {

    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy{
        GameRepository(retrofit)
    }

}