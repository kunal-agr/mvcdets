package com.kagrawal.util;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;

public class FirebaseUtil {

    private static Firestore db;

    static {
        try {
            String emulatorHost = System.getenv("FIRESTORE_EMULATOR_HOST");
            String projectId = System.getenv("FIREBASE_PROJECT_ID");

            // Use emulator if environment variable is set
            if (emulatorHost != null && !emulatorHost.isEmpty()) {
                FirestoreOptions options = FirestoreOptions.newBuilder()
                        .setProjectId(projectId != null ? projectId : "mvcdets")
                        .setHost(emulatorHost)
                        .build();
                db = options.getService();
                System.out.println("✅ Firestore EMULATOR connected at: " + emulatorHost);
            }
            // Use production Firebase with service account credentials
            else {
                GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
                FirestoreOptions options = FirestoreOptions.newBuilder()
                        .setCredentials(credentials)
                        .setProjectId(projectId != null ? projectId : "mvcdets")
                        .build();
                db = options.getService();
                System.out.println("✅ Firestore PRODUCTION connected to project: " +
                        (projectId != null ? projectId : "mvcdets"));
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to initialize Firestore");
            e.printStackTrace();
            throw new RuntimeException("Firestore initialization failed", e);
        }
    }

    public static Firestore getFirestore() {
        return db;
    }
}