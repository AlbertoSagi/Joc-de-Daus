package com.init.JocDausM.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.init.JocDausM.Model.Jugador;
import com.init.JocDausM.Model.Tirada;
import com.init.JocDausM.Service.SequenceGeneratorService;
import com.init.JocDausM.Service.ServiceController;
import com.init.JocDausM.dao.JugadorDao;
import com.init.JocDausM.dao.TiradesDao;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class ControllerJugador {

	@Autowired
	private JugadorDao jugadorDao;

	@Autowired
	private TiradesDao tiradesDao;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	ServiceController serviceController = new ServiceController();

	// List of players
	// (GET /players/).
	@GetMapping(value = "/players")
	public ResponseEntity<List<Jugador>> getJugadors() {

		List<Jugador> jugadors = serviceController.llistaJugadors();
		return ResponseEntity.ok(jugadors);
	}

	// POST: /players : Create a player
	@PostMapping(value = "/players")
	public ResponseEntity<String> createJugador(@RequestBody Jugador jugador) {
		List<Jugador> jugadors = jugadorDao.findAll();
		if (jugador.getNom().equalsIgnoreCase("")) {
			jugador.setNom("Anonimo");
		}

		for (Jugador nombre : jugadors) {
			if (nombre.getNom().equalsIgnoreCase(jugador.getNom())) {
				jugador.setNom(jugador.getNom() + "1");
			}
		}
		jugador = serviceController.creaJugador(jugador);
		// serviceController.creaJugador(jugador);

		return ResponseEntity.ok("jugador creat correctament");
	}

	// PUT /players : update player's name
	@PutMapping(value = "/players/{id}")
	public ResponseEntity<String> ModificaNomJugador(@PathVariable long id, @RequestBody String nom) {
		Jugador jugador = serviceController.canviaNomJugador(id, nom);
		if (jugador == null)
			return ResponseEntity.ok("el jugador con id " + id + " no existe");
		else
			return ResponseEntity.ok(jugador.toString());
	}

	// POST /players/{id}/games/ : An specific player makes a dice roll
	@PostMapping(value = "/players/{id}/games")
	public ResponseEntity<String> tirarDaus(@PathVariable(name = "id") long id) {

		String tirada = serviceController.jugadorTiraDaus(id);
		return ResponseEntity.ok(tirada);
	}

	// DELETE /players/{id}/games: Delete the dice rolls of a player
	@Transactional
	@DeleteMapping(value = "players/{id_jugador}/games")
	public ResponseEntity<String> borrarTiradas(@PathVariable(name = "id_jugador") long id) {

		Optional<Jugador> optionalJugador = jugadorDao.findById(id);

		String resposta = serviceController.eliminaTiradesJugador(optionalJugador);
		return ResponseEntity.ok(resposta);
	}

	// GET /players/: Return a list of all players in the system with average score
	// percentage
	@GetMapping(value = "/players/porcentatge")
	public Map<String, Double> llistatPercentatges() {

		Map<String, Double> mapjugadorPercentatge = serviceController.llistatnomIPercentatges();
		return mapjugadorPercentatge;
	}

	// GET /players/{id}/games: Returns a list of dice roll per player
	@GetMapping(value = "/players/{id}/games")
	public ResponseEntity<List<Tirada>> llistarTirades(@PathVariable long id) {
		Optional<Jugador> optionalJugador = jugadorDao.findById(id);

		if (optionalJugador.isPresent()) {
			List<Tirada> tirades = tiradesDao.findAll();
			List<Tirada> tiradesJugador = serviceController.buscartiradesJugador(tirades, id);
			return ResponseEntity.ok(tiradesJugador);

		} else {
			List<Tirada> empty = new ArrayList<Tirada>();
			return ResponseEntity.ok(empty);
		}

	}

	// GET /players/ranking: Returns average score of all players of the system.
	@GetMapping(value = "/players/ranking")
	public double rankingMig() {
		List<Tirada> tirades = tiradesDao.findAll();
		double rankingMig = serviceController.DonaRankingMig(tirades);
		return rankingMig;
	}

	// GET /players/ranking/loser: Returns player with worst score
	@GetMapping(value = "/players/ranking/loser")
	public Jugador rankingloser() {
		List<Jugador> jugadors = jugadorDao.findAll();
		Jugador jugadorPitjor = serviceController.DonaPitjorRanking(jugadors);
		return jugadorPitjor;
	}

	// GET /players/ranking/winner: Return the player with best score percentage
	@GetMapping(value = "/players/ranking/winner")
	public Jugador rankingwinner() {

		List<Jugador> jugadors = jugadorDao.findAll();
		Jugador jugadorMillor = serviceController.DonaMillorRanking(jugadors);
		return jugadorMillor;

	}
}
