package br.testes.simulator.ui.adapter

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.testes.simulator.databinding.MatchItemBinding
import br.testes.simulator.domain.Match
import br.testes.simulator.ui.DetailActivity
import com.bumptech.glide.Glide
import java.lang.String
import kotlin.Int


class MatchesAdapter(matches: List<Match>) :
    RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {
    private val matches: List<Match>
    fun getMatches(): List<Match> {
        return matches
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: MatchItemBinding = MatchItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val match: Match = matches[position]

        // Adapta os dados da partida (recuperada da API) para o nosso layout.
        Glide.with(context).load(match.homeTeam.image).circleCrop()
            .into(holder.binding.ivHomeTeam)
        holder.binding.tvHomeTeamName.text = (match.homeTeam.name)
        if (match.homeTeam.score != null) {
            holder.binding.tvHomeTeamScore.text = (String.valueOf(match.homeTeam.score))
        }
        Glide.with(context).load(match.awayTeam.image).circleCrop()
            .into(holder.binding.ivAwayTeam)
        holder.binding.tvAwayTeamName.text = (match.awayTeam.name)
        if (match.awayTeam.score != null) {
            holder.binding.tvAwayTeamScore.text = match.awayTeam.score.toString()
        }
        holder.itemView.setOnClickListener { view: View? ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.Extras.MATCH, match)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return matches.size
    }

    class ViewHolder(binding: MatchItemBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        internal val binding: MatchItemBinding

        init {
            this.binding = binding
        }
    }

    init {
        this.matches = matches
    }
}