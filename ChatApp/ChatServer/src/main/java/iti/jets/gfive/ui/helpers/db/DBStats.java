package iti.jets.gfive.ui.helpers.db;

import iti.jets.gfive.db.DataSourceFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Integer> selectCountryStats() {
        var ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        Map<String, Integer> countryStats = new HashMap<>();
        try {
            con = ds.getConnection();
            String selectQuery = "select coalesce(country,'Unspecified'), COUNT(*) from chatapp.user_data group by country;";
            preparedStatement = con.prepareStatement(selectQuery);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                countryStats.put(rs.getString(1), rs.getInt(2));
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
        return countryStats;
    }

    public Map<Boolean, Integer> selectConnectionStats() {
        var ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        Map<Boolean, Integer> countryStats = new HashMap<>();
        try {
            con = ds.getConnection();
            String selectQuery = "select on_line, COUNT(*)from chatapp.user_data group by on_line;";
            preparedStatement = con.prepareStatement(selectQuery);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                countryStats.put(rs.getBoolean(1), rs.getInt(2));
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
        return countryStats;
    }

    public int selectUsersCount() {
        var ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        int userCount = 0;
        try {
            con = ds.getConnection();
            String selectQuery = "select COUNT(*) from chatapp.user_data";
            preparedStatement = con.prepareStatement(selectQuery);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userCount = rs.getInt(1);
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
        return userCount;
    }


}
