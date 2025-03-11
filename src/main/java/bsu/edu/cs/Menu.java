package bsu.edu.cs;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Scanner;
import java.util.*;

public class Menu {
    public static double getMpg() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter your avg mpg: ");
        return scn.nextDouble();
    }

    public static double getYearlyMileage() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter your Yearly Mileage Driven: ");
        return scn.nextDouble();
    }

    public static double getAvgGallonCost() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter the avg cost per gallon of fuel: ");
        return scn.nextDouble();

    }

    public static void main(String[] args) throws IOException {

        try {
            String filePath = "C:\\Users\\cmj17\\IdeaProjects\\Final-Project\\src\\main\\resources\\vehicles.csv"; // Specify the path to your CSV file
            Map<String, List<String>> columnData = readCSV(filePath);

            // Print the map to verify the result
            columnData.forEach((key, value) -> System.out.println(key + ": " + value));
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        }

    public static Map<String, List<String>> readCSV(String filePath) throws IOException,CsvException {
        Map<String, List<String>> columnData = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext();
            if (headers != null) {
                for (String header : headers) {
                    columnData.put(header, new ArrayList<>());
                }

                String[] values;
                while ((values = reader.readNext()) != null) {
                    for (int i = 0; i < values.length; i++) {
                        columnData.get(headers[i]).add(values[i]);
                    }
                }
            }
        }

        return columnData;
    }
}
