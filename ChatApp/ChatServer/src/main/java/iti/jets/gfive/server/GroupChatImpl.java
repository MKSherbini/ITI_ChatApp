package iti.jets.gfive.server;

import iti.jets.gfive.common.interfaces.GroupChatInter;
import iti.jets.gfive.common.models.GroupDto;
import iti.jets.gfive.common.models.GroupMessagesDto;
import iti.jets.gfive.common.models.NotifDBRecord;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.db.DataSourceFactory;

import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupChatImpl extends UnicastRemoteObject implements GroupChatInter {
    @Override
    public List<GroupDto> selectAllGroups(String number) throws RemoteException {
        List<GroupDto> groups = new ArrayList<>();
        GroupDto groupDto = new GroupDto();
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        try {
            con = ds.getConnection();
            String selectQuery = "select group_name ,gc.group_id\n" +
                    "from chatapp.groupchat gc join\n" +
                    "chatapp.groupchatmember gcm\n" +
                    "where gc.group_id = gcm.group_id and member_id = ?;";
            preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setString(1, number);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                groupDto =new GroupDto();
                groupDto.setGroupname(rs.getString("group_name"));
                groupDto.setId(rs.getInt("group_id"));
                 groups.add(groupDto);

            }
            System.out.println("number of groups equals " + groups.size());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (con != null && preparedStatement != null) {
                try {
                    preparedStatement.close();
                    con.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return groups;
    }

    public GroupChatImpl() throws RemoteException {
    }

    DataSource ds;

    @Override
    public int insert(String groupname, List<String> members) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int rowsAffected = 0;
        int groupid = -1;
        try {
            con = ds.getConnection();
            String insertQuery = "insert into groupchat\n" +
                    "(group_name)\n" +
                    "values (?)";
            preparedStatement = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, groupname);

            rowsAffected = preparedStatement.executeUpdate();
            //What shall i do here
            if (rowsAffected == 0)
                return 0;
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                groupid = rs.getInt(1);
            }
            System.out.println("member size--"+members.size());
            System.out.println(" groupid--"+groupid);
            for (int i = 0; i < members.size(); i++) {


                String query = "insert into groupchatmember\n" +
                        "(group_id, member_id)\n" +
                        "values (?, ?)";

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, groupid);
                preparedStatement.setString(2, members.get(i));
                rowsAffected += preparedStatement.executeUpdate();
                System.out.println("row affeected==" + rowsAffected);


            }
            System.out.println("row affeected==" + rowsAffected);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            if (con != null && preparedStatement != null && rs != null) {
                try {
                    rs.close();
                    preparedStatement.close();
                    con.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return groupid;
    }

    @Override
    public List<String> selectAllMemebers(String id) throws RemoteException {
        List<String> members  = new ArrayList<>();
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        try {
            con = ds.getConnection();
            String selectQuery = "select member_id\n" +
                    "from chatapp.groupchatmember\n" +
                    "where group_id = ?;";
            preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setInt(1, Integer.parseInt(id));
            rs = preparedStatement.executeQuery();
            while (rs.next()) {

                members.add(rs.getString("member_id"));

            }
            System.out.println("number of members "+members.size());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (con != null && preparedStatement != null) {
                try {
                    preparedStatement.close();
                    con.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return members;

    }

    @Override
    public List<GroupMessagesDto> selectAllMessages(String groupid) throws RemoteException {
        List<GroupMessagesDto> messages  = new ArrayList<>();
        GroupMessagesDto groupMessagesDto = new GroupMessagesDto();
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        try {
            con = ds.getConnection();
            String selectQuery = "select id, group_id ,message , sender_id, message_name\n" +
                    "from chatapp.groupmessages\n" +
                    "where group_id = ?;";
            preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setInt(1, Integer.parseInt(groupid));
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                groupMessagesDto=new GroupMessagesDto();
                groupMessagesDto.setId(String.valueOf(rs.getInt("group_id")));
                groupMessagesDto.setMessage(rs.getString("message"));
                groupMessagesDto.setSendernumber(rs.getString("sender_id"));
                groupMessagesDto.setMessage_name(rs.getString("message_name"));
                groupMessagesDto.setId(rs.getString("id"));
                messages.add(groupMessagesDto);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (con != null && preparedStatement != null) {
                try {
                    preparedStatement.close();
                    con.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return messages;
    }

    @Override
    public int saveAllMessages(GroupMessagesDto groupMessagesDto) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int rowsAffected = 0;
        int groupid = -1;
        try {
            con = ds.getConnection();
            String insertQuery = "insert into chatapp.groupmessages\n" +
                    "(group_id ,message ,sender_id, message_name)\n" +
                    "values (?,?,?,?)";
            preparedStatement = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, groupMessagesDto.getGroupId());
            if(groupMessagesDto.getMessage_name().equals("text")){
                preparedStatement.setString(2, groupMessagesDto.getMessage());
            } else {
                preparedStatement.setBytes(2, groupMessagesDto.getFileContent());
            }
            preparedStatement.setString(3, groupMessagesDto.getSendernumber());
            preparedStatement.setString(4, groupMessagesDto.getMessage_name());
            rowsAffected = preparedStatement.executeUpdate();
            rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                groupid = rs.getInt(1);
            }
            System.out.println("row affeected==" + rowsAffected);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            if (con != null && preparedStatement != null && rs != null) {
                try {
                    rs.close();
                    preparedStatement.close();
                    con.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return groupid;
    }

    @Override
    public String getUserName(String number) throws RemoteException {
         String name =null;
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        try {
            con = ds.getConnection();
            String selectQuery = "select user_name\n" +
                    "from chatapp.user_data\n" +
                    "where phone_number = ?;";
            preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setString(1, number);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
               name =  rs.getString("user_name");

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (con != null && preparedStatement != null) {
                try {
                    preparedStatement.close();
                    con.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return name;
    }

    @Override
    public byte[] getFileForGroup(int recordId) throws RemoteException {
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs;
        byte [] fileData = null;
        try {
            con = ds.getConnection();
            String query = "select message from groupmessages \n" +
                    "where id = ?;";
            preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, recordId);
            rs = preparedStatement.executeQuery();
            try{
                while(rs.next()){
                    fileData = rs.getBytes("message");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if(con != null && preparedStatement != null){
                try {
                    preparedStatement.close(); con.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return fileData;
    }
}
