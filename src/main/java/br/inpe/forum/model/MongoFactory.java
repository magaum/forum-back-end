package br.inpe.forum.model;

import java.util.Objects;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoFactory {

	public static MongoDatabase createMongoDatabase() {

		MongoClientURI uri = new MongoClientURI(System.getenv("MONGODB_URI"));
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient(uri);

		MongoDatabase database = mongoClient.getDatabase("forum");
		if (Objects.nonNull(database))
			return database;
		throw new MongoClientException("Database not found");
	}

}
