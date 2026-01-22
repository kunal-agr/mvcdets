package com.kagrawal.util;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class FirebaseUtil {

    private static Firestore db;

    static {
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setProjectId("mvcdets") // project ID can match emulator
                .setHost("localhost:8085") // Firestore emulator
                .build();
        db = options.getService();
        System.out.println("✅ Firestore emulator connected!");
    }

    public static Firestore getFirestore() {
        return db;
    }
}
