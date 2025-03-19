package bsu.edu.cs.model;

public class Vehicle {
    private String make;
    private String model;
    private int year;
    private double cityMpg;
    private double highwayMpg;
    private double combinedMpg;

    public Vehicle(String make,String model,double combinedMpg, int year){
        this.make = make;
        this.model = model;
        this.combinedMpg = combinedMpg;
        this.cityMpg = combinedMpg * 0.9;
        this.highwayMpg = combinedMpg * 1.1;
        this.year = year;
    }

    public Vehicle(String make, String model, double combinedMpg) {
        this.make = make;
        this.model = model;
        this.cityMpg = combinedMpg * 0.9;
        this.highwayMpg = combinedMpg * 1.1;
        this.combinedMpg = combinedMpg;
    }
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getCityMpg() {
        return cityMpg;
    }

    public void setCityMpg(double cityMpg) {
        this.cityMpg = cityMpg;
    }

    public double getHighwayMpg() {
        return highwayMpg;
    }

    public void setHighwayMpg(double highwayMpg) {
        this.highwayMpg = highwayMpg;
    }

    public double getCombinedMpg() {
        return combinedMpg;
    }

    public void setCombinedMpg(double combinedMpg) {
        this.combinedMpg = combinedMpg;
    }


}
