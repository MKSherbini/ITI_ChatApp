package iti.jets.gfive.server;

import iti.jets.gfive.common.interfaces.MessageDBInter;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.db.DataSourceFactory;

import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDBImpl extends UnicastRemoteObject implements  MessageDBInter {
    DataSource ds;
    public MessageDBImpl() throws RemoteException
    {
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
                    " WHERE sender_id = ? and receiver_id =?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, senderNumber);
            preparedStatement.setString(2, receiverNumber);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messageDto = new MessageDto();
                messageDto.setContent(resultSet.getString("content"));
                messageDto.setReceiverNumber(resultSet.getString("receiver_id"));
                messageDto.setSenderNumber(resultSet.getString("sender_id"));
                messageDto.setMessageDate(resultSet.getDate("message_date"));
                messageDto.setState(resultSet.getString("state"));
                messageDto.setMessageName(resultSet.getString("message_name"));
                messageList.add(messageDto);
            }
            System.out.println("Number of list "+messageList.size());
        } catch (SQLException | NullPointerException throwables) {
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
        return messageList;
    }

    @Override
    public int insertMessage(MessageDto messageDto) throws RemoteException {

        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String insertQuery = "insert into message\n" +
                    "(sender_id, receiver_id, state , content, message_date)\n" +
                    "values (?, ?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, messageDto.getSenderNumber());
            preparedStatement.setString(2, messageDto.getReceiverNumber());
            preparedStatement.setString(3, messageDto.getState());
            preparedStatement.setString(4, messageDto.getContent());
            preparedStatement.setDate(5, messageDto.getMessageDate());
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

    @Override
    public int updateMessageState(String receiverNumber, String senderNumber) throws RemoteException{
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
        }
        if (con != null && preparedStatement != null) {
            try {
                preparedStatement.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }
}
