package com.kagrawal.util;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    static {
        try {
            MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017");
            mongoClient = new MongoClient(uri);
            database = mongoClient.getDatabase("mvcdetsdb");
        } catch (Exception e) {
            e.printStackTrace();
        }
    } public static MongoDatabase getDatabase()
    {
        return database;
    }
}