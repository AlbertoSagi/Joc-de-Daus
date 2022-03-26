package com.init.JocDausS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.init.JocDausS.Controller.ControllerJugador;
import com.init.JocDausS.Entity.Jugador;
import com.init.JocDausS.Entity.Tirada;
import com.init.JocDausS.Service.ServiceController;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = { ControllerTest.class })
public class ControllerTest {

	@Mock
	ServiceController ServiceController;

	@InjectMocks
	ControllerJugador controllerJugador;

	List<Jugador> elsJugadors;

	Jugador jugador1;
	Jugador jugador2;
	Jugador jugador3;
	Jugador jugadorModificado;

	Tirada tirada1;
	Tirada tirada2;
	Tirada tirada3;

	@Test
	@Order(1)
	public void test_getJugadors() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);

		when(ServiceController.llistaJugadors()).thenReturn(elsJugadors);
		ResponseEntity<List<Jugador>> res = controllerJugador.getJugadors();
		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());
	}

	@Test
	@Order(1)
	public void test_getJugadorsNotFound() {

		when(ServiceController.llistaJugadors()).thenThrow();
		ResponseEntity<List<Jugador>> res = controllerJugador.getJugadors();
		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

	}

	@Test
	@Order(2)
	public void test_createJugador() {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));

		when(ServiceController.creaJugador(jugador1)).thenReturn(jugador1);
		ResponseEntity<Jugador> res = controllerJugador.createJugador(jugador1);
		assertEquals(HttpStatus.CREATED, res.getStatusCode());
		assertEquals(jugador1, res.getBody());
	}

	@Test
	@Order(2)
	public void test_createJugadorConflict() {

		when(ServiceController.creaJugador(null)).thenThrow();
		ResponseEntity<Jugador> res = controllerJugador.createJugador(null);
		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());

	}

	@Test
	@Order(3)
	public void test_ModificaNomJugador() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugadorModificado = new Jugador(1, "Ana", new java.util.Date(7 / 1 / 2022));
		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.updateJugador(jugador1)).thenReturn(jugadorModificado);
		ResponseEntity<Jugador> res = controllerJugador.ModificaNomJugador(jugadorId, "Ana");

		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(1, res.getBody().getId());
		assertEquals("Ana", res.getBody().getNom());
	}

	@Test
	@Order(3)
	public void test_ModificaNomJugadorNotFound() {
		int jugadorId = 4;
		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.empty());
		ResponseEntity<Jugador> res = controllerJugador.ModificaNomJugador(jugadorId, "Ana");

		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

	}

	@Test
	@Order(3)
	public void test_ModificaNomJugadorComflict() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.updateJugador(jugador1)).thenThrow();
		ResponseEntity<Jugador> res = controllerJugador.ModificaNomJugador(jugadorId, "Ana");

		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());

	}

	@Test
	@Order(4)
	public void test_tirarDaus() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		int jugadorId = 1;
		jugadorModificado = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		jugadorModificado.addTirada(tirada1);

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.updateJugador(jugador1)).thenReturn(jugadorModificado);

		ResponseEntity<Jugador> res = controllerJugador.tirarDaus(jugadorId);

		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(1, res.getBody().getTirades().size());

	}

	@Test
	@Order(4)
	public void test_tirarDausNotFound() {
		int jugadorId = 4;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.empty());

		ResponseEntity<Jugador> res = controllerJugador.tirarDaus(jugadorId);

		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

	}

	@Test
	@Order(4)
	public void test_tirarDausComflict() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.updateJugador(jugador1)).thenThrow();

		ResponseEntity<Jugador> res = controllerJugador.tirarDaus(jugadorId);

		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
	}

	@Test
	@Order(5)
	public void test_borrarTiradas() {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		jugador1.addTirada(tirada1);
		jugadorModificado = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.deleteTiradesJugador(jugador1)).thenReturn(jugadorModificado);
		ResponseEntity<Jugador> res = controllerJugador.borrarTiradas(jugadorId);

		assertEquals(HttpStatus.OK, res.getStatusCode());

	}

