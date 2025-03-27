package bsu.edu.cs.model;

import bsu.edu.cs.model.Vehicle;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.json.JSONArray;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FuelEconomyService {
    private static final String API_BASE_URL = "https://www.fueleconomy.gov/ws/rest/vehicle/menu/";
    private final OkHttpClient client;

    public FuelEconomyService() {
        // Create OkHttpClient with logging interceptor for debugging
        client = new OkHttpClient.Builder()
                .addInterceptor(new okhttp3.logging.HttpLoggingInterceptor()
                        .setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY))
                .build();
    }
    public CompletableFuture<List<String>> getYears() {
        return CompletableFuture.supplyAsync(() -> {
            List<String> years = new ArrayList<>();
            int currentYear = java.time.Year.now().getValue();
            for (int year = currentYear; year >= 1984; year--) {
                years.add(String.valueOf(year));
            }
            return years;
        });
    }

    public CompletableFuture<List<String>> getMakes(String year) {
        return sendApiRequest(API_BASE_URL + "make?year=" + year);
    }

    public CompletableFuture<List<String>> getModels(String year,String make) {
        return sendApiRequest(API_BASE_URL + "model?year=" + year + "&make=" + make);
    }

    public CompletableFuture<List<String>> getTrims(String year,String make,String model) {
        return sendApiRequest(API_BASE_URL + "model?year=" + year + "&make=" + make + "&model" + model);
    }

    public CompletableFuture<Vehicle> searchVehicles(String year, String make, String model, String trim) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = API_BASE_URL + "search?year=" + year +
                        "&make=" + make +
                        "&model=" + model +
                        "&trim=" + trim;

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Accept", "application/json")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseBody = response.body() != null ? response.body().string() : "";
                    return parseVehicleDetails(responseBody);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error searching vehicle", e);
            }
        });
    }

    private CompletableFuture<List<String>> sendApiRequest(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Accept", "application/json")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseBody = response.body() != null ? response.body().string() : "";
                    return parseJSONArray(responseBody);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching data", e);
            }
        });
    }

    private List<String> parseJSONArray(String jsonResponse){
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray itemsArray = jsonObject.optJSONArray("menuItem");

            if (itemsArray == null) return new ArrayList<>();

            return itemsArray.toList().stream()
                    .map(item -> ((JSONObject)item).getString("text"))
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new RuntimeException("Error parsing JSON",e);
        }
    }
    private Vehicle parseVehicleDetails(String jsonResponse){
        try{
            JSONObject vehicleDetails = new JSONObject(jsonResponse);

            return new Vehicle(
                    vehicleDetails.optString("make","N/A"),
                    vehicleDetails.optString("trim","N/A"),
                    vehicleDetails.optString("model","N/A"),
                    parseDouble(vehicleDetails, "city08"),
                    parseDouble(vehicleDetails, "highway08"),
                    parseDouble(vehicleDetails, "combined08"),
                    vehicleDetails.optString("year","N/A")

                    );
        } catch (Exception e){
            throw new RuntimeException("Error parsing vehicle details",e);
        }
    }
    private double parseDouble(JSONObject json, String key){
        try {
            return json.optDouble(key, 0.0);
        } catch (Exception e){
            return 0.0;
        }
    }
}
