package bsu.edu.cs.model;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDatabase {
    //private String apiUrl = "https://vpic.nhtsa.dot.gov/api/";
    private final List<Vehicle> vehicles;

    public VehicleDatabase() {
        vehicles = new ArrayList<>();
        //vehicles.addAll(getDefaultVehicles());
        try {
            importVehiclesFromCSV("src/main/resources/vehicles.csv");
        } catch (Exception e) {
            System.out.println("Could not import vehicles from CSV: " + e.getMessage());
        }
    }

//    public List<Vehicle> getDefaultVehicles(){
//        List<Vehicle> vehicles = new ArrayList<>();
//
//        vehicles.add(new Vehicle("Toyota","Prius",56, 2024));
//        vehicles.add(new Vehicle("Toyota","Camry",31, 2023));
//        vehicles.add(new Vehicle("Honda","Ridgeline",20, 2020));
//        vehicles.add(new Vehicle("Chevrolet","Silverado 1500",16, 2004));
//
//        return vehicles;
//    }

    public void importVehiclesFromCSV(String filename) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            reader.readNext();

            String[] nextLine;
            //System.out.println(nextLine[63]);
            while ((nextLine = reader.readNext()) != null) {
                try {
                    if (nextLine.length >= 63) {
                        String make = nextLine[46];
                        String model = nextLine[47];
                        int year = Integer.parseInt(nextLine[63]);

                        double combinedMpg;
                        double highwayMpg;
                        double cityMpg;
                        try {
                            combinedMpg = Double.parseDouble(nextLine[15]);
                            highwayMpg = Double.parseDouble(nextLine[4]);
                            cityMpg = Double.parseDouble(nextLine[34]);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            continue;
                        }

                        Vehicle vehicle = new Vehicle(make, model, combinedMpg,cityMpg,highwayMpg, year);
                        vehicles.add(vehicle);
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing vehicle row: " + e.getMessage());
                }
            }
        }
    }
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Vehicle> searchVehicles(String query){
        List<Vehicle> results = new ArrayList<>();
        String lowercaseQuery = query.toLowerCase();

        for (Vehicle vehicle: getVehicles()){
            if(vehicle.getMake().toLowerCase().contains(lowercaseQuery) || vehicle.getModel().toLowerCase().contains(lowercaseQuery)){
                results.add(vehicle);
            }
        }
        return results;
    }
}
