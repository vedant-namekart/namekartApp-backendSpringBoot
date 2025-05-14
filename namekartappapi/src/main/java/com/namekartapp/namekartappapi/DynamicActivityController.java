package com.namekartapp.namekartappapi;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/{category}~{subType}~{subSubType}")
public class DynamicActivityController {


    @PostMapping
    public ResponseEntity<?> handleDynamicRequest(
            @PathVariable("category") String category,
            @PathVariable("subType") String subType,
            @PathVariable("subSubType") String subSubType,
            @RequestBody Map<String, Object> message) {

        if(subSubType==null||subSubType.equals("")){
            subSubType=category;
        }

        // 🔍 Check if device_notification exists and is a non-empty list
        if (message.containsKey("device_notification")) {
            List<Map<String, String>> notifications = (List<Map<String, String>>) message.get("device_notification");
            String title = "",msg=""   ,topic="";
            for (int i=0;i<notifications.size();i++) {
                System.out.println(notifications.get(1).get("message"));

                title = notifications.get(0).get("title");
                msg = notifications.get(1).get("message");
                topic = notifications.get(2).get("topic");
            }

            // Only send if all required fields are present
            if (!title.equals("") && !msg.equals("") && !topic.equals("")) {
                System.out.println("worked");
                ProgramHelper.sendNotificationToDevices(title, msg, topic);
            }
        }



        // Combine category and subType for processing
        String path = category + "~" + subType+"~"+subSubType;

        // Pass the dynamic category and subType to your helper method
        ProgramHelper.doAllSendingAndDatabaseAddingTask("broadcast",path,subType, category, subSubType, message);


        // Return the response with the original message
        return ResponseEntity.status(HttpStatus.CREATED).body(message);


    }
}