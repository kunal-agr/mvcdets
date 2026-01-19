package com.kagrawal.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static MongoDatabase database;

    static {
        try {
            // 1️⃣ Read MongoDB URI from environment variable
            String uri = System.getenv("MONGO_URI");
            if (uri == null || uri.isEmpty()) {
                throw new IllegalStateException("Environment variable MONGO_URI is not set!");
            }

            // 2️⃣ Read MongoDB database name from environment variable
            String dbName = System.getenv("MONGO_DB_NAME");
            if (dbName == null || dbName.isEmpty()) {
                throw new IllegalStateException("Environment variable MONGO_DB_NAME is not set!");
            }

            // 3️⃣ Create Mongo client and get the database
            MongoClient client = MongoClients.create(uri);
            database = client.getDatabase(dbName);

            System.out.println("✅ MongoDB connection initialized successfully for database: " + dbName);

        } catch (Exception e) {
            // 4️⃣ Log and fail-fast
            System.err.println("❌ Failed to initialize MongoDB connection!");
            e.printStackTrace();
            // Throw runtime exception to prevent app from starting with invalid DB
            throw new RuntimeException("MongoDB initialization failed", e);
        }
    }

    /**
     * Get the initialized MongoDatabase instance.
     * @return MongoDatabase
     */
    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("MongoDB database is not initialized!");
        }
        return database;
    }
}