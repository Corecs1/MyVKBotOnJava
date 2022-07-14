package logic.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Cities {
    private final Map<String, String> cities;

    public Cities() throws IOException {
        this.cities = new HashMap<>();
        String line;
        String csvFile = "src\\main\\resources\\cities_ru_en.csv";
        BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
        while ((line = csvReader.readLine()) != null) {
            String csvSplitBy = ",";
            String[] text = line.split(csvSplitBy);
            cities.put(text[0].toLowerCase(), text[1].toLowerCase());
        }
    }

    public String findCity(String ruCity) {
        return cities.getOrDefault(ruCity.toLowerCase(), "Не найдено");
    }
}
