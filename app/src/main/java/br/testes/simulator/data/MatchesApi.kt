package br.testes.simulator.data

import br.testes.simulator.domain.Match
import retrofit2.Call
import retrofit2.http.GET


interface MatchesApi {
    @get:GET("matches.json")
    val matches: Call<List<Match>>
}