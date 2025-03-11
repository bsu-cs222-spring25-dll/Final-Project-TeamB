package bsu.edu.cs;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class VehicleFinder {
    public static String[] findVehicleByMakeModelYear(String filePath, String make, String model, String year) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext();  // Read the header line

            // Find the indices for make, model, and year
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].toLowerCase(), i);
            }

            Integer makeIndex = headerMap.get("make");
            Integer modelIndex = headerMap.get("model");
            Integer yearIndex = headerMap.get("year");
            Integer cityMpg = headerMap.get("city08");
            Integer highwayMpg = headerMap.get("highway08");
            Integer combinedMpg = headerMap.get("comb08");



            if (makeIndex == null || modelIndex == null || yearIndex == null) {
                throw new IllegalArgumentException("CSV does not contain required headers: Make, Model, Year");
            }

            String[] values;
            while ((values = reader.readNext()) != null) {
                if (values.length >= 3 &&
                        values[makeIndex].equalsIgnoreCase(make) &&
                        values[modelIndex].equalsIgnoreCase(model) &&
                        values[yearIndex].equalsIgnoreCase(year)){

                    String vehicleMake = headers[makeIndex] + ": " + values[makeIndex];
                    System.out.println(vehicleMake);
                    String vehicleModel = headers[modelIndex] + ": " + values[modelIndex];
                    System.out.println(vehicleModel);
                    String vehicleYear = headers[yearIndex] + ": " + values[yearIndex];
                    System.out.println(vehicleYear);
                    String vehicleCity = headers[cityMpg] +": " + values[cityMpg];
                    System.out.println(vehicleCity);

                    System.out.println(headers[highwayMpg] + ": " + values[highwayMpg]);

                    System.out.println(headers[combinedMpg] + ": " + values[combinedMpg]);





                    return values;
                }
            }
        }
        return null;
    }
    public static void main(String[] args) {
        try {
            String filePath = "C:\\Users\\cmj17\\IdeaProjects\\Final-Project\\src\\main\\resources\\vehicles.csv"; // Specify the path to your CSV file
            String make = "Toyota";
            String model = "Camry";
            String year = "2020";

            String[] vehicle = findVehicleByMakeModelYear(filePath, make, model, year);

            if (vehicle != null) {
                System.out.println("Vehicle found:");
            } else {
                System.out.println("Vehicle not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
