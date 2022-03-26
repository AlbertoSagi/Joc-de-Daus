package com.init.JocDausM.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.init.JocDausM.Model.Jugador;

@Component
@Repository
public interface JugadorDao extends MongoRepository<Jugador, Long> {

}
