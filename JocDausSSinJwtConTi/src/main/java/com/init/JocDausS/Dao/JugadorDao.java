package com.init.JocDausS.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.init.JocDausS.Entity.Jugador;

@Repository
public interface JugadorDao extends JpaRepository<Jugador, Integer>{

}
