package com.init.JocDausM.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.init.JocDausM.Model.Jugador;
import com.init.JocDausM.Model.Tirada;
import com.init.JocDausM.dao.JugadorDao;
import com.init.JocDausM.dao.TiradesDao;

@Service
public class ServiceController implements IServiceController {

	@Autowired
	private JugadorDao jugadorDao;

	@Autowired
	private TiradesDao tiradesDao;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	// List players: Returns list of players
	public List<Jugador> llistaJugadors() {
		List<Jugador> jugadors = jugadorDao.findAll();
		return jugadors;
	}

	// POST: /players : Create a player
	public Jugador creaJugador(Jugador jugador) {
		jugador.setId(sequenceGeneratorService.generateSequence(Jugador.SEQUENCE_NAME));
		jugadorDao.save(jugador);
		return jugador;
	}

	// PUT /players : update the player's name
	public Jugador canviaNomJugador(long id, String nom) {

		Optional<Jugador> optionalJugador = jugadorDao.findById(id);

		if (optionalJugador.isPresent()) {
			Jugador updateJugador = optionalJugador.get();
			updateJugador.setNom(nom);
			jugadorDao.save(updateJugador);
			return updateJugador;

		} else {
			Jugador updateJugador = optionalJugador.get();
			return updateJugador;
		}
	}

	// POST A players makes a dice roll.
	public String jugadorTiraDaus(long id) {
		Optional<Jugador> optionalJugador = jugadorDao.findById(id);
		String resposta;
		if (optionalJugador.isPresent()) {
			Jugador updateJugador = optionalJugador.get();

			Tirada tirada = new Tirada(updateJugador.getId());
			// updateJugador.addTirada(tirada);
			tirada.setId(sequenceGeneratorService.generateSequence(Tirada.SEQUENCE_NAME));
			tiradesDao.save(tirada);
			List<Tirada> tirades = tiradesDao.findAll();
			List<Tirada> tiradesjugador = buscartiradesJugador(tirades, id);
			double porcentatge = calcularPorcentaje(tiradesjugador);
			updateJugador.setPorcentatgeExit(porcentatge);
			jugadorDao.save(updateJugador);
			resposta = tirada.toString();
		} else
			resposta = "aquest jugador no existeix";

		return resposta;
	}

	// DELETE /players/{id}/games: Delete the player's dice roll
	public String eliminaTiradesJugador(Optional<Jugador> optionalJugador) {
		if (optionalJugador.isPresent()) {
			List<Tirada> tirades = tiradesDao.findAll();
			for (Tirada tirada : tirades) {
				if (tirada.getId_jugador() == optionalJugador.get().getId()) {
					tiradesDao.delete(tirada);
				}
			}
			return "tirades eliminades";
		}
		return "el jugador amb id " + optionalJugador.get().getId() + " no existeix";

	}

	// GET /players/: Returns a list of all players in the system with average score
	// percentage
	public Map<String, Double> llistatnomIPercentatges() {
		List<Jugador> jugadors = jugadorDao.findAll();
		List<Tirada> tirades = tiradesDao.findAll();

		Map<String, Double> mapjugadorPercentatge = new HashMap<String, Double>();
		List<Tirada> tiradesJugador;
		if (jugadors != null && jugadors.size() > 0) {
			String key = "";
			Double value = null;
			for (int i = 0; i < jugadors.size(); i++) {
				String Key = jugadors.get(i).getNom();
				tiradesJugador = buscartiradesJugador(tirades, jugadors.get(i).getId());
				if (!tiradesJugador.isEmpty()) {

					value = calcularPorcentaje(tiradesJugador);
				} else {
					value = null;
				}
				mapjugadorPercentatge.put(Key, value);
			}

		}
		return mapjugadorPercentatge;
	}

	// Returns a list of a player's dice rolls.
	public List<Tirada> buscartiradesJugador(List<Tirada> tirades, long jugador_id) {
		List<Tirada> tiradesJugador = new ArrayList<Tirada>();
		for (Tirada tirada : tirades) {
			if (tirada.getId_jugador() == jugador_id) {
				tiradesJugador.add(tirada);
			}
		}
		return tiradesJugador;

	}

	// Returns the player's percentage
	public double calcularPorcentaje(List<Tirada> llistaTirades) {
		int contador = 0;
		for (int i = 0; i < llistaTirades.size(); i++) {
			if (llistaTirades.get(i).isGuanyar()) {
				contador += 1;
			}
		}
		double porcentaje = pasaAPorcentaje(llistaTirades.size(), contador);
		return porcentaje;
	}

	// make up the percentage
	public double pasaAPorcentaje(int total, int totalTrue) {
		double porcentaje = (totalTrue * 100) / total;
		return porcentaje;
	}

	// Returns the average score of all players in the system.
	public double DonaRankingMig(List<Tirada> tirades) {
		double rankingMig = 0.0;
		if (tirades != null && tirades.size() > 0) {
			rankingMig = calcularPorcentaje(tirades);
		}
		return rankingMig;
	}

	// Returns the player with the worst score percentage
	public Jugador DonaPitjorRanking(List<Jugador> jugadors) {

		jugadors.sort(Comparator.comparing(Jugador::getPorcentatgeExit));

		return jugadors.get(0);
	}

	// Returns the player with the best score percentage
	public Jugador DonaMillorRanking(List<Jugador> jugadors) {
		jugadors.sort(Comparator.comparing(Jugador::getPorcentatgeExit).reversed());

		return jugadors.get(0);
	}

}
