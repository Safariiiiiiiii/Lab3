package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private Map<String, Map<String, Object>> countriesTranslation = new HashMap<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObj = jsonArray.getJSONObject(i);

                // Construct a country as the key
                countryObj.remove("id");
                countryObj.remove("alpha2");
                String alpha3 = (String) countryObj.remove("alpha3");

                // Get the translations for the country
                Map<String, Object> countryTranslation = countryObj.toMap();
                countriesTranslation.put(alpha3, countryTranslation);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // return an appropriate list of language codes.
        Map<String, Object> countryTranslation = countriesTranslation.get(country);
        if (countryTranslation == null) {
            throw new RuntimeException("Country not found: " + country);
        }
        return new ArrayList<>(countryTranslation.keySet());

    }

    @Override
    public List<String> getCountries() {
        // return an appropriate list of country codes.
        return new ArrayList<>(countriesTranslation.keySet());
    }

    @Override
    public String translate(String country, String language) {
        Map<String, Object> countryTranslation = countriesTranslation.get(country);
        if (countryTranslation == null) {
            throw new RuntimeException("Country not found: " + country);
        }
        String translation = (String) countryTranslation.get(language);
        if (translation == null) {
            throw new RuntimeException("Language not found: " + language);
        }
        return translation;
    }

    /**
     *  Basic test of constructor.
     * @param args not used
     */
    public static void main(String[] args) {
        JSONTranslator translator = new JSONTranslator();

    }
}