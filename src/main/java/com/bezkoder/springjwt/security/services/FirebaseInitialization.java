package com.bezkoder.springjwt.security.services;

import java.io.FileInputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

@Service
public class FirebaseInitialization {

	@PostConstruct
    public void initialization(){

        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream("./geo-app1-firebase-adminsdk-ynhl4-f49a8fb6fc.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://geo-app1-default-rtdb.europe-west1.firebasedatabase.app/")
                .build();
        FirebaseApp app = null;
        if(FirebaseApp.getApps().isEmpty()) {
            app = FirebaseApp.initializeApp(options);
        }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

