package com.init.JocDausS.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "jugador")
public class Jugador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_jugador")
	private int id_jugador;

	@Column(name = "nom")
	private String nom;

	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date data;

	@OneToMany(mappedBy = "jugador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Tirada> llistaTirades;

	@Transient
	private Double porcentatgeExit;

	public Jugador() {
	}

	public Jugador(int id_jugador, String nom, java.util.Date data) {
		this.id_jugador = id_jugador;
		this.nom = nom;
		this.data = data;
	}

	public int getId() {
		return id_jugador;
	}

	public void setId(int id) {
		this.id_jugador = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public java.util.Date getData() {
		return data;
	}

	public void setData(java.util.Date data) {
		this.data = data;
	}

	public List<Tirada> getTirades() {
		return llistaTirades;
	}

	public void setTirades(List<Tirada> tirades) {
		this.llistaTirades = tirades;
	}

	public Double getPorcentatgeExit() {
		return porcentatgeExit;
	}

	public void setPorcentatgeExit(Double porcentatgeExit) {
		this.porcentatgeExit = porcentatgeExit;
	}

	public void addTirada(Tirada laTirada) {

		if (llistaTirades == null)
			llistaTirades = new ArrayList<>();

		llistaTirades.add(laTirada);

		laTirada.setJugador(this);

	}

	public double calcularPorcentaje() {
		int contador = 0;
		for (int i = 0; i < llistaTirades.size(); i++) {
			if (llistaTirades.get(i).isGuanyar()) {
				contador += 1;
			}
		}
		double porcentaje = pasaAPorcentaje(llistaTirades.size(), contador);
		return porcentaje;
	}

	public double pasaAPorcentaje(int total, int totalTrue) {
		double porcentaje = (totalTrue * 100) / total;
		return porcentaje;
	}
}
