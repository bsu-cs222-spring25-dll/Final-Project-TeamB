package bsu.edu.cs.model;

import java.util.ArrayList;
import java.util.List;

public class VehicleDatabase {
    private String apiUrl = "https://vpic.nhtsa.dot.gov/api/";

    public List<Vehicle> getDefaultVehicles(){
        List<Vehicle> vehicles = new ArrayList<>();

        vehicles.add(new Vehicle("Toyota","Prius",56));
        vehicles.add(new Vehicle("Toyota","Camry",31));
        vehicles.add(new Vehicle("Honda","Ridgeline",20));
        vehicles.add(new Vehicle("Tesla","Model 3",132));

        return vehicles;
    }

    public List<Vehicle> searchVehicles(String query){
        List<Vehicle> results = new ArrayList<>();
        String lowercaseQuery = query.toLowerCase();

        for (Vehicle vehicle: getDefaultVehicles()){
           if(vehicle.make.toLowerCase().contains(lowercaseQuery) || vehicle.model.toLowerCase().contains(lowercaseQuery)){
               results.add(vehicle);
           }
        }
        return results;
    }
}
