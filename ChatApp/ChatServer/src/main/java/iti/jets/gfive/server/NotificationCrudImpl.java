package iti.jets.gfive.server;

import iti.jets.gfive.common.interfaces.NotificationCrudInter;
import iti.jets.gfive.common.interfaces.NotificationsLabelInter;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.db.DataSourceFactory;

import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

public class NotificationCrudImpl extends UnicastRemoteObject implements NotificationCrudInter {
    DataSource ds;
    public NotificationCrudImpl() throws RemoteException {}
    @Override
    public int insertNotification(String content, String senderId, Date date, boolean completed, String receiverId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int rowsAffected = 0;
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
            rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int notificationId = rs.getInt(1);
                String query = "insert into notification_receivers\n" +
                        "(notification_id, receiver)\n" +
                        "values (?, ?)";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, notificationId);
                preparedStatement.setString(2, receiverId);
                rowsAffected += preparedStatement.executeUpdate();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        if(con != null && preparedStatement != null && rs != null){
            try {
                rs.close(); preparedStatement.close(); con.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return rowsAffected;
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
                    "where r.receiver = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, userId);
            rs = preparedStatement.executeQuery();
            try{
                while(rs.next()){
                    NotificationDto notification = new NotificationDto(
                            Integer.parseInt(rs.getString("notification_id")),
                            rs.getString("content"),
                            rs.getString("sender"),
                            rs.getDate("notification_date"),
                            rs.getBoolean("completed"),
                            rs.getString("receiver"));
                    notificationList.add(notification);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
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
        return notificationList;
    }

    @Override
    public void sendNotification(String userId) throws RemoteException {
        //todo check if the user is online in the pool
        ClientConnectionImpl.clientsPool.forEach(connectedClient -> {
            if(connectedClient.getClient().getPhoneNumber().equals(userId)){
                try {
                    connectedClient.getReceiveNotif().receive(null);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
//        NotificationsLabelInter
//                n = NotificationsLabel.getInstance();
//        System.out.println(n + " nnn");
//        n.increaseNotificationsNumber();
    }


}
