package com.namekartapp.namekartappapi;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProgramHelper {

    public static Firestore db=FirestoreClient.getFirestore(); ;



    // Helper method to get the current datetime in the correct format (yyyy-MM-dd'T'HH:mm:ss)
    public static String getCurrentDatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(new java.util.Date());
    }

    public static void updateJsonWithNewDocumentId(String pathKey, int newId, String timestamp) {
        File jsonFile = new File("latest_ids.json");

        try {
            // Load or create JSON structure
            JSONObject root;
            if (jsonFile.exists()) {
                String content = new String(Files.readAllBytes(jsonFile.toPath()), StandardCharsets.UTF_8);
                root = new JSONObject(content);
            } else {
                root = new JSONObject();
            }

            // Create or update entry for the given path
            JSONObject entry = new JSONObject();
            entry.put("lastId", newId);
            entry.put("lastUpdated", timestamp);

            root.put(pathKey, entry);

            // Save updated JSON
            Files.write(jsonFile.toPath(), root.toString(2).getBytes(StandardCharsets.UTF_8));
            System.out.println("✅ JSON metadata updated for: " + pathKey);

        } catch (IOException | JSONException e) {
            System.err.println("❌ Failed to update JSON metadata: " + e.getMessage());
        }
    }

    public static int getLatestId(String pathKey) {
        File jsonFile = new File("latest_ids.json");

        if (!jsonFile.exists()) {
            System.out.println("⚠️ Metadata file not found.");
            return 0;
        }

        try {
            String content = Files.readString(jsonFile.toPath());
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(content, JsonObject.class);

            if (root.has(pathKey)) {
                JsonObject entry = root.getAsJsonObject(pathKey);
                return entry.get("lastId").getAsInt();
            } else {
                System.out.println("⚠️ Path not found in metadata: " + pathKey);
                return 0;
            }

        } catch (Exception e) {
            System.err.println("❌ Error reading latest ID: " + e.getMessage());
            return 0;
        }
    }


    public static void sendNotificationToDevices(String titleOfNotification,String message,String sendToTopic) {
        try {
            String title = titleOfNotification;

            // Build the message with notification and data
            Message firebaseMessage = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(message)
                            .build())
                    .setTopic(sendToTopic)  // Send to devices subscribed to "godaddy" topic
                    .build();

            // Send the notification
            FirebaseMessaging.getInstance().send(firebaseMessage);
            System.out.println("Notification sent successfully ");
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
        }
    }

    public static int getLastDocumentIdFromFirestore(String collection, String name, String subCollection) {
        try {
            CollectionReference subCollectionRef = db.collection(collection)
                    .document(name)
                    .collection(subCollection);

            ApiFuture<QuerySnapshot> future = subCollectionRef.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            int maxId = 0;
            for (QueryDocumentSnapshot doc : documents) {
                try {
                    int id = Integer.parseInt(doc.getId());
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException ignored) {
                    // Skip non-numeric document IDs like "metadata"
                }
            }

            return maxId;
        } catch (Exception e) {
            System.err.println("Error fetching last document ID: " + e.getMessage());
            return 0;
        }
    }

    public static void addAuctionToFirestoreWithNumericId(String collection, String name, String subCollection, Map<String, Object> data, String documentId, String universalDatetime) {
        try {
            // Reference main document
            DocumentReference mainDocRef = db.collection(collection).document(name);
            mainDocRef.set(new HashMap<>(), SetOptions.merge());

            // Ensure subcollection "metadata" exists
            DocumentReference subCollectionDocRef = mainDocRef.collection(subCollection).document("metadata");
            subCollectionDocRef.set(new HashMap<>(), SetOptions.merge());

            // Set auction document using numeric ID
            DocumentReference actualDocRef = mainDocRef.collection(subCollection).document(documentId);

            ApiFuture<WriteResult> future = actualDocRef.set(data, SetOptions.merge());
            WriteResult result = future.get();
            System.out.println("Auction successfully added with numeric ID: " + documentId + " at " + result.getUpdateTime());

        } catch (Exception e) {
            System.err.println("Error adding auction with numeric ID: " + e.getMessage());
        }
    }




    public static void doAllSendingAndDatabaseAddingTask(String type,String path, String name, String category, String subCollections, Map<String, Object> data) {

        // 🔄 Get last document ID from Firestore and increment
        int lastId = getLastDocumentIdFromFirestore(category, name, subCollections);
        int newId = lastId + 1;

        // ⏰ Generate the universal datetime
        String universalDatetime = getCurrentDatetime();

        // ✅ Add ID and datetime to data
        data.put("id", String.valueOf(newId));
        // 🔊 Broadcast websocket message
        new DomainWebSocketHandler().broadcastMessage(type,path, data);

        // 📥 Add to Firestore with numeric ID
        addAuctionToFirestoreWithNumericId(category, name, subCollections, data, String.valueOf(newId), universalDatetime);

        // 📝 Still update JSON if you want local tracking
        updateJsonWithNewDocumentId(path, newId, universalDatetime);
    }

    public static List<String> getSubCollectionsForPath(String path) {
        Firestore db = FirestoreClient.getFirestore();

        String[] segments = path.split("\\.");
        // Case 1: Collection path (odd number of segments)
        if (segments.length % 2 != 0) {
            CollectionReference collectionRef = db.collection(segments[0]);
            for (int i = 1; i < segments.length; i++) {
                System.out.println(segments[i]);
                collectionRef = collectionRef.document(segments[i]).collection(segments[++i]);
            }

            // Get document IDs inside the collection
            List<String> documentIds = new ArrayList<>();
            collectionRef.listDocuments().forEach(doc -> documentIds.add(doc.getId()));
            return documentIds;

        } else {
            // Case 2: Document path (even number of segments)
            DocumentReference docRef = db.collection(segments[0]).document(segments[1]);
            for (int i = 2; i < segments.length; i += 2) {
                docRef = docRef.collection(segments[i]).document(segments[i + 1]);
            }

            // Get subcollections inside the document
            List<String> subcollectionNames = new ArrayList<>();
            for (CollectionReference subCol : docRef.listCollections()) {
                subcollectionNames.add(subCol.getId().trim());
                System.out.println(subCol.getId().trim());
            }
            return subcollectionNames;
        }
    }

    public static Map<String, Object> updateFirestoreByPath(
            String fullPath, String fieldPath, Object newValue
    ) {
        try {
            // 🔹 Parse the Firestore document path: "live/dropcatch/auctions/7"
            String[] segments = fullPath.split("~");
            if (segments.length % 2 != 0) {
                System.err.println("Invalid Firestore document path: " + fullPath);
                return null;
            }

            // 🔹 Traverse to the target document
            DocumentReference docRef = db.collection(segments[0]).document(segments[1]);
            for (int i = 2; i < segments.length; i += 2) {
                docRef = docRef.collection(segments[i]).document(segments[i + 1]);
            }

            // 🔹 Get the full document
            DocumentSnapshot docSnap = docRef.get().get();
            Map<String, Object> data = docSnap.getData();
            if (data == null) {
                System.err.println("Document not found or is empty.");
                return null;
            }

            // 🔹 Split the fieldPath: "data.uiButtons[0].button1.onclick"
            String[] parts = fieldPath.split("\\.");
            Object current = data;

            for (int i = 0; i < parts.length - 1; i++) {
                String key = parts[i];

                if (key.contains("[")) {
                    // Handle array key like "uiButtons[0]"
                    String arrayKey = key.substring(0, key.indexOf("["));
                    int index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));

                    List<Object> list = (List<Object>) ((Map<String, Object>) current).get(arrayKey);
                    current = list.get(index);
                } else {
                    current = ((Map<String, Object>) current).get(key);
                }
            }

            // 🔹 Update the final key (e.g., 'onclick')
            String lastKey = parts[parts.length - 1];
            ((Map<String, Object>) current).put(lastKey, newValue);

            // 🔹 Save the entire updated document back to Firestore
            docRef.set(data, SetOptions.merge()).get(); // Use merge to avoid overwriting other fields

            // ✅ Return the entire updated document (not just the modified part)

            System.out.println("send to mobile "+data);
            return data;

        } catch (Exception e) {
            System.err.println("❌ Error updating Firestore: " + e.getMessage());
            return null;
        }
    }




}
