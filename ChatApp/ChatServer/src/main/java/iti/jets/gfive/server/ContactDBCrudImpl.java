package iti.jets.gfive.server;

import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.db.DataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

public class ContactDBCrudImpl extends UnicastRemoteObject implements ContactDBCrudInter {
    DataSource ds;

    public ContactDBCrudImpl() throws RemoteException {
    }

    @Override
    public int insertContactRecord(String contactId, String currentUserId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String insertQuery = "insert into contacts\n" +
                    "(user_id, contact_id)\n" +
                    "values (?, ?)";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, currentUserId);
            preparedStatement.setString(2, contactId);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        if (con != null && preparedStatement != null) {
            try {
                preparedStatement.close();
                con.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return rowsAffected;
    }

    public ArrayList<UserDto> getContactsList(String userId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        ArrayList<UserDto> contactsList = new ArrayList<>();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        try {
            con = ds.getConnection();
            String query = "select u.* from contacts c \n" +
                    "inner join user_data u \n" +
                    "on u.phone_number = c.contact_id\n" +
                    "where c.contact_id in(select contact_id from contacts where user_id = ?);";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, userId);
            rs = preparedStatement.executeQuery();
            try {
                while (rs.next()) {
                    //todo use salma's UserDto's overloaded constructor instead of making one and solve conflicts
                    //todo set the profile picture
                    UserDto contact = new UserDto();
                    contact.setPhoneNumber(rs.getString("phone_number"));
                    contact.setUsername(rs.getString("user_name"));
                    contact.setEmail(rs.getString("email"));
                    contact.setPassword(rs.getString("user_password"));
                    contact.setGender(rs.getString("gender"));
                    contact.setCountry(rs.getString("country"));
                    contact.setBirthDate(rs.getDate("date_birth"));
                    contact.setBio(rs.getString("bio"));
                    contact.setStatus(rs.getString("user_status"));
                    contact.setImage(UserDBCrudImpl.deserializeFromString(rs.getBytes("picture")));
                    contactsList.add(contact);
                }
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
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
        return contactsList;
    }
}
