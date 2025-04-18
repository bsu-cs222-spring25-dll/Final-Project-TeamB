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

    private double purchasePrice;
    private double downPayment;
    private double loanAmount;
    private double interestRate;
    private double loanPeriod;

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

        this.purchasePrice = 10000;
        this.downPayment = 0.0;
        this.loanAmount = 0.0;
        this.interestRate = 0.0;
        this.loanPeriod = 0.0;
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

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(double downPayment) {
        this.downPayment = downPayment;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(double loanPeriod) {
        this.loanPeriod = loanPeriod;
    }


    public double calculateMonthlyPayment() {
        if (loanAmount <= 0 || loanPeriod <= 0 || interestRate <= 0) {
            return 0.0;
        }

        double monthlyRate = interestRate / 100 / 12;
        double numPayments = loanPeriod;
        System.out.println(loanAmount * (monthlyRate * Math.pow(1 + monthlyRate, numPayments)) /
                (Math.pow(1 + monthlyRate, numPayments) - 1));

        return loanAmount * (monthlyRate * Math.pow(1 + monthlyRate, numPayments)) /
                (Math.pow(1 + monthlyRate, numPayments) - 1);
    }
    
    public double calculateTotalInterest() {
        double monthlyPayment = calculateMonthlyPayment();
        System.out.println((monthlyPayment * loanPeriod) - loanAmount);
        return (monthlyPayment * loanPeriod) - loanAmount;
    }

    public double calculateTotalCostOfOwnership(int annualMiles, int yearsOwned,
                                                double fuelPricePerGallon, double electricityPricePerKWH) {
        double totalMiles = annualMiles * yearsOwned;

        double fuelCost;
        if ("Electricity".equalsIgnoreCase(fuelType)) {

            double kwhPerMile = 33.7 / combinedMpge;
            fuelCost = totalMiles * kwhPerMile * electricityPricePerKWH;
        } else {
            fuelCost = totalMiles / combinedMpg * fuelPricePerGallon;
        }

        double vehicleCost;
        if (loanAmount > 0 && loanPeriod > 0 && interestRate > 0) {
            vehicleCost = calculateMonthlyPayment() * loanPeriod;
            System.out.println(vehicleCost);
        } else {
            vehicleCost = purchasePrice;
            //System.out.println(vehicleCost);
        }

        return vehicleCost + fuelCost;
    }

    @Override
    public String toString() {
        return String.format("%d %s %s %s", year, make, model, trim);
    }
}