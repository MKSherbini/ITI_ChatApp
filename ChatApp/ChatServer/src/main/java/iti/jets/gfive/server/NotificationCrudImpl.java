package iti.jets.gfive.server;

import iti.jets.gfive.common.interfaces.NotificationCrudInter;
import iti.jets.gfive.db.DataSourceFactory;

import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class NotificationCrudImpl extends UnicastRemoteObject implements NotificationCrudInter {
    DataSource ds;
    public NotificationCrudImpl() throws RemoteException {}
    @Override
    public int insertNotification(String content, String senderId, Date date, boolean seen) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String insertQuery = "insert into notifications\n" +
                    "(content, sender, notifaction_date, seen)\n" +
                    "values (?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, senderId);
            preparedStatement.setDate(3, date);
            preparedStatement.setBoolean(4, seen);
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
}
