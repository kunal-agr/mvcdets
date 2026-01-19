package com.kagrawal.util;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static MongoDatabase database;
    private static MongoClient mongoClient;

    static {
        try {
            // 1️⃣ READ AND VALIDATE ENVIRONMENT VARIABLES
            System.out.println("\n========== MONGODB CONNECTION DEBUG ==========");

            String uri = System.getenv("MONGO_URI");
            String dbName = System.getenv("MONGO_DB_NAME");

            // Debug: Print all environment variables containing 'MONGO'
            System.out.println("\n📋 Environment Variables:");
            System.getenv().forEach((key, value) -> {
                if (key.contains("MONGO")) {
                    String maskedValue = value.replaceAll("(mongodb.*:)([^@]+)(@)", "$1***$3");
                    System.out.println("  " + key + " = " + maskedValue);
                }
            });

            System.out.println("\n🔍 Current Values:");
            System.out.println("  MONGO_URI = " + (uri != null ? "SET (" + uri.length() + " chars)" : "NULL ❌"));
            System.out.println("  MONGO_DB_NAME = " + (dbName != null ? "SET (" + dbName + ")" : "NULL ❌"));

            // VALIDATE URI
            if (uri == null || uri.trim().isEmpty()) {
                System.err.println("\n❌ CRITICAL ERROR: MONGO_URI is not set!");
                System.err.println("   This will cause MongoClients.create() to default to localhost:27017");
                System.err.println("   Solution: Set MONGO_URI in Render Environment Variables");
                throw new IllegalStateException(
                        "MONGO_URI environment variable not found! " +
                                "Configure it in Render Dashboard → Environment tab"
                );
            }

            uri = uri.trim();

            // VALIDATE CONNECTION STRING FORMAT
            if (!uri.startsWith("mongodb://") && !uri.startsWith("mongodb+srv://")) {
                throw new IllegalStateException(
                        "Invalid MongoDB URI format! Must start with 'mongodb://' or 'mongodb+srv://'\n" +
                                "Current URI: " + uri.substring(0, Math.min(50, uri.length())) + "..."
                );
            }

            // Mask password for logging
            String maskedUri = maskPasswordInUri(uri);
            System.out.println("\n🔗 Connection String (masked):");
            System.out.println("  " + maskedUri);

            // VALIDATE DB NAME
            if (dbName == null || dbName.trim().isEmpty()) {
                System.err.println("\n⚠️  MONGO_DB_NAME not set, using default 'mvcdetsdb'");
                dbName = "mvcdetsdb";
            }
            dbName = dbName.trim();
            System.out.println("\n📦 Database Name: " + dbName);

            // 2️⃣ CREATE MONGODB CLIENT
            System.out.println("\n🔄 Creating MongoDB Client...");
            long startTime = System.currentTimeMillis();

            mongoClient = MongoClients.create(uri);

            System.out.println("   Client created: " + (System.currentTimeMillis() - startTime) + "ms");

            // 3️⃣ TEST CONNECTION WITH PING
            System.out.println("\n🧪 Testing connection (ping command)...");
            startTime = System.currentTimeMillis();

            mongoClient.getDatabase("admin").runCommand(new org.bson.Document("ping", 1));

            long pingTime = System.currentTimeMillis() - startTime;
            System.out.println("   ✅ Ping successful: " + pingTime + "ms");

            // 4️⃣ GET DATABASE
            database = mongoClient.getDatabase(dbName);

            System.out.println("\n✅ MongoDB connection SUCCESS!");
            System.out.println("   Database: " + dbName);
            System.out.println("===========================================\n");

        } catch (IllegalStateException e) {
            System.err.println("\n❌ Configuration Error:");
            System.err.println("   " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("MongoDB configuration failed", e);

        } catch (MongoException e) {
            System.err.println("\n❌ MongoDB Connection Error:");
            System.err.println("   " + e.getClass().getSimpleName());
            System.err.println("   Cannot reach MongoDB cluster");
            System.err.println("   Possible causes:");
            System.err.println("   1. MongoDB Atlas IP whitelist - add 0.0.0.0/0");
            System.err.println("   2. Wrong hostname in MONGO_URI");
            System.err.println("   3. Credentials incorrect");
            System.err.println("   4. MongoDB cluster is down");
            System.err.println("   5. Connection string format invalid");
            System.err.println("   Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("MongoDB connection failed", e);

        } catch (Exception e) {
            System.err.println("\n❌ Unexpected Error: " + e.getClass().getSimpleName());
            System.err.println("   " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("MongoDB initialization failed", e);
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("MongoDB database not initialized!");
        }
        return database;
    }

    public static MongoClient getClient() {
        if (mongoClient == null) {
            throw new IllegalStateException("MongoDB client not initialized!");
        }
        return mongoClient;
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            try {
                mongoClient.close();
                System.out.println("✅ MongoDB connection closed");
            } catch (Exception e) {
                System.err.println("⚠️ Error closing connection: " + e.getMessage());
            }
        }
    }

    private static String maskPasswordInUri(String uri) {
        if (uri == null) return null;
        return uri.replaceAll("(mongodb.*?://)([^:]+):([^@]+)(@)", "$1$2:***$4");
    }
}