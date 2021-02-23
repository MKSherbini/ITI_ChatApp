package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.ui.helpers.db.DBStats;

public class StatsManager {
    private static final StatsManager instance = new StatsManager();

    private StatsManager() {

    }

    public static StatsManager getInstance() {
        return instance;
    }

    // wanted to separate each update but meh... bardo
    public void updateStats() {
        var db = DBStats.getInstance(); // can free them from being a singleton but meh...
        var model = ModelsFactory.getInstance().getStatsModel();

        // gender
        model.setMalesCount(db.getMalesCount());
        model.setFemalesCount(db.getFemalesCount());

        // country
        var countryMap = db.selectCountryStats();
        var propMap = model.getCountryPropertiesMap();

        countryMap.forEach((s, integer) -> {
            if (propMap.containsKey(s)) {
                propMap.get(s).set(integer);
            }
        });

        // connection
        var connectedMap = db.selectConnectionStats();
        var connectedPropMap = model.getConnectionPropertiesMap();
        System.out.println(connectedMap.entrySet());
        System.out.println(connectedPropMap.entrySet());
        connectedPropMap.get("Online").set(connectedMap.getOrDefault(true, 0));
        connectedPropMap.get("Offline").set(connectedMap.getOrDefault(false, 0));
        System.out.println(connectedPropMap.entrySet());

//        connectedMap.forEach((online, count) -> {
//            if (online) {
//                connectedPropMap.get("Online").set(count);
//            } else {
//                connectedPropMap.get("Offline").set(count);
//            }
//        });
    }
}
