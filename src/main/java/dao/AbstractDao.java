package dao;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;

public abstract class AbstractDao {
	
	//Reactive Programming --> 1.9
	// Async + Non-blocking IO
	
	private static final String MY_DB = "OnTap";
	private MongoClient client;
	protected MongoDatabase db;
	public AbstractDao(MongoClient client) {
		super();
		this.client = client;
		db = client.getDatabase(MY_DB); 
	}
	
	public MongoDatabase getDb() {
		return db;
	}
	
	public MongoClient getClient() {
		return client;
	}
	
	
}
