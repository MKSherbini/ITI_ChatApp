package iti.jets.gfive.server;

import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.db.DataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDBCrudImpl extends UnicastRemoteObject implements UserDBCrudInter {
    DataSource ds;


    public UserDBCrudImpl() throws RemoteException {
    }

    @Override
    public UserDto selectFromDB(String phoneNumber , String password) throws RemoteException {
        UserDto user = new UserDto();
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String sql = "select * from user_data \n" +
                    " WHERE phone_number = ? and user_password =?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new UserDto();
                user.setUsername(resultSet.getString("user_name"));
                user.setEmail(resultSet.getString("email"));
                user.setGender(resultSet.getString("gender"));
                user.setCountry(resultSet.getString("country"));
                user.setBirthDate(resultSet.getDate("date_birth"));
                user.setBio(resultSet.getString("bio"));
                user.setStatus(resultSet.getString("user_status"));
                Blob b =resultSet.getBlob("picture");
                //user.setImage(resultSet.getBytes("picture"));


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (con != null && stmt != null && resultSet != null) {
            try {
                stmt.close();
                con.close();
                resultSet.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return user;
    }

    @Override
    public int insertUserRecord(UserDto user) throws RemoteException {
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
            preparedStatement.setString(1, user.getPhoneNumber());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setDate(5, user.getBirthDate());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        if (con != null && stmt != null) {
            try {
                stmt.close();
                con.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return rowsAffected;
    }

    @Override
    public int updateUserPhoto(UserDto user) throws RemoteException {

        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String insertQuery = "update user_data \n" +
                    "set picture = ?, \n" +
                    " WHERE phone_number = ?";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            // preparedStatement.setBlob(1, new FileInputStream(user.getImage()));
            preparedStatement.setString(2, user.getPhoneNumber());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (con != null && stmt != null) {
            try {
                stmt.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }

    @Override
    public int updateUserRecord(UserDto user) throws RemoteException {
        System.out.println("inside update");
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String insertQuery = "update user_data set user_name = ?, email = ?, user_password = ?, gender = ?, country = ?, date_birth = ?, bio = ?  WHERE phone_number = ?";
            //"update users set num_points = ? where first_name = ?";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getCountry());
            preparedStatement.setDate(6, user.getBirthDate());
            preparedStatement.setString(7, user.getBio());
            preparedStatement.setString(8, user.getPhoneNumber());
            System.out.println("inside 2");
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (con != null && stmt != null) {
            try {
                stmt.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }

    @Override
    public int deleteUser(UserDto user) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String insertQuery = "delete from user_data \n" +
                    " WHERE phone_number = ?";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getPhoneNumber());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (con != null && stmt != null) {
            try {
                stmt.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }
}
