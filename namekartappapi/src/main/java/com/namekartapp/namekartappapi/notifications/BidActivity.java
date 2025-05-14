package com.namekartapp.namekartappapi.notifications;

import com.namekartapp.namekartappapi.ProgramHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/notifications/BidActivity")
public class BidActivity {

    @PostMapping
    public ResponseEntity<?> addAuction(@RequestBody Map<String, Object> message) {

//        ProgramHelper.doAllSendingAndDatabaseAddingTask("BidActivity","notifications","notifications",message);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

}
