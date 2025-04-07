package bsu.edu.cs.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FuelEconomyService {
    private final VehicleDatabase vehicleDatabase;

    public FuelEconomyService(String csvFilePath) throws Exception {
        this.vehicleDatabase = new VehicleDatabase(csvFilePath);
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
        return CompletableFuture.supplyAsync(() -> {
            List<String> makes = new ArrayList<>();
            for (Vehicle vehicle : vehicleDatabase.getVehicles()) {
                if (year == null || year.isEmpty() ||
                        String.valueOf(vehicle.getYear()).equals(year)) {
                    if (!makes.contains(vehicle.getMake())) {
                        makes.add(vehicle.getMake());
                    }
                }
            }
            return makes;
        });
    }

    public CompletableFuture<List<String>> getModels(String year, String make) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> models = new ArrayList<>();
            for (Vehicle vehicle : vehicleDatabase.getVehicles()) {
                boolean yearMatch = year == null || year.isEmpty() ||
                        String.valueOf(vehicle.getYear()).equals(year);
                boolean makeMatch = make == null || make.isEmpty() ||
                        vehicle.getMake().equalsIgnoreCase(make);

                if (yearMatch && makeMatch && !models.contains(vehicle.getModel())) {
                    models.add(vehicle.getModel());
                }
            }
            return models;
        });
    }

    public CompletableFuture<List<String>> getTrims(String year, String make, String model) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> trims = new ArrayList<>();
            for (Vehicle vehicle : vehicleDatabase.getVehicles()) {
                boolean yearMatch = year == null || year.isEmpty() ||
                        String.valueOf(vehicle.getYear()).equals(year);
                boolean makeMatch = make == null || make.isEmpty() ||
                        vehicle.getMake().equalsIgnoreCase(make);
                boolean modelMatch = model == null || model.isEmpty() ||
                        vehicle.getModel().equalsIgnoreCase(model);

                if (yearMatch && makeMatch && modelMatch &&
                        vehicle.getTrim() != null && !vehicle.getTrim().isEmpty() &&
                        !trims.contains(vehicle.getTrim())) {
                    trims.add(vehicle.getTrim());
                }
            }
            return trims;
        });
    }

    public CompletableFuture<Vehicle> searchVehicles(String year, String make, String model, String trim) {
        return CompletableFuture.supplyAsync(() -> {
            List<Vehicle> matches = vehicleDatabase.searchVehicles(year, make, model, trim);
            if (!matches.isEmpty()) {
                return matches.getFirst();
            }
            return new Vehicle(make, model, trim != null ? trim : "Standard",
                    25.0, 23.0, 27.0, year != null ? year : "2023");
        });
    }
}