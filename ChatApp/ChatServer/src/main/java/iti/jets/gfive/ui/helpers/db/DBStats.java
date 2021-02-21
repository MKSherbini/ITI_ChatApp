package iti.jets.gfive.ui.helpers.db;

import iti.jets.gfive.db.DataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBStats {
    private static final DBStats instance = new DBStats();

    private DBStats() {

    }

    public static DBStats getInstance() {
        return instance;
    }

    public int getMalesCount() {
        return selectCountByGender("male");
    }

    public int getFemalesCount() {
        return selectCountByGender("female");
    }

    private int selectCountByGender(String gender) {
        var ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        int genderCount = -1;
        try {
            con = ds.getConnection();
            String selectQuery = "SELECT count(*) FROM chatapp.user_data where gender = ?;";
            preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setString(1, gender);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                genderCount = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (con != null && preparedStatement != null) {
            try {
                preparedStatement.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return genderCount;
    }

}
