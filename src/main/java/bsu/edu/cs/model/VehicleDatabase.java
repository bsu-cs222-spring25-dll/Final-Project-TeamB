package bsu.edu.cs.model;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VehicleDatabase {
    private final List<Vehicle> vehicles;

    public VehicleDatabase(String csvFilePath) throws IOException {
        vehicles = new ArrayList<>();
        importVehiclesFromCSV(csvFilePath);
    }

    public void importVehiclesFromCSV(String filename) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {

            String[] headers = reader.readNext();

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    Vehicle vehicle = getVehicle(nextLine);
                    vehicles.add(vehicle);
                } catch (Exception e) {
                    System.out.println("Error parsing vehicle row: " + e.getMessage());
                }
            }
        }
    }

    private Vehicle getVehicle(String[] nextLine) {
        String make = nextLine[46];
        String model = nextLine[65];
        String trim = nextLine[47];
        String year = nextLine[63];

        double cityMpg = Double.parseDouble(nextLine[4]);
        double highwayMpg = Double.parseDouble(nextLine[34]);
        double combinedMpg = Double.parseDouble(nextLine[15]);

        if (Objects.equals(nextLine[30], "Electricity")){
            String fuelType = nextLine[30];
            double combinedMpge = Double.parseDouble(nextLine[19]);
            return new Vehicle(make, model, trim, combinedMpg, cityMpg, highwayMpg, year, fuelType,combinedMpge);

        }

        return new Vehicle(make, model, trim, combinedMpg, cityMpg, highwayMpg, year);
    }

    public List<Vehicle> searchVehicles(String year, String make, String model, String trim) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            boolean yearMatch = year == null || year.isEmpty() || vehicle.getYear() == Integer.parseInt(year);
            boolean makeMatch = make == null || make.isEmpty() ||
                    vehicle.getMake().equalsIgnoreCase(make);
            boolean modelMatch = model == null || model.isEmpty() ||
                    vehicle.getModel().equalsIgnoreCase(model);
            boolean trimMatch = trim == null || trim.isEmpty() ||
                    (vehicle.getTrim() != null &&
                            vehicle.getTrim().equalsIgnoreCase(trim));

            if (yearMatch && makeMatch && modelMatch && trimMatch) {
                results.add(vehicle);
            }
        }
        return results;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}