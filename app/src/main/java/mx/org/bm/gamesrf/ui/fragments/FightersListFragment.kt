package mx.org.bm.gamesrf.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import mx.org.bm.gamesrf.R
import mx.org.bm.gamesrf.application.MKRFApp
import mx.org.bm.gamesrf.data.MKRepository
import mx.org.bm.gamesrf.data.remote.model.CharacterItemDto
import mx.org.bm.gamesrf.databinding.FragmentFightersListBinding
import mx.org.bm.gamesrf.ui.adapters.GamesAdapter
import mx.org.bm.gamesrf.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FightersListFragment : Fragment() {

    private var _binding: FragmentFightersListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: MKRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFightersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as MKRFApp).repository

        lifecycleScope.launch {
            val call: Call<List<CharacterItemDto>> = repository.getFighters()

            call.enqueue(object: Callback<List<CharacterItemDto>>{
                override fun onResponse(
                    call: Call<List<CharacterItemDto>>,
                    response: Response<List<CharacterItemDto>>
                ) {

                    binding.pbLoading.visibility = View.GONE

                    Log.d(Constants.LOGTAG, "Respuesta del servidor: ${response.body()}")

                    response.body()?.let {list ->
                        binding.rvGames.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = GamesAdapter(list){element ->
                                element.id?.let{id ->
                                    // Mostrar detalle
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, GameDetailFragment.newInstance(id))
                                        .addToBackStack(null)
                                        .commit()
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<CharacterItemDto>>, t: Throwable) {
                    Log.d(Constants.LOGTAG, "Error: ${t.message}")

                    Toast.makeText(requireActivity(), "No hay conexión", Toast.LENGTH_SHORT).show()

                    binding.pbLoading.visibility = View.GONE
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}