package com.init.JocDausS;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

//import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.init.JocDausS.Controller.ControllerJugador;
import com.init.JocDausS.Entity.Jugador;
import com.init.JocDausS.Entity.Tirada;
import com.init.JocDausS.Service.ServiceController;

@TestMethodOrder(OrderAnnotation.class)
@ComponentScan(basePackages = "com.init.JocDausS")
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = { ControllerMockMvcTests.class })
public class ControllerMockMvcTests {

	@Autowired
	MockMvc mockMvc;

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

	@BeforeEach
	public void setUp() {

		mockMvc = MockMvcBuilders.standaloneSetup(controllerJugador).build();
	}

	@Test
	@Order(1)
	public void test_getJugadors() throws Exception {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugador2 = new Jugador(2, "Josep", new java.util.Date(7 / 1 / 2022));

		List<Jugador> elsJugadors = new ArrayList<Jugador>();
		elsJugadors.add(jugador1);
		elsJugadors.add(jugador2);

		when(ServiceController.llistaJugadors()).thenReturn(elsJugadors);

		this.mockMvc.perform(get("/players")).andExpect(status().isFound()).andDo(print());
	}

	@Test
	@Order(2)
	public void test_createJugador() throws Exception {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));

		when(ServiceController.creaJugador(jugador1)).thenReturn(jugador1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(jugador1);

		this.mockMvc.perform(post("/players").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(print());

	}

	@Test
	@Order(3)
	public void test_ModificaNomJugador() throws Exception {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		jugadorModificado = new Jugador(1, "Ana", new java.util.Date(7 / 1 / 2022));
		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.updateJugador(jugador1)).thenReturn(jugadorModificado);

		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(jugadorModificado);

		this.mockMvc.perform(put("/players/{id}", jugadorId).content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath(".id").value(1))
				.andExpect(jsonPath("$.nom", containsString("Ana"))).andDo(print());
	}

	@Test
	@Order(4)
	public void test_tirarDaus() throws Exception {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		int jugadorId = 1;
		jugadorModificado = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		jugadorModificado.addTirada(tirada1);

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.updateJugador(jugador1)).thenReturn(jugadorModificado);

		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(jugadorModificado);

		this.mockMvc
				.perform(post("/players/{id}/games/", jugadorId).content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", org.hamcrest.Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath(".nom").value("Isa"))
				.andExpect(MockMvcResultMatchers.jsonPath(".tirades").isArray()).andDo(print());

	}

	@Test
	@Order(5)
	public void test_borrarTiradas() throws Exception {

		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		jugador1.addTirada(tirada1);
		jugadorModificado = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		List<Tirada> tirades = new ArrayList<Tirada>();
		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.deleteTiradesJugador(jugador1)).thenReturn(jugadorModificado);

		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(jugadorModificado);

		this.mockMvc
				.perform(delete("/players/{id_jugador}/games", jugadorId).content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath(".id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath(".nom").value("Isa"))
				.andExpect(MockMvcResultMatchers.jsonPath(".tirades").isArray()).andDo(print());
	}

	@Test
	@Order(6)
	public void test_llistatPercentatges() throws Exception {
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

		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(jugadorModificado);

		this.mockMvc.perform(get("/players/porcentatge").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath(".Isa").value(100.0))
				.andExpect(MockMvcResultMatchers.jsonPath(".Josep").value(50.0)).andDo(print());

	}

	@Test
	@Order(7)
	public void test_llistarTirades() throws Exception {
		jugador1 = new Jugador(1, "Isa", new java.util.Date(7 / 1 / 2022));
		tirada1 = new Tirada(1, 2, 5, true, jugador1);
		tirada2 = new Tirada(2, 2, 4, false, jugador1);
		jugador1.addTirada(tirada1);
		jugador1.addTirada(tirada2);
		int jugadorId = 1;

		when(ServiceController.BuscaJugador(jugadorId)).thenReturn(Optional.of(jugador1));
		when(ServiceController.llistaTiradesJugador(jugador1)).thenReturn(jugador1.getTirades());

		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(jugadorModificado);

		this.mockMvc
				.perform(
						get("/players/{id}/games", jugadorId).content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath(".id").isArray())
				.andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
				.andExpect(jsonPath("$[*].id", org.hamcrest.Matchers.containsInAnyOrder(1, 2))).andDo(print());

	}

	@Test
	@Order(8)
	public void test_rankingMig() throws Exception {

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

		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(jugadorModificado);

		this.mockMvc.perform(get("/players/ranking").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", org.hamcrest.Matchers.is(75.0))).andDo(print());
	}

	@Test
	@Order(9)
	public void test_rankingloser() throws Exception {

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

		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(jugadorModificado);

		this.mockMvc.perform(get("/players/ranking/loser").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath(".nom").value("Isa"))
				.andDo(print());
	}

	@Test
	@Order(9)
	public void test_rankingwinner() throws Exception {

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

		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(jugadorModificado);

		this.mockMvc.perform(get("/players/ranking/winner").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath(".nom").value("Josep"))
				.andDo(print());
	}
}
