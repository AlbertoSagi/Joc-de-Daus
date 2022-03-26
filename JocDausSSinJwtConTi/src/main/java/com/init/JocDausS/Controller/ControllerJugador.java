package com.init.JocDausS.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.init.JocDausS.Dao.JugadorDao;
import com.init.JocDausS.Dao.TiradesDao;
import com.init.JocDausS.Entity.Jugador;
import com.init.JocDausS.Entity.Tirada;
import com.init.JocDausS.Service.ServiceController;

@RestController
@RequestMapping("/")
public class ControllerJugador {

	@Autowired
	private JugadorDao jugadorDao;

	@Autowired
	private TiradesDao tiradesDao;

	@Autowired
	ServiceController ServiceController = new ServiceController();

	// List of all players
	// (GET /players/).
	@GetMapping(value = "/players")
	public ResponseEntity<List<Jugador>> getJugadors() {
		try {
			List<Jugador> jugadors = ServiceController.llistaJugadors();
			return new ResponseEntity<List<Jugador>>(jugadors, HttpStatus.FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	// POST: /players : Create a player
	@PostMapping(value = "/players")
	public ResponseEntity<Jugador> createJugador(@RequestBody Jugador jugador) {
		try {
			Jugador jugadorCreat = ServiceController.creaJugador(jugador);
			return new ResponseEntity<Jugador>(jugadorCreat, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	// PUT /players : Update a player's name
	@PutMapping(value = "/players/{id}")
	public ResponseEntity<Jugador> ModificaNomJugador(@PathVariable int id, @RequestBody String nom) {
		try {
			Optional<Jugador> optionalJugador = ServiceController.BuscaJugador(id);

			if (optionalJugador.isPresent()) {
				Jugador updateJugador = optionalJugador.get();
				updateJugador.setNom(nom);
				ServiceController.updateJugador(updateJugador);
				return new ResponseEntity<Jugador>(updateJugador, HttpStatus.OK);

			}

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	// POST /players/{id}/games/ : A player make a dice roll.
	@PostMapping(value = "/players/{id}/games/")
	public ResponseEntity<Jugador> tirarDaus(@PathVariable(name = "id") int id) {
		try {
			Optional<Jugador> optionalJugador = ServiceController.BuscaJugador(id);

			if (optionalJugador.isPresent()) {
				Jugador updateJugador = optionalJugador.get();
				Tirada tirada = new Tirada(updateJugador);
				updateJugador.addTirada(tirada);
				ServiceController.updateJugador(updateJugador);
				return new ResponseEntity<Jugador>(updateJugador, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	// DELETE /players/{id}/games: Delete a player's dice rolls
	@Transactional
	@DeleteMapping(value = "players/{id_jugador}/games")
	public ResponseEntity<Jugador> borrarTiradas(@PathVariable(name = "id_jugador") int id) {
		try {
			Optional<Jugador> jugadorOptional = ServiceController.BuscaJugador(id);
			Jugador Jugador = jugadorOptional.get();

			if (!jugadorOptional.isEmpty()) {

				Jugador JugadorSenseTirades = ServiceController.deleteTiradesJugador(Jugador);

				return new ResponseEntity<Jugador>(JugadorSenseTirades, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	// GET /players/: Returns a list of all players in the system with average score
	// percentage
	@GetMapping(value = "/players/porcentatge")
	public ResponseEntity<Map<String, Double>> llistatPercentatges() {
		try {
			List<Jugador> jugadors = ServiceController.llistaJugadors();
			Map<String, Double> mapjugadorPercentatge = ServiceController.llistatnomIPercentatges(jugadors);
			return new ResponseEntity<Map<String, Double>>(mapjugadorPercentatge, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	// GET /players/{id}/games: Returns a list of dice rolls for specific player.
	@GetMapping(value = "/players/{id}/games")
	public ResponseEntity<List<Tirada>> llistarTirades(@PathVariable int id) {
		try {
			Optional<Jugador> optionalJugador = ServiceController.BuscaJugador(id);

			if (optionalJugador.isPresent()) {
				Jugador jugador = optionalJugador.get();
				List<Tirada> tirades = ServiceController.llistaTiradesJugador(jugador);
				return new ResponseEntity<List<Tirada>>(tirades, HttpStatus.OK);

			} else {

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	// GET /players/ranking: Returns the average ranking of all players of the
	// system.
	@GetMapping(value = "/players/ranking")
	public ResponseEntity<Double> rankingMig() {
		try {
			double rankingMig = 0.0;
			List<Jugador> jugadors = ServiceController.llistaJugadors();
			rankingMig = ServiceController.DonaRankingMig(jugadors);
			return new ResponseEntity<Double>(rankingMig, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	// GET /players/ranking/loser: Returns the player with the worst score
	// percentage
	@GetMapping(value = "/players/ranking/loser")
	public ResponseEntity<Jugador> rankingloser() {
		try {
			double rankingMig = 0.0;
			List<Jugador> jugadors = ServiceController.llistaJugadors();
			Jugador jugadorPitjor = ServiceController.DonaPitjorRanking(jugadors);
			return new ResponseEntity<Jugador>(jugadorPitjor, HttpStatus.OK);
		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	// GET /players/ranking/winner: Returns the player with the best score
	// percentage
	@GetMapping(value = "/players/ranking/winner")
	public ResponseEntity<Jugador> rankingwinner() {
		try {
			List<Jugador> jugadors = ServiceController.llistaJugadors();
			Jugador jugadorMillor = ServiceController.DonaMillorRanking(jugadors);
			return new ResponseEntity<Jugador>(jugadorMillor, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}
}
