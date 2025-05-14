package com.namekartapp.namekartappapi.notifications;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.namekartapp.namekartappapi.ProgramHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications/CloseoutsActivity")
public class CloseoutsActivity {

    @PostMapping
    public ResponseEntity<?> addAuction(@RequestBody String message) {
//        ProgramHelper.doAllSendingAndDatabaseAddingTask("CloseoutsActivity","notifications","notifications",message);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

}
