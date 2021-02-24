package iti.jets.gfive.server;

import iti.jets.gfive.common.interfaces.MessageDBInter;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.db.DataSourceFactory;

import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDBImpl extends UnicastRemoteObject implements MessageDBInter {
    DataSource ds;

    public MessageDBImpl() throws RemoteException {
    }

    @Override
    public List<MessageDto> selectAllMessages(String receiverNumber, String senderNumber) throws RemoteException {
        List<MessageDto> messageList = new ArrayList<>();
        MessageDto messageDto = new MessageDto();

        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String sql = "select * from message \n" +
                    " WHERE (sender_id = ? and receiver_id =?) or (sender_id=? and receiver_id=?) order by message_id";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, senderNumber);
            preparedStatement.setString(2, receiverNumber);
            preparedStatement.setString(3, receiverNumber);
            preparedStatement.setString(4, senderNumber);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messageDto = new MessageDto();
                messageDto.setId(resultSet.getInt("message_id"));
                messageDto.setContent(resultSet.getString("content"));
                messageDto.setReceiverNumber(resultSet.getString("receiver_id"));
                messageDto.setSenderNumber(resultSet.getString("sender_id"));
                messageDto.setMessageDate(resultSet.getDate("message_date"));
                messageDto.setState(resultSet.getString("state"));
                messageDto.setMessageName(resultSet.getString("message_name"));
                messageList.add(messageDto);
            }
            System.out.println("Number of list " + messageList.size());
        } catch (SQLException | NullPointerException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return messageList;
    }

    @Override
    public int insertMessage(MessageDto messageDto) throws RemoteException {

        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        int messageId = -1;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            String insertQuery = "insert into message\n" +
                    "(message_name, sender_id, receiver_id, state , content, message_date)\n" +
                    "values (?, ?, ?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, messageDto.getMessageName());
            preparedStatement.setString(2, messageDto.getSenderNumber());
            preparedStatement.setString(3, messageDto.getReceiverNumber());
            preparedStatement.setString(4, messageDto.getState());
            if (messageDto.getContent() == null) {
                preparedStatement.setBytes(5, messageDto.getFileContent());
            } else {
                preparedStatement.setString(5, messageDto.getContent());
            }
            preparedStatement.setDate(6, messageDto.getMessageDate());
            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0)
                return messageId;
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                messageId = rs.getInt(1);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
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
        }
        return messageId;
    }

    @Override
    public int updateMessageState(String receiverNumber, String senderNumber) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String insertQuery = "update message set state = ? WHERE sender_id = ? and receiver_id =? ";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, senderNumber);
            preparedStatement.setString(2, receiverNumber);
            rowsAffected = preparedStatement.executeUpdate();
            System.out.println("rowsaffected" + rowsAffected);
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

    @Override
    public byte[] getFile(int recordId) {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        byte[] fileData = null;
        try {
            con = ds.getConnection();
            String query = "select content from message \n" +
                    "where message_id = ?;";
            preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, recordId);
            rs = preparedStatement.executeQuery();
            try {
                while (rs.next()) {
                    fileData = rs.getBytes("content");
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
        return fileData;
    }
}
