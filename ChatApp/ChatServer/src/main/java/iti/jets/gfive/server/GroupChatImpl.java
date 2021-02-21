package iti.jets.gfive.server;

import iti.jets.gfive.common.interfaces.GroupChatInter;
import iti.jets.gfive.common.models.GroupDto;
import iti.jets.gfive.common.models.NotifDBRecord;
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
        }
        if (con != null && preparedStatement != null) {
            try {
                preparedStatement.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
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
        }
        if (con != null && preparedStatement != null && rs != null) {
            try {
                rs.close();
                preparedStatement.close();
                con.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        return groupid;
    }
}