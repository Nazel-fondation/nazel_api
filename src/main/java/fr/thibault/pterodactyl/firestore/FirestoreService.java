package fr.thibault.pterodactyl.firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirestoreService {
    static Firestore db;
    public void load() throws IOException {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-key.json");;
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
        FirebaseApp.initializeApp(options);

        db = (Firestore) FirestoreClient.getFirestore();

    }

    public Boolean userIdExist(String userId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        return document.exists();
    }
    
    public String getUserIdFromPseudo(String pseudo) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection("users").whereEqualTo("pseudo", pseudo).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (DocumentSnapshot document : documents) {
            return document.getId();
        }
        return null;
    }

    public String getPseudoFromUserId(String userId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()){
            return document.getData().get("pseudo").toString();
        }else{
            return null;
        }
    }
}
