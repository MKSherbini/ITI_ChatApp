package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.ui.helpers.db.DBStats;

public class StatsManager {
    private static final StatsManager instance = new StatsManager();

    private StatsManager() {

    }

    public static StatsManager getInstance() {
        return instance;
    }

    public void initStats() {
        var db = DBStats.getInstance(); // can free them from being a singleton but meh...
        var model = ModelsFactory.getInstance().getStatsModel();

        model.setMalesCount(db.getMalesCount());
        model.setFemalesCount(db.getFemalesCount());
    }
}
