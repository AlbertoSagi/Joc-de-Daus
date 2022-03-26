package com.init.JocDausM.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.init.JocDausM.Model.Jugador;
import com.init.JocDausM.Model.Tirada;

public interface IServiceController {
	List<Jugador> llistaJugadors();

	Jugador creaJugador(Jugador jugador);

	Jugador canviaNomJugador(long id, String nom);

	String jugadorTiraDaus(long id);

	String eliminaTiradesJugador(Optional<Jugador> optionalJugador);

	public Map<String, Double> llistatnomIPercentatges();

	List<Tirada> buscartiradesJugador(List<Tirada> tirades, long jugador_id);

	double calcularPorcentaje(List<Tirada> llistaTirades);

	double pasaAPorcentaje(int total, int totalTrue);

	double DonaRankingMig(List<Tirada> tirades);

	Jugador DonaPitjorRanking(List<Jugador> jugadors);

	Jugador DonaMillorRanking(List<Jugador> jugadors);

}
