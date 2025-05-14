package com.namekartapp.namekartappapi.notifications;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.namekartapp.namekartappapi.ProgramHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications/LostTracker")
public class LostTracker {

    @PostMapping
    public ResponseEntity<?> addAuction(@RequestBody String message) {
//        ProgramHelper.doAllSendingAndDatabaseAddingTask("LostTracker","notifications","notifications",message);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
