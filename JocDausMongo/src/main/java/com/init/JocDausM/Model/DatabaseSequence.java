package com.init.JocDausM.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document(collection = "database_sequences")
@Component
public class DatabaseSequence {

	// The aim of this class is to create an autoincremental numer to avoid id
	// provided by mongo
	@Id
	private String id;

	private long seq;

	public DatabaseSequence() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}
}