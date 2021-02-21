package iti.jets.gfive.ui.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.*;

public class StatsModel {
    private final SimpleIntegerProperty malesCount = new SimpleIntegerProperty();
    private final SimpleIntegerProperty femalesCount = new SimpleIntegerProperty();
    private final Map<String, SimpleIntegerProperty> countryPropertiesMap = new HashMap<>();

    {
        initCountryMap("Unspecified", "Albania", "Algeria", "Andorra", "Argentia", "Austalia", "Bahrain", "Belize", "Bolivia",
                "Cambodia", "Cameroon", "Canada", "Dominica", "Egypt", "Estonia", "Finland", "Greece", "Grenada", "Haiti", "Iceland", "Japan", "Laos", "Lebanon",
                "Libya", "Mali", "Panama", "Saudi Arabia", "serbia", "Turkey", "Vietnam", "Yemen", "Zambia", "Zimbabwe");
    }

    private void initCountryMap(String... countryNames) {
        for (String name : countryNames) {
            countryPropertiesMap.put(name, new SimpleIntegerProperty());
        }
    }

    public int getMalesCount() {
        return malesCount.get();
    }

    public SimpleIntegerProperty malesCountProperty() {
        return malesCount;
    }

    public void setMalesCount(int malesCount) {
        this.malesCount.set(malesCount);
    }

    public int getFemalesCount() {
        return femalesCount.get();
    }

    public SimpleIntegerProperty femalesCountProperty() {
        return femalesCount;
    }

    public void setFemalesCount(int femalesCount) {
        this.femalesCount.set(femalesCount);
    }

    public Map<String, SimpleIntegerProperty> getCountryPropertiesMap() {
        return countryPropertiesMap;
    }
}
