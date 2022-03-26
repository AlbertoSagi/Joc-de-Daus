package com.init.JocDausS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.init.JocDausS.Dao.JugadorDao;
import com.init.JocDausS.Dao.TiradesDao;
import com.init.JocDausS.Entity.Jugador;
import com.init.JocDausS.Entity.Tirada;
import com.init.JocDausS.Service.ServiceController;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = serviceTest.class)
public class serviceTest {

	@Mock
	private JugadorDao jugadorDao;

	@InjectMocks
	ServiceController ServiceController;

	@Mock
	private TiradesDao tiradesDao;

	List<Jugador> elsJugadors;

	Jugador jugador1;
	Jugador jugador2;
	Jugador jugador3;

	Tirada tirada1;
	Tirada tirada2;
	Tirada tirada3;
	Tirada tirada4;
	Tirada tirada5;

//	
//	@BeforeEach
//	void setUp() {
//		MockitoAnnotations.openMocks(this);

//		jugador1=new Jugador(1,"Isa",new java.util.Date(7/1/2022));
//		jugador2=new Jugador(2,"Josep",new java.util.Date(7/1/2022));
//		tirada1=new Tirada(jugador1);
//		tirada2=new Tirada(jugador2);
//		jugador1.addTirada(tirada1);
//		jugador2.addTirada(tirada2);
//		List<Jugador>elsJugadors=new ArrayList<Jugador>();
//		elsJugadors.add(jugador1);
//		elsJugadors.add(jugador2);
//	}
	@Test
	@Order(1)
	public void test_llistaJugadors() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);

		when(jugadorDao.findAll()).thenReturn(elsJugadors);
		assertEquals(2, ServiceController.llistaJugadors().size());
		assertEquals("Isa", ServiceController.llistaJugadors().get(0).getNom());

	}

	@Test
	@Order(2)
	public void test_creaJugador() {
		Jugador jugador = new Jugador(3, "Consuelo", new java.util.Date(7 / 1 / 2022));

		when(jugadorDao.save(jugador)).thenReturn(jugador);
		assertEquals(jugador, ServiceController.creaJugador(jugador));
	}

	@Test
	@Order(3)
	public void test_updateJugador() {
		Jugador jugador = new Jugador(3, "Consuelo", new java.util.Date(7 / 1 / 2022));

		when(jugadorDao.save(jugador)).thenReturn(jugador);
		assertEquals(jugador, ServiceController.updateJugador(jugador));
	}

	@Test
	@Order(4)
	public void test_BuscaJugadorfound() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));

		List<Jugador> elsJugadors = new ArrayList<Jugador>();

		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);

		int jugadorId = 1;

		when(jugadorDao.findById(1)).thenReturn(Optional.of(jugador1));

		assertEquals(jugadorId, ServiceController.BuscaJugador(jugadorId).get().getId());

	}

	@Test
	@Order(5)
	public void test_BuscaJugadornotFound() {

		int jugadorId = 5;

		when(jugadorDao.findById(5)).thenReturn(Optional.empty());

		assertEquals(true, ServiceController.BuscaJugador(jugadorId).isEmpty());

	}

	@Test
	@Order(6)
	public void test_deleteTiradesJugador() {

		jugador3 = new Jugador(3, "Isa", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(jugador3);
		tirada2 = new Tirada(jugador3);
		jugador3.addTirada(tirada1);
		jugador3.addTirada(tirada2);
		Jugador jugadorSenseTirades = new Jugador(3, "Isa", new java.util.Date(7 / 1 / 2022));
		int id = 3;
		when(jugadorDao.findById(id)).thenReturn(Optional.of(jugador3));// Mocking

		tiradesDao.deleteAllByJugador(jugador3);
		verify(tiradesDao, times(1)).deleteAllByJugador(jugador3);

		when(tiradesDao.deleteAllByJugador(jugador3)).thenReturn(jugadorSenseTirades.getId());
		when(jugadorDao.findById(3)).thenReturn(Optional.of(jugadorSenseTirades));

		assertEquals(null, ServiceController.deleteTiradesJugador(jugador3).getTirades());
		assertEquals(3, ServiceController.deleteTiradesJugador(jugador3).getId());

	}

	@Test
	@Order(7)
	public void test_llistatnomIPercentatges() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));
		jugador3 = new Jugador(3, "Ana", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(jugador1);
		tirada2 = new Tirada(jugador2);
		tirada3 = new Tirada(jugador3);
		jugador1.addTirada(tirada1);
		jugador2.addTirada(tirada2);
		jugador3.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);
		elsJugadors.add(jugador3);

		Map<String, Double> llistacreada = ServiceController.llistatnomIPercentatges(elsJugadors);
		assertEquals(elsJugadors.get(0).calcularPorcentaje(), llistacreada.get("Isa"));
		assertEquals(elsJugadors.get(1).calcularPorcentaje(), llistacreada.get("Josep"));
		assertEquals(elsJugadors.get(2).calcularPorcentaje(), llistacreada.get("Ana"));
		assertEquals(3, llistacreada.size());
	}

	@Test
	@Order(8)
	public void test_llistaTiradesJugador() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador1);
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);
		List<Tirada> tirades = ServiceController.llistaTiradesJugador(jugador1);
		assertEquals(jugador1.getTirades(), tirades);
	}

	@Test
	@Order(9)
	public void test_DonaRankingMig() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));
		jugador3 = new Jugador(3, "Ana", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(jugador1);
		tirada2 = new Tirada(jugador2);
		tirada3 = new Tirada(jugador3);
		jugador1.addTirada(tirada1);
		jugador2.addTirada(tirada2);
		jugador2.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);
		elsJugadors.add(jugador3);

		double porcentatgeReal = (jugador1.calcularPorcentaje() + jugador2.calcularPorcentaje()) / 3;
		double porcentatge = ServiceController.DonaRankingMig(elsJugadors);
		assertEquals(porcentatgeReal, porcentatge);
	}

	@Test
	@Order(10)
	public void test_llistatJugadorsAmbTirades() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));
		jugador3 = new Jugador(3, "Ana", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(jugador1);
		tirada2 = new Tirada(jugador2);
		tirada3 = new Tirada(jugador3);
		jugador1.addTirada(tirada1);
		jugador2.addTirada(tirada2);
		jugador2.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);
		elsJugadors.add(jugador3);

		List<Jugador> jugadorsAmbTirades = ServiceController.llistatJugadorsAmbTirades(elsJugadors);

		assertEquals(2, jugadorsAmbTirades.size());
	}

	@Test
	@Order(11)
	public void test_DonaPitjorRanking() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));
		jugador3 = new Jugador(3, "Ana", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador2);
		tirada3 = new Tirada(3, 2, 5, true, jugador2);
		jugador1.addTirada(tirada1);
		jugador2.addTirada(tirada2);
		jugador2.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);
		elsJugadors.add(jugador3);

		Jugador jugadorPitjor = ServiceController.DonaPitjorRanking(elsJugadors);
		assertEquals(elsJugadors.get(1), jugadorPitjor);
	}

	@Test
	@Order(12)
	public void test_DonaMillorRanking() {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));
		jugador3 = new Jugador(3, "Ana", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador2);
		tirada3 = new Tirada(3, 2, 5, true, jugador2);
		jugador1.addTirada(tirada1);
		jugador2.addTirada(tirada2);
		jugador2.addTirada(tirada3);

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);
		elsJugadors.add(jugador3);

		Jugador jugadorPitjor = ServiceController.DonaMillorRanking(elsJugadors);
		assertEquals(elsJugadors.get(0), jugadorPitjor);
	}
}
