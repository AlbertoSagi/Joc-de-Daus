package com.init.JocDausS.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.init.JocDausS.Entity.Jugador;
import com.init.JocDausS.Entity.Tirada;

public interface IServiceController {

	List<Jugador> llistaJugadors();

	Jugador creaJugador(Jugador jugador);

	Jugador updateJugador(Jugador jugador);

	Optional<Jugador> BuscaJugador(int id);

	Jugador deleteTiradesJugador(Jugador jugador);

	Map<String, Double> llistatnomIPercentatges(List<Jugador> jugadors);

	List<Tirada> llistaTiradesJugador(Jugador jugador);

	double DonaRankingMig(List<Jugador> jugadors);

	List<Jugador> llistatJugadorsAmbTirades(List<Jugador> jugadors);

	Jugador DonaPitjorRanking(List<Jugador> jugadors);

	Jugador DonaMillorRanking(List<Jugador> jugadors);

}
