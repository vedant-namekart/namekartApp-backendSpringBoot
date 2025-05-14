package com.namekartapp.namekartappapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonOperations {
    private static final String JSON_FILE_PATH = "auction_data.json";

    // Method to create or update the JSON file with auction datetime data
    public void createOrUpdateAuctionData(String collectionName, String datetime) {
        JsonObject auctionData = readAuctionDataFromFile();

        // If the file doesn't exist (first-time run), populate with empty values
        if (auctionData.size() == 0) {
            createEmptyAuctionData(auctionData);
        }

        // Update or add the collection with the provided datetime
        updateCollectionField(auctionData, collectionName, datetime);

        // Write the updated auction data back to the JSON file
        writeAuctionDataToFile(auctionData);
    }

    // Method to check which collections have mismatched or missing datetime values
    public List<String> checkUpdate(Map<String, String> datetimeMap) {
        JsonObject auctionData = readAuctionDataFromFile();
        List<String> mismatchedCollections = new ArrayList<>();

        // Iterate through the provided datetime map to check each collection's datetime value
        for (Map.Entry<String, String> entry : datetimeMap.entrySet()) {
            String collectionName = entry.getKey();
            String providedDatetime = entry.getValue();

            if (auctionData.has(collectionName)) {
                JsonObject collectionData = auctionData.getAsJsonObject(collectionName);
                String storedDatetime = collectionData.get("datetime").getAsString();

                // If the stored datetime is empty or does not match the provided datetime
                if (storedDatetime.isEmpty() || !storedDatetime.equals(providedDatetime)) {
                    mismatchedCollections.add(collectionName);
                }
            } else {
                // If the collection doesn't exist in the data (which is a mismatch)
                mismatchedCollections.add(collectionName);
            }
        }

        return mismatchedCollections;  // Return a list of collections with mismatched or missing datetime
    }

    // Helper method to get the current datetime in the correct format (yyyy-MM-dd'T'HH:mm:ss)
    private String getCurrentDatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(new Date());
    }

    // Helper method to read the auction data from the JSON file
    JsonObject readAuctionDataFromFile() {
        JsonObject auctionData = new JsonObject();
        try {
            File file = new File(JSON_FILE_PATH);
            if (file.exists()) {
                // File exists, so read the data from the file
                FileReader fileReader = new FileReader(file);
                Gson gson = new Gson();
                auctionData = gson.fromJson(fileReader, JsonObject.class);
                fileReader.close();
            } else {
                // File does not exist, initialize an empty JsonObject
                auctionData = new JsonObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auctionData;
    }

    // Helper method to create empty auction data with empty datetime values for all collections
    private void createEmptyAuctionData(JsonObject auctionData) {
        String[] collections = {
                "dropcatch", "dynadot", "godaddy", "namecheap", "namesilo",
                "dropcatchlist", "dynadotlist", "godaddylist", "namecheaplist", "namesilolist",
                "BidActivity", "BotActivity", "CloseoutsActivity", "DailyLiveReports",
                "LiveStatesSummary", "LostTracker", "TargetReports", "UserActivity", "WinTracker"
        };

        // Initialize all collections with empty datetime values
        for (String collection : collections) {
            JsonObject collectionData = new JsonObject();
            collectionData.addProperty("datetime", "");  // Empty value for datetime
            auctionData.add(collection, collectionData);
        }
    }

    // Helper method to update a collection with the provided datetime
    private void updateCollectionField(JsonObject auctionData, String collectionName, String datetime) {
        JsonObject collectionData = auctionData.has(collectionName) ? auctionData.getAsJsonObject(collectionName) : new JsonObject();
        collectionData.addProperty("datetime", datetime);  // Update datetime value

        // Add or update the collection data in the auctionData
        auctionData.add(collectionName, collectionData);
    }

    // Helper method to write the auction data to the JSON file
    private void writeAuctionDataToFile(JsonObject auctionData) {
        try {
            FileWriter fileWriter = new FileWriter(JSON_FILE_PATH);
            Gson gson = new Gson();
            gson.toJson(auctionData, fileWriter);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
