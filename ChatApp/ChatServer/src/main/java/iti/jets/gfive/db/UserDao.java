package iti.jets.gfive.db;

import iti.jets.gfive.ui.models.CurrentUserModel;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao implements Dao<CurrentUserModel> {

    DataSource ds;
    @Override
    public ResultSet selectFromDB(String sql) {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(con != null && stmt!= null){
            try {
                stmt.close(); con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rs;
    }

    @Override
    public int insertToDB(String sql) {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rowsAffected = stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(con != null && stmt!= null){
            try {
                stmt.close(); con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }

    @Override
    public int updateRecordToDB(String sql) {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rowsAffected = stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(con != null && stmt!= null){
            try {
                stmt.close(); con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }

    @Override
    public int deleteFromDB(String sql) {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rowsAffected = stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(con != null && stmt!= null){
            try {
                stmt.close(); con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }

    public int insertUserRecord(String phoneNumber, String username, String password, String gender, Date birthDate){
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String insertQuery = "insert into user_data\n" +
                    "(phone_number, user_name, user_password, gender, date_birth)\n" +
                    "values (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, gender);
            preparedStatement.setDate(5, birthDate);
            //preparedStatement.setDate(5, birthdate);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(con != null && stmt!= null){
            try {
                stmt.close(); con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }
}
