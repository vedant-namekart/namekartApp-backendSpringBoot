package com.namekartapp.namekartappapi;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FCMService {


    public void sendNotificationToTopic(String topic, String title, String body, Map<String, Object> payload) throws Exception {
        // Convert Map<String, Object> to Map<String, String>
        Map<String, String> data = new HashMap<>();
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            data.put(entry.getKey(), String.valueOf(entry.getValue()));  // Convert all values to String
        }

        // Add title and body to data
        data.put("title", title);
        data.put("body", body);

        // Create FCM message
        Message message = Message.builder()
                .setTopic(topic)
                .putAllData(data) // Now it's Map<String, String>
                .build();

        // Send message
        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Notification sent to topic '" + topic + "': " + response);
    }

}
