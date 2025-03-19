package bsu.edu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;

public class VehicleDatabaseTest {
    private VehicleDatabase database;

    @BeforeEach
    public void setUp(){
        database = new VehicleDatabase();
    }

    @Test
    public void getDefaultVehicles(){
        List<Vehicle> vehicles = database.getDefaultVehicles();

        assertEquals(4,vehicles.size());

        Vehicle prius = vehicles.getFirst();
        assertEquals("Prius", prius.getModel());
    }

    @Test
    public void searchVehicleByMake(){
        List<Vehicle> toyota = database.searchVehicles("Toyota");

        assertEquals(2,toyota.size());
    }
}
