package bsu.edu.cs.model;

public class Vehicle {
    private final String make;
    private final String model;
    private final String trim;
    private final int year;
    private final double cityMpg;
    private final double highwayMpg;
    private final double combinedMpg;
    private final double combinedMpge;
    private final String fuelType;

    public Vehicle(String make, String model, String trim, double combinedMpg,
                   double cityMpg, double highwayMpg, String year, String fuelType,
                   double combinedMpge) {
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

    public Vehicle(String make, String model, String trim, double combinedMpg,
                   double cityMpg, double highwayMpg, String year) {
        this(make, model, trim, combinedMpg, cityMpg, highwayMpg, year, null, 0);
    }

    public Vehicle(String make, String model, double combinedMpg, int year) {
        this(make, model, "Standard", combinedMpg, combinedMpg * 0.9,
                combinedMpg * 1.1, String.valueOf(year));
    }

    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getCityMpg() { return cityMpg; }
    public double getHighwayMpg() { return highwayMpg; }
    public double getCombinedMpg() { return combinedMpg; }
    public String getTrim() { return trim; }
    public String getFuelType() { return fuelType; }
    public double getCombinedMpge() { return combinedMpge; }

    @Override
    public String toString() {
        return String.format("%d %s %s %s", year, make, model, trim);
    }
}