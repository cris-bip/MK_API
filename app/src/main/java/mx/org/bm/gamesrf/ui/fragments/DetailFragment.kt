package mx.org.bm.gamesrf.ui.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import mx.org.bm.gamesrf.R
import mx.org.bm.gamesrf.application.MKRFApp
import mx.org.bm.gamesrf.data.MKRepository
import mx.org.bm.gamesrf.data.remote.model.CharacterDetailDto
import mx.org.bm.gamesrf.databinding.FragmentDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

private const val ID = "id"

class DetailFragment : Fragment(), OnMapReadyCallback{
    private var id: String? = null

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: MKRepository

    private val decimalsFormat = DecimalFormat("###,###,###.00")

    private lateinit var map: GoogleMap
    private var latitude: Double = 0.0
    private var longitud: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID)

            repository = (requireActivity().application as MKRFApp).repository

            lifecycleScope.launch {
                id?.let { id ->
                    val call: Call<CharacterDetailDto> = repository.getCharacterDetail(id)

                    call.enqueue(object : Callback<CharacterDetailDto>{
                        override fun onResponse(
                            call: Call<CharacterDetailDto>,
                            response: Response<CharacterDetailDto>
                        ) {
                            binding.apply {
                                pbLoading.visibility = View.GONE
                                tvTitle.text = response.body()?.name
                                tvLongDesc.text = response.body()?.realm
                                tvFrendship.text = response.body()?.friendship
                                tvFatality.text = response.body()?.fatality
                                tvBrutality.text = response.body()?.brutality

                                Glide.with(requireContext())
                                    .load(response.body()?.image)
                                    .into(ivImage)

                                vvVideo.setVideoURI(Uri.parse(response.body()?.video))

                                val mc = MediaController(requireContext())
                                mc.setAnchorView(vvVideo)
                                vvVideo.setMediaController(mc)
                                vvVideo.start()

                                latitude = response.body()?.locationLat!!
                                longitud = response.body()?.locationLng!!

                                var mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                                mapFragment.getMapAsync(this@DetailFragment)
                            }
                        }

                        override fun onFailure(call: Call<CharacterDetailDto>, t: Throwable) {
                            binding.pbLoading.visibility = View.GONE

                            Toast.makeText(requireActivity(), getString(R.string.no_connection_msg), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        createMarker()
    }

    private fun createMarker(){
        val coordinates = LatLng(latitude, longitud)
        val marker = MarkerOptions()
            .position(coordinates)
            .title("Location: (${decimalsFormat.format(latitude)}, ${decimalsFormat.format(longitud)})")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.person))

        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4_000,
            null
        )
    }
}