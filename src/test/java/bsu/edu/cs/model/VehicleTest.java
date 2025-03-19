package bsu.edu.cs.model;

import bsu.edu.cs.view.MainView;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {
    @Test
    public void TestBasicConstructor(){
        Vehicle vehicle = new Vehicle("Honda","Accord",32, 2);

        assertEquals("Honda",vehicle.getMake());
        assertEquals("Accord", vehicle.getModel());
        assertEquals(32,vehicle.getCombinedMpg());

        assertEquals(0, vehicle.getYear());
        assertEquals(0, vehicle.getCityMpg());
        assertEquals(0, vehicle.getHighwayMpg());
    }
    @Test
    public void TestDetailedConstructor(){
        Vehicle vehicle = new Vehicle("Toyota","Camry",32);

        assertEquals("Toyota", vehicle.getMake());
        assertEquals("Camry", vehicle.getModel());
        assertEquals(32, vehicle.getCombinedMpg());
        assertEquals(2013, vehicle.getYear());
        assertEquals(32, vehicle.getHighwayMpg());
        assertEquals(32, vehicle.getCityMpg());
    }

}
