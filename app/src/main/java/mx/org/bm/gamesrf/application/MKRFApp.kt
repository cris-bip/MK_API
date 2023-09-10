package mx.org.bm.gamesrf.application

import android.app.Application
import mx.org.bm.gamesrf.data.MKRepository
import mx.org.bm.gamesrf.data.remote.RetrofitHelper

class MKRFApp: Application() {

    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy{
        MKRepository(retrofit)
    }
}