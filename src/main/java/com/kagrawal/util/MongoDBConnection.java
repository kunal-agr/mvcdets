package com.kagrawal.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static MongoDatabase database;

    static {
        try {
            String uri = System.getenv("MONGO_URI");
            if (uri == null || uri.trim().isEmpty()) {
                throw new IllegalStateException("MONGO_URI environment variable not set");
            }

            String dbName = System.getenv("MONGO_DB_NAME");
            if (dbName == null || dbName.trim().isEmpty()) {
                throw new IllegalStateException("MONGO_DB_NAME environment variable not set");
            }

            MongoClient client = MongoClients.create(uri.trim());
            database = client.getDatabase(dbName.trim());

        } catch (Exception e) {
            System.err.println("❌ MongoDB Connection Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("MongoDB initialization failed", e);
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("MongoDB database is not initialized");
        }
        return database;
    }
}