package iti.jets.gfive.server;

import iti.jets.gfive.common.interfaces.NotificationCrudInter;
import iti.jets.gfive.common.models.NotifDBRecord;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.db.DataSourceFactory;

import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

public class NotificationCrudImpl extends UnicastRemoteObject implements NotificationCrudInter {
    DataSource ds;

    public NotificationCrudImpl() throws RemoteException {
    }

    @Override
    public NotifDBRecord insertNotification(String content, String senderId, Date date, boolean completed, String receiverId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        NotifDBRecord notifObj = new NotifDBRecord(-1, -1);
        int rowsAffected = 0;
        int notificationId = -1;
        try {
            con = ds.getConnection();
            String insertQuery = "insert into notifications\n" +
                    "(content, sender, notifaction_date, completed)\n" +
                    "values (?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, senderId);
            preparedStatement.setDate(3, date);
            preparedStatement.setBoolean(4, completed);
            rowsAffected = preparedStatement.executeUpdate();
            //todo if i return like this does it means that the connections are not closed? should i put it in finally!
            if (rowsAffected == 0)
                return notifObj;
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                notificationId = rs.getInt(1);
                String query = "insert into notification_receivers\n" +
                        "(notification_id, receiver)\n" +
                        "values (?, ?)";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, notificationId);
                preparedStatement.setString(2, receiverId);
                rowsAffected += preparedStatement.executeUpdate();
                if (rowsAffected != 2)
                    return notifObj;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
//            if (con != null && preparedStatement != null) {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (con != null)
                    con.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
//            }
        }
        notifObj.setNotifId(notificationId);
        notifObj.setRowsAffected(rowsAffected);
        return notifObj;
    }

    @Override
    public ArrayList<NotificationDto> getNotificationList(String userId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        ArrayList<NotificationDto> notificationList = new ArrayList<>();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        try {
            con = ds.getConnection();
            String query = "select * from notifications n\n" +
                    "join notification_receivers r \n" +
                    "on n.notification_id = r.notification_id\n" +
                    "where r.receiver = ? and completed = 0;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, userId);
            rs = preparedStatement.executeQuery();
            try {
                while (rs.next()) {
                    NotificationDto notification = new NotificationDto(
                            Integer.parseInt(rs.getString("notification_id")),
                            rs.getString("content"),
                            rs.getString("sender"),
                            rs.getDate("notifaction_date"),
                            rs.getBoolean("completed"),
                            rs.getString("receiver"));
                    notificationList.add(notification);
                }
            } catch (SQLException throwables) {
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
        return notificationList;
    }

    @Override
    public void sendNotification(NotificationDto notif) throws RemoteException {
        ClientConnectionImpl.clientsPool.forEach(connectedClient -> {
            if (connectedClient.getClient().getPhoneNumber().equals(notif.getReceiverId())) {
                try {
                    connectedClient.getReceiveNotif().receive(notif);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean pendingNotification(String senderId, String receiverId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        boolean notificationExists = false;
        try {
            con = ds.getConnection();
            String query = "select * from notifications n, notification_receivers nr \n" +
                    "where n.notification_id = nr.notification_id\n" +
                    "and n.sender= ? and nr.receiver= ? and n.completed = 0;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, senderId);
            preparedStatement.setString(2, receiverId);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                notificationExists = true;
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
        return notificationExists;
    }

    @Override
    public boolean reverseNotification(String senderId, String receiverId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        boolean notificationReverseExists = false;
        try {
            con = ds.getConnection();
            String query = "select * from\n" +
                    "notifications n, notification_receivers nr\n" +
                    "where nr.receiver = ? and n.sender = ? and n.completed = 0;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, senderId);
            preparedStatement.setString(2, receiverId);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                notificationReverseExists = true;
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
        return notificationReverseExists;
    }

    @Override
    public int updateNotificationStatus(int notifId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        ArrayList<NotificationDto> notificationList = new ArrayList<>();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String query = "update notifications set completed = 1 where notification_id = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, notifId);
            rowsAffected = preparedStatement.executeUpdate();
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
        return rowsAffected;
    }


}
