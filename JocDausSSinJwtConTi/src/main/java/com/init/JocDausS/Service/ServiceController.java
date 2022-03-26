package com.init.JocDausS.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.init.JocDausS.Dao.JugadorDao;
import com.init.JocDausS.Dao.TiradesDao;
import com.init.JocDausS.Entity.Jugador;
import com.init.JocDausS.Entity.Tirada;

@Service
public class ServiceController implements IServiceController {

	private static int count = 0;

	@Autowired
	private JugadorDao jugadorDao;

	@Autowired
	private TiradesDao tiradesDao;

	// List of players
	public List<Jugador> llistaJugadors() {

		List<Jugador> jugadors = jugadorDao.findAll();
		return jugadors;
	}

	// Create a player
	public Jugador creaJugador(Jugador jugador) {
		List<Jugador> jugadors = jugadorDao.findAll();
		if (jugador.getNom().equalsIgnoreCase("")) {
			jugador.setNom("Anonimo");
		}

		for (Jugador nombre : jugadors) {
			if (nombre.getNom().equalsIgnoreCase(jugador.getNom())) {
				jugador.setNom(jugador.getNom() + " " + (++count));
			}
		}

		jugadorDao.save(jugador);

		return jugador;
	}

	// Update a player
	public Jugador updateJugador(Jugador jugador) {
		jugadorDao.save(jugador);

		return jugador;

	}

	// Search for a player by id
	public Optional<Jugador> BuscaJugador(int id) {

		Optional<Jugador> jugador = jugadorDao.findById(id);

		return jugador;
	}

	// Delete a player's dice rolls
	public Jugador deleteTiradesJugador(Jugador jugador) {
		tiradesDao.deleteAllByJugador(jugador);
		Optional<Jugador> player = jugadorDao.findById(jugador.getId());
		Jugador jugador1 = null;
		if (!player.isEmpty()) {
			jugador1 = player.get();
		}
		return jugador1;

	}

	// List of player and their score percentage
	public Map<String, Double> llistatnomIPercentatges(List<Jugador> jugadors) {

		Map<String, Double> mapjugadorPercentatge = new HashMap<String, Double>();

		if (jugadors != null && jugadors.size() > 0) {
			String key = "";
			Double value = null;
			for (int i = 0; i < jugadors.size(); i++) {
				key = jugadors.get(i).getNom();
				if (jugadors.get(i).getTirades().size() > 0 && jugadors.get(i).getTirades() != null) {
					value = jugadors.get(i).calcularPorcentaje();
				} else {
					value = null;
				}
				mapjugadorPercentatge.put(key, value);
			}
		}
		return mapjugadorPercentatge;
	}

	// List of a player's dice rolls
	public List<Tirada> llistaTiradesJugador(Jugador jugador) {
		List<Tirada> tirades = jugador.getTirades();
		return tirades;
	}

	// Return the average ranking of all players
	public double DonaRankingMig(List<Jugador> jugadors) {
		double rankingMig = 0.0;
		double sumatoriPorcentatge = 0.0;
		for (int i = 0; i < jugadors.size(); i++) {
			if (jugadors.get(i).getTirades() != null && jugadors.get(i).getTirades().size() > 0) {
				sumatoriPorcentatge += jugadors.get(i).calcularPorcentaje();
			}
		}
		rankingMig = sumatoriPorcentatge / jugadors.size();
		return rankingMig;
	}

	// List of players with dice rolls
	public List<Jugador> llistatJugadorsAmbTirades(List<Jugador> jugadors) {
		List<Jugador> llistatJugadorsAmbTirades = new ArrayList<Jugador>();

		for (Jugador jugador : jugadors) {
			if (jugador.getTirades() != null && jugador.getTirades().size() > 0) {
				llistatJugadorsAmbTirades.add(jugador);
			}

		}
		return llistatJugadorsAmbTirades;
	}

	// Returns the worst player
	public Jugador DonaPitjorRanking(List<Jugador> jugadors) {
		List<Jugador> jugadorsAmbTirades = llistatJugadorsAmbTirades(jugadors);
		jugadorsAmbTirades.sort(Comparator.comparing(Jugador::calcularPorcentaje));

		return jugadorsAmbTirades.get(0);

	}

	// Returns the best player
	public Jugador DonaMillorRanking(List<Jugador> jugadors) {
		List<Jugador> jugadorsAmbTirades = llistatJugadorsAmbTirades(jugadors);
		jugadorsAmbTirades.sort(Comparator.comparing(Jugador::calcularPorcentaje).reversed());

		return jugadorsAmbTirades.get(0);
	}

}
