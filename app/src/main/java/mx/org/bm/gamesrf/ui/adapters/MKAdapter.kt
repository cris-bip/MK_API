package mx.org.bm.gamesrf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.org.bm.gamesrf.data.remote.model.CharacterItemDto
import mx.org.bm.gamesrf.databinding.GameElementBinding

class MKAdapter(
    private val charactersList: List<CharacterItemDto>,
    private val clickAction: (CharacterItemDto) -> Unit
): RecyclerView.Adapter<MKAdapter.ViewHolder>() {

    class ViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivThumbnail = binding.ivThumbnail

        fun bind(character: CharacterItemDto){
            binding.tvTitle.text = character.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = charactersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = charactersList[position]

        holder.bind(game)

        //Con Glide
        Glide.with(holder.itemView.context)
            .load(game.preview)
            .into(holder.ivThumbnail)

        //Procesamiento del clic al elemento
        holder.itemView.setOnClickListener {
            clickAction(game)
        }
    }
}