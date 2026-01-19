package com.kagrawal.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoDatabase database;

    static {
        try {
            // Read from environment variable (recommended)
            String uri = System.getenv("MONGO_URI");

            if (uri == null || uri.isEmpty()) {
                uri = "mongodb+srv://kagrawal_db_user:password@cluster0.ycpcbdo.mongodb.net/?appName=Cluster0";
            }

            MongoClient client = MongoClients.create(uri);
            database = client.getDatabase(""); // <-- replace with your DB name
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}
