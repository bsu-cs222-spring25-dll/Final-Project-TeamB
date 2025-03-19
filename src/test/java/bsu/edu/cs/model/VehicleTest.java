package bsu.edu.cs.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {
    @Test
    public void TestBasicConstructor(){
        Vehicle vehicle = new Vehicle("Honda","Accord",32, 2012);

        assertEquals("Honda",vehicle.getMake());
        assertEquals("Accord", vehicle.getModel());
        assertEquals(32,vehicle.getCombinedMpg());

        assertEquals(2012, vehicle.getYear());
        assertEquals(28.8, vehicle.getCityMpg());
        assertEquals(35.2, vehicle.getHighwayMpg());
    }
    @Test
    public void TestDetailedConstructor(){
        Vehicle vehicle = new Vehicle("Toyota","Camry",32,2013);

        assertEquals("Toyota", vehicle.getMake());
        assertEquals("Camry", vehicle.getModel());
        assertEquals(32, vehicle.getCombinedMpg());
        assertEquals(2013, vehicle.getYear());
        assertEquals(35.2, vehicle.getHighwayMpg());
        assertEquals(28.8, vehicle.getCityMpg());
    }

}
