package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {

    // Stores the country names and their corresponding codes，key is country name, value is country code
    private final Map<String, String> countryNameCodeMap;

    /**
     * Default constructor which will load the country codes from "country-codes.txt" in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     *
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            countryNameCodeMap = new HashMap<>();
            if (lines.size() > 1) {
                for (int i = 1; i < lines.size(); i++) {
                    String[] parts = lines.get(i).split("\t");
                    countryNameCodeMap.put(parts[0], parts[2].toLowerCase());
                }
            }
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the country for the given country code.
     *
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        return countryNameCodeMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(code))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
    }

    /**
     * Returns the code of the country for the given country name.
     *
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        return countryNameCodeMap.get(country);
    }

    /**
     * Returns how many countries are included in this code converter.
     *
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return countryNameCodeMap.size();
    }
}
