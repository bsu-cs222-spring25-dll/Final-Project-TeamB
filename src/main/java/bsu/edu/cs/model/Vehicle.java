package bsu.edu.cs.model;

public class Vehicle {
    private final String make;
    private final String model;
    private String trim;
    private final int year;
    private final double cityMpg;
    private final double highwayMpg;
    private final double combinedMpg;
    private double combinedMpge;
    private String fuelType;


    public Vehicle(String make, String model, String trim, double combinedMpg, double cityMpg, double highwayMpg, String year, String fuelType, double combinedMpge) {
        this.make = make;
        this.model = model;
        this.trim = trim;
        this.combinedMpg = combinedMpg;
        this.cityMpg = cityMpg;
        this.highwayMpg = highwayMpg;
        this.year = Integer.parseInt(year);
        this.fuelType = fuelType;
        this.combinedMpge = combinedMpge;
    }
    public Vehicle(String make, String model, String trim, double combinedMpg, double cityMpg, double highwayMpg, String year){
        this.make = make;
        this.model = model;
        this.trim = trim;
        this.combinedMpg = combinedMpg;
        this.cityMpg = cityMpg;
        this.highwayMpg = highwayMpg;
        this.year = Integer.parseInt(year);
    }

    public Vehicle(String make, String model, double combinedMpg, int year) {
        this.make = make;
        this.model = model;
        this.cityMpg = combinedMpg * 0.9;
        this.highwayMpg = combinedMpg * 1.1;
        this.combinedMpg = combinedMpg;
        this.year = year;
    }



    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }
    public int getYear() {
        return year;
    }
    public double getCityMpg() {
        return cityMpg;
    }
    public double getHighwayMpg() {
        return highwayMpg;
    }
    public double getCombinedMpg() {
        return combinedMpg;
    }
    public String getTrim() {return trim;}
    public String getFuelType(){return fuelType;}
    public double getCombinedMpge(){return combinedMpge;}
    @Override
    public String toString() {
        return String.format("%d %s %s %s", year, make, model, trim);
    }


}
