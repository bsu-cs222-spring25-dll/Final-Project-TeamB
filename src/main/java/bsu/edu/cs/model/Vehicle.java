package bsu.edu.cs.model;

public class Vehicle {
    public String make;
    public String model;
    public int year;
    public double cityMpg;
    public double highwayMpg;
    public double combinedMpg;
    public double yearsOwned;

    public Vehicle(String make,String model,double combinedMpg, int year){
        this.make = make;
        this.model = model;
        this.combinedMpg = combinedMpg;
        this.year = year;
    }

    public Vehicle(String make, String model, double combinedMpg, double timeOwned) {
        this.make = make;
        this.model = model;
        this.cityMpg = combinedMpg;
        this.highwayMpg = combinedMpg;
        this.combinedMpg = combinedMpg;
        this.year = year;
        this.yearsOwned = timeOwned;
    }
}
