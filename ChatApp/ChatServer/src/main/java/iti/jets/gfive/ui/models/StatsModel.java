package iti.jets.gfive.ui.models;

import javafx.beans.property.SimpleIntegerProperty;

public class StatsModel {
    private final SimpleIntegerProperty malesCount = new SimpleIntegerProperty();
    private final SimpleIntegerProperty femalesCount = new SimpleIntegerProperty();

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
}
