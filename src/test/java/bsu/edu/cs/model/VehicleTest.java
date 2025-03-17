package bsu.edu.cs.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {
    @Test
    public void TestBasicConstructor(){
        Vehicle vehicle = new Vehicle("Honda","Accord",32);

        assertEquals("Honda",vehicle.make);
        assertEquals("Accord",vehicle.model);
        assertEquals(32,vehicle.combinedMpg);

        assertEquals(0,vehicle.year);
        assertEquals(0,vehicle.cityMpg);
        assertEquals(0,vehicle.highwayMpg);
    }
    @Test
    public void TestDetailedConstructor(){
        Vehicle vehicle = new Vehicle("Toyota","Camry",32,2013);

        assertEquals("Toyota",vehicle.make);
        assertEquals("Camry",vehicle.model);
        assertEquals(32,vehicle.combinedMpg);
        assertEquals(2013,vehicle.year);
        assertEquals(32,vehicle.highwayMpg);
        assertEquals(32,vehicle.cityMpg);
    }

}
