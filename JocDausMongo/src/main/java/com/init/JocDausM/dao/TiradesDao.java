package com.init.JocDausM.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.init.JocDausM.Model.Tirada;

@Repository
public interface TiradesDao extends MongoRepository<Tirada, Long> {

}