//	@Test
//	@Order(5)
//	public void test_borrarTiradasNotFound() {
//		
//		int  jugadorId=4;
//
//		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.empty());
//		
//		ResponseEntity<Jugador> res=controllerJugador.borrarTiradas(jugadorId);
//		
//		assertEquals(HttpStatus.NOT_FOUND,res.getStatusCode());
//
//	}
	@Test
	@Order(5)
	public void test_borrarTiradasConflict() {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.deleteTiradesJugador(jugador1)).thenThrow();
		ResponseEntity<Jugador> res = controllerJugador.borrarTiradas(jugadorId);

		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());

	}

	@Test
	@Order(6)
	public void test_llistatPercentatges() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador2);
		tirada3 = new Tirada(3, 2, 5, true, jugador2);
		jugador1.addTirada(tirada1);
		jugador2.addTirada(tirada2);
		jugador2.addTirada(tirada2);
		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);
		Map<String, Double> ranking = new HashMap<String, Double>();
		ranking.put("Isa", 100.0);
		ranking.put("Josep", 50.0);
		when(ServiceController.llistaJugadors()).thenReturn(elsJugadors);
		when(ServiceController.llistatnomIPercentatges(elsJugadors)).thenReturn(ranking);
		ResponseEntity<Map<String, Double>> res = controllerJugador.llistatPercentatges();
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(100, res.getBody().get("Isa"));
	}

	@Test
	@Order(6)
	public void test_llistatPercentatgesComflict() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador2);
		tirada3 = new Tirada(3, 2, 5, true, jugador2);
		jugador1.addTirada(tirada1);
		jugador2.addTirada(tirada2);
		jugador2.addTirada(tirada2);
		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);
		Map<String, Double> ranking = new HashMap<String, Double>();
		ranking.put("Isa", 100.0);
		ranking.put("Josep", 50.0);
		when(ServiceController.llistaJugadors()).thenReturn(elsJugadors);
		when(ServiceController.llistatnomIPercentatges(elsJugadors)).thenThrow();
		ResponseEntity<Map<String, Double>> res = controllerJugador.llistatPercentatges();
		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());

	}

	@Test
	@Order(7)
	public void test_llistarTirades() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador1);
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);
		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.llistaTiradesJugador(jugador1)).thenReturn(jugador1.getTirades());
		ResponseEntity<List<Tirada>> res = controllerJugador.llistarTirades(jugadorId);
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(tirada1, res.getBody().get(0));
		assertEquals(2, res.getBody().size());

	}

	@Test
	@Order(7)
	public void test_llistarTiradesNoContent() {

		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.empty());

		ResponseEntity<List<Tirada>> res = controllerJugador.llistarTirades(jugadorId);
		assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());

	}

	@Test
	@Order(7)
	public void test_llistarTiradesConflict() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador1);
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);
		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.llistaTiradesJugador(jugador1)).thenThrow();
		ResponseEntity<List<Tirada>> res = controllerJugador.llistarTirades(jugadorId);
		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());

	}

	@Test
	@Order(8)
	public void test_rankingMig() {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));

		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador1);
		tirada3 = new Tirada(3, 2, 5, true, jugador2);
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);
		jugador2.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);

		Double rankingMig = 75.0;
		when(ServiceController.llistaJugadors()).thenReturn(elsJugadors);
		when(ServiceController.DonaRankingMig(elsJugadors)).thenReturn(rankingMig);

		ResponseEntity<Double> res = controllerJugador.rankingMig();

		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(75, res.getBody());

	}

	@Test
	@Order(8)
	public void test_rankingMigConflict() {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));

		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador1);
		tirada3 = new Tirada(3, 2, 5, true, jugador2);
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);
		jugador2.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);

		when(ServiceController.llistaJugadors()).thenReturn(elsJugadors);
		when(ServiceController.DonaRankingMig(elsJugadors)).thenThrow();

		ResponseEntity<Double> res = controllerJugador.rankingMig();

		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());

	}

	@Test
	@Order(9)
	public void test_rankingloser() {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));

		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador1);
		tirada3 = new Tirada(3, 2, 5, true, jugador2);
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);
		jugador2.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);

		when(ServiceController.llistaJugadors()).thenReturn(elsJugadors);
		when(ServiceController.DonaPitjorRanking(elsJugadors)).thenReturn(jugador1);

		ResponseEntity<Jugador> res = controllerJugador.rankingloser();
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(jugador1, res.getBody());
	}

	@Test
	@Order(9)
	public void test_rankingloserConflict() {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));

		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador1);
		tirada3 = new Tirada(3, 2, 5, true, jugador2);
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);
		jugador2.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);

		when(ServiceController.llistaJugadors()).thenReturn(elsJugadors);
		when(ServiceController.DonaPitjorRanking(elsJugadors)).thenThrow();

		ResponseEntity<Jugador> res = controllerJugador.rankingloser();
		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
	}

	@Test
	@Order(10)
	public void test_rankingwinner() {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));

		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador1);
		tirada3 = new Tirada(3, 2, 5, true, jugador2);
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);
		jugador2.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);

		when(ServiceController.llistaJugadors()).thenReturn(elsJugadors);
		when(ServiceController.DonaMillorRanking(elsJugadors)).thenReturn(jugador2);

		ResponseEntity<Jugador> res = controllerJugador.rankingwinner();
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals(jugador2, res.getBody());
	}

	@Test
	@Order(10)
	public void test_rankingwinnerConflict() {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));

		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador1);
		tirada3 = new Tirada(3, 2, 5, true, jugador2);
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);
		jugador2.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);

		when(ServiceController.llistaJugadors()).thenReturn(elsJugadors);
		when(ServiceController.DonaMillorRanking(elsJugadors)).thenThrow();

		ResponseEntity<Jugador> res = controllerJugador.rankingwinner();
		assertEquals(HttpStatus.CONFLICT, res.getStatusCode());

	}
}
