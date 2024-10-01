package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    public static final String ALPHA2 = "alpha2";
    public static final String ALPHA3 = "alpha3";
    public static final String ID = "id";
    private final Map<String, JSONObject> countryCodeJsonMap;
    private final CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
    private final LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

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

            String jsonString = Files.readString(Paths.get(getClass()
                    .getClassLoader()
                    .getResource(filename)
                    .toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            countryCodeJsonMap = new HashMap<>();

            jsonArray.forEach(item -> {
                JSONObject jsonObject = (JSONObject) item;
                countryCodeJsonMap.put(jsonObject.getString(ALPHA3), jsonObject);
            });

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        if (!countryCodeJsonMap.containsKey(country)) {
            return List.of();
        }
        return List.copyOf(countryCodeJsonMap.get(country)
                .keySet()
                .stream()
                .filter(lanCode -> !ALPHA2.equals(lanCode) && !ALPHA3.equals(lanCode) && !ID.equals(lanCode))
                .collect(Collectors.toList())
        );
    }

    @Override
    public List<String> getCountries() {
        return List.copyOf(countryCodeJsonMap.keySet());
    }

    @Override
    public String translate(String country, String language) {
        return countryCodeJsonMap.get(country).get(language).toString();
    }
}
