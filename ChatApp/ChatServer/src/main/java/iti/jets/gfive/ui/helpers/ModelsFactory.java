package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.ui.models.CurrentUserModel;
import iti.jets.gfive.ui.models.StatsModel;

public class ModelsFactory {

    private static final ModelsFactory instance = new ModelsFactory();

    private CurrentUserModel currentUserModel;
    private StatsModel statsModel;

    private ModelsFactory() {
    }

    public static ModelsFactory getInstance() {
        return instance;
    }

    public CurrentUserModel getCurrentUserModel() {
        if (currentUserModel == null) {
            currentUserModel = new CurrentUserModel();
        }
        return currentUserModel;
    }

    public StatsModel getStatsModel() {
        if (statsModel == null) {
            statsModel = new StatsModel();
        }
        return statsModel;
    }

}
