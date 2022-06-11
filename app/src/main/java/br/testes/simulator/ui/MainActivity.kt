package br.testes.simulator.ui
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.testes.simulator.R
import br.testes.simulator.data.MatchesApi
import br.testes.simulator.databinding.ActivityMainBinding
import br.testes.simulator.domain.Match
import br.testes.simulator.ui.adapter.MatchesAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.RuntimeException
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var matchesApi: MatchesApi? = null
    private var matchesAdapter: MatchesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setupHttpClient()
        setupMatchesList()
        setupMatchesRefresh()
        setupFloatingActionButton()
    }

    private fun setupHttpClient() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://digitalinnovationone.github.io/matches-simulator-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        matchesApi = retrofit.create(MatchesApi::class.java)
    }

    private fun setupMatchesList() {
        binding!!.rvMatches.setHasFixedSize(true)
        binding!!.rvMatches.layoutManager = LinearLayoutManager(this)
        matchesAdapter = MatchesAdapter(emptyList())
        binding!!.rvMatches.adapter = matchesAdapter
        findMatchesFromApi()
    }

    private fun setupMatchesRefresh() {
        binding!!.srlMatches.setOnRefreshListener { findMatchesFromApi() }
    }

    private fun setupFloatingActionButton() {
        binding?.fbSimulate?.setOnClickListener { view ->

            view.animate().rotationBy(360f).setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        val random = Random
                        for (i in 0 until matchesAdapter!!.itemCount) {
                            val match: Match = matchesAdapter!!.getMatches()[i]
                            match.homeTeam
                                .score=(random.nextInt(match.homeTeam.stars + 1))
                            match.awayTeam
                                .score=(random.nextInt(match.awayTeam.stars + 1))
                            matchesAdapter!!.notifyItemChanged(i)
                        }
                    }
                })
        }
    }

    private fun findMatchesFromApi() {
        binding!!.srlMatches.isRefreshing = true
        matchesApi!!.matches.enqueue(object : Callback<List<Match>> {

            override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
                if (response.isSuccessful) {
                    val matches: List<Match> = response.body() as List<Match>
                    matchesAdapter = MatchesAdapter(matches)
                    binding!!.rvMatches.adapter = matchesAdapter
                } else {
                    showErrorMessage()
                }
                binding!!.srlMatches.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Match>>, t: Throwable) {
                showErrorMessage()
                binding!!.srlMatches.isRefreshing = false
            }
        })
    }

    private fun showErrorMessage() {
        Snackbar.make(binding!!.fbSimulate, R.string.error_api, Snackbar.LENGTH_LONG).show()
    }
}