package mx.org.bm.gamesrf.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mx.org.bm.gamesrf.R
import mx.org.bm.gamesrf.databinding.ActivityMainBinding
import mx.org.bm.gamesrf.ui.fragments.FightersListFragment
import mx.org.bm.gamesrf.util.SoundService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startService(Intent(this, SoundService::class.java))

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FightersListFragment())
                .commit()
        }

        binding.btnStopSound.setOnClickListener {
            stopService()
        }
    }

    override fun onResume() {
        super.onResume()

        startService(Intent(this, SoundService::class.java))
    }

    override fun onPause() {
        super.onPause()

        stopService()
    }

    override fun onDestroy() {
        super.onDestroy()

        stopService()
    }

    private fun stopService(){
        stopService(Intent(this, SoundService::class.java))
    }
}