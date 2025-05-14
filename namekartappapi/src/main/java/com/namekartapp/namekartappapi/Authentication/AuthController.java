package com.namekartapp.namekartappapi.Authentication;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private Firestore firestore;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");  // 🔹 Use "username" instead of "userId"
        String password = loginData.get("password");

        System.out.println("Received login request: " + loginData); // Debugging

        // 🔹 Validate input
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.status(400).body("❌ Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.status(400).body("❌ Password cannot be empty");
        }

        try {
            // 🔹 Fetch the user document from Firestore
            DocumentReference docRef = firestore.collection("accounts").document(username);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            // 🔹 Check if the user exists
            if (!document.exists()) {
                return ResponseEntity.status(401).body("❌ Wrong Username/Password");
            }

            // 🔹 Extract password and admin status
            String storedPassword = document.getString("password");
            String isAdmin = document.getString("admin"); // Fetch admin status

            if (storedPassword != null && storedPassword.equals(password)) {
                // ✅ Login successful
                return ResponseEntity.ok(Map.of(
                        "message", "✅ Logged In Successfully",
                        "admin", isAdmin
                ));
            } else {
                return ResponseEntity.status(401).body("❌ Wrong Username/Password");
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Internal Server Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Unexpected Error: " + e.getMessage());
        }
    }
}
