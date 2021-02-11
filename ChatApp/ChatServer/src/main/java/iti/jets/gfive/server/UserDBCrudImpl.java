package iti.jets.gfive.server;

import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.db.DataSourceFactory;

import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

public class UserDBCrudImpl extends UnicastRemoteObject implements UserDBCrudInter {
    DataSource ds;

    public UserDBCrudImpl() throws RemoteException {}

    @Override
    public ArrayList<UserDto> selectFromDB(String sql) throws RemoteException {
        return null;
    }

    @Override
    public int insertUserRecord(UserDto user) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String insertQuery = "insert into user_data\n" +
                    "(phone_number, user_name, user_password, gender, date_birth)\n" +
                    "values (?, ?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getPhoneNumber());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setDate(5, user.getBirthDate());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        if(con != null && preparedStatement != null){
            try {
                preparedStatement.close(); con.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return rowsAffected;
    }

    //todo tell salma about the stmt that is always null and should be replaced with preparedStatement
    @Override
    public int updateUserRecord(UserDto user) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String insertQuery = "update user_data \n" +
                    "set user_name = ?, email = ?, user_password = ?, gender = ?, country = ?, date_birth = ?, bio = ?, \n" +
                    " WHERE phone_number = ?";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getCountry());
            preparedStatement.setDate(6, user.getBirthDate());
            preparedStatement.setString(7, user.getBio());
            preparedStatement.setString(8, user.getPhoneNumber());
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

    @Override
    public int deleteUser(UserDto user) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String insertQuery = "delete from user_data \n" +
                    " WHERE phone_number = ?";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getPhoneNumber());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(con != null && preparedStatement != null){
            try {
                preparedStatement.close(); con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }

    public boolean checkUserId(String userId){
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        boolean registered = false;
        try {
            con = ds.getConnection();
            String selectQuery = "select * from user_data \n" +
                    " WHERE phone_number = ?";
            preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setString(1, userId);
            rs = preparedStatement.executeQuery();
            if(rs.next() != false){
                System.out.println("rs.next: " + rs.next());
                registered = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(con != null && preparedStatement != null){
            try {
                preparedStatement.close(); con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return registered;
    }
}
