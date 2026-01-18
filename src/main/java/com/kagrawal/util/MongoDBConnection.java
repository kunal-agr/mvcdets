package com.kagrawal.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    static {
        try {
            String host = System.getenv().getOrDefault("MONGO_HOST", "localhost");
            String port = System.getenv().getOrDefault("MONGO_PORT", "27017");
            String db   = System.getenv().getOrDefault("MONGO_DB", "mvcdetsdb");

            String uri = "mongodb://" + host + ":" + port;

            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase(db);

            System.out.println("MongoDB connected to: " + uri + "/" + db);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MongoDBConnection() {
        // prevent instantiation
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}