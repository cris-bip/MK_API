package mx.org.bm.gamesrf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import mx.org.bm.gamesrf.data.remote.model.CharacterItemDto
import mx.org.bm.gamesrf.data.remote.model.GameDto
import mx.org.bm.gamesrf.databinding.GameElementBinding

class GamesAdapter(
    private val games: List<CharacterItemDto>,
    private val onGameClicked: (CharacterItemDto) -> Unit
): RecyclerView.Adapter<GamesAdapter.ViewHolder>() {

    class ViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivThumbnail = binding.ivThumbnail

        fun bind(game: CharacterItemDto){
            binding.tvTitle.text = game.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]

        holder.bind(game)

        //Con Glide
        Glide.with(holder.itemView.context)
            .load(game.preview)
            .into(holder.ivThumbnail)

        //Procesamiento del clic al elemento
        holder.itemView.setOnClickListener {
            onGameClicked(game)
        }
    }
}