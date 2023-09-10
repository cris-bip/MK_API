package mx.org.bm.gamesrf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mx.org.bm.gamesrf.R
import mx.org.bm.gamesrf.databinding.ActivityMainBinding
import mx.org.bm.gamesrf.ui.fragments.FightersListFragment
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FightersListFragment())
                .commit()
        }
    }
}