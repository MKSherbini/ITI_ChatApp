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
            if (rowsAffected == 0) return rowsAffected;
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, contactId);
            preparedStatement.setString(2, currentUserId);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (con != null)
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
            String query = "select u.* from contacts c , user_data u\n" +
                    "where u.phone_number = c.contact_id and user_id = ?";
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
                    if (rs.getBoolean("on_line")) {
                        contact.setStatus(rs.getString("user_status"));
                    } else {
                        contact.setStatus("offline");
                    }
                    contact.setImage(UserDBCrudImpl.deserializeFromString(rs.getBytes("picture")));
                    contactsList.add(contact);
                }
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (con != null)
                    con.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return contactsList;
    }

    @Override
    public boolean checkActiveChatBot(String contactId, String currentUserId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        boolean activeChatBot = true;
        try {
            con = ds.getConnection();
            String insertQuery = "select active_chat_bot from chatapp.contacts where user_id=? and contact_id =?;";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, currentUserId);
            preparedStatement.setString(2, contactId);
            var rs = preparedStatement.executeQuery();

            if (rs.next())
                activeChatBot = rs.getBoolean(1);

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (con != null)
                    con.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return activeChatBot;
    }

    @Override
    public void updateActiveChatBot(String contactId, String currentUserId, boolean chatBotState) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = ds.getConnection();
            String insertQuery = "update chatapp.contacts set active_chat_bot = ? where user_id=? and contact_id =?;";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setBoolean(1, chatBotState);
            preparedStatement.setString(2, currentUserId);
            preparedStatement.setString(3, contactId);
            preparedStatement.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (con != null)
                    con.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public void updateUserContacts(String userId, UserDto contactInfo) throws RemoteException {
        ClientConnectionImpl.clientsPool.forEach(connectedClient -> {
            if (connectedClient.getClient().getPhoneNumber().equals(userId)) {
                try {
                    connectedClient.getReceiveNotif().updateContactsList(contactInfo);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public UserDto getContactInfo(String contactId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        UserDto contact = null;
        try {
            con = ds.getConnection();
            String query = "select * from user_data\n" +
                    "where phone_number = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, contactId);
            rs = preparedStatement.executeQuery();
            try {
                while (rs.next()) {
                    contact = new UserDto();
                    contact.setPhoneNumber(rs.getString("phone_number"));
                    contact.setUsername(rs.getString("user_name"));
                    contact.setEmail(rs.getString("email"));
                    contact.setGender(rs.getString("gender"));
                    contact.setCountry(rs.getString("country"));
                    contact.setBirthDate(rs.getDate("date_birth"));
                    contact.setBio(rs.getString("bio"));
                    contact.setStatus(rs.getString("user_status"));
                    contact.setImage(UserDBCrudImpl.deserializeFromString(rs.getBytes("picture")));
                }
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (con != null)
                    con.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return contact;
    }

}
