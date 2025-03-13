package bsu.edu.cs.model;

public class Vehicle {
    public String make;
    public String model;
    public int year;
    public double cityMpg;
    public double highwayMpg;
    public double combinedMpg;

    public Vehicle(String make,double combinedMpg){
        this.make = make;
        this.combinedMpg = combinedMpg;
    }

    public Vehicle(String make, String model, double combinedMpg, int year) {
        this.make = make;
        this.model = model;
        this.cityMpg = combinedMpg;
        this.highwayMpg = combinedMpg;
        this.combinedMpg = combinedMpg;
        this.year = year;
    }
}
