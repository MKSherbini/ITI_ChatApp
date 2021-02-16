package iti.jets.gfive.server;

import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.db.DataSourceFactory;
import javafx.scene.image.Image;

import javax.sql.DataSource;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserDBCrudImpl extends UnicastRemoteObject implements UserDBCrudInter {
    DataSource ds;


    public UserDBCrudImpl() throws RemoteException {
    }

    @Override
    public UserDto selectFromDB(String phoneNumber) throws RemoteException {
        UserDto user = new UserDto();
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String sql = "select * from user_data \n" +
                    " WHERE phone_number = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, phoneNumber);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new UserDto();
                user.setUsername(resultSet.getString("user_name"));
                user.setEmail(resultSet.getString("email"));
                user.setGender(resultSet.getString("gender"));
                user.setCountry(resultSet.getString("country"));
                user.setBirthDate(resultSet.getDate("date_birth"));
                user.setBio(resultSet.getString("bio"));
                user.setStatus(resultSet.getString("user_status"));
                byte[] bytes = {};
                bytes = resultSet.getBytes("picture");
                Image image = deserializeFromString(bytes);
                user.setImage(image);

                return user;
            }
        } catch (SQLException | NullPointerException | IOException throwables) {
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
        return null;
    }

    @Override
    public UserDto selectFromDB(String phoneNumber, String password) throws RemoteException {
        UserDto user = new UserDto();
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String sql = "select * from user_data \n" +
                    " WHERE phone_number = ? and user_password =?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new UserDto();
                user.setUsername(resultSet.getString("user_name"));
                user.setEmail(resultSet.getString("email"));
                user.setGender(resultSet.getString("gender"));
                user.setCountry(resultSet.getString("country"));
                user.setBirthDate(resultSet.getDate("date_birth"));
                user.setBio(resultSet.getString("bio"));
                user.setStatus(resultSet.getString("user_status"));
                byte[] bytes = {};
//                System.out.println("befor condition");
//                System.out.println(resultSet.getBytes("picture"));
                bytes = resultSet.getBytes("picture");
                Image image = deserializeFromString(bytes);
                user.setImage(image);

                return user;
            }
        } catch (SQLException | NullPointerException | IOException throwables) {
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
                    "(phone_number, user_name, user_password, gender, date_birth, picture)\n" +
                    "values (?, ?, ?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getPhoneNumber());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setDate(5, user.getBirthDate());
//            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\A\\Desktop\\JavaProject_Team5ChatAPP\\ITI_ChatApp\\ChatApp\\ChatClient\\src\\main\\resources\\iti\\jets\\gfive\\images\\personal.jpg");
//            Image image = new Image(fileInputStream);
            if (user.getImage() == null) System.out.println("F*Image is still F*Null");
            byte[] bytes = serializeToString(user.getImage());
            preparedStatement.setBytes(6, bytes);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException | IOException throwable) {
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

    //todo tell salma about the stmt that is always null and should be replaced with preparedStatement
    @Override
    public int updateUserPhoto(UserDto user) throws RemoteException {
        System.out.println("inside update photo");

        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String insertQuery = "update user_data set picture = ? WHERE phone_number = ?";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            //null
            System.out.println("imaaage " + user.getImage());
            byte[] bytes = serializeToString(user.getImage());
            System.out.println("inside update photo22");
            preparedStatement.setBytes(1, bytes);
            preparedStatement.setString(2, user.getPhoneNumber());
            rowsAffected = preparedStatement.executeUpdate();
            System.out.println("rowaffected " + rowsAffected);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        if (con != null && stmt != null) {
            try {
                stmt.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }

    @Override
    public int updateUserStatus(UserDto user) throws RemoteException {
        System.out.println("inside the updateUserStatus ");

        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        Statement stmt = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           String insertQuery = "update user_data set user_status = ? WHERE phone_number = ?";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            //null
            System.out.println("update the status of  " +user.getStatus());
             preparedStatement.setString(1,user.getStatus());
            preparedStatement.setString(2, user.getPhoneNumber());
            rowsAffected = preparedStatement.executeUpdate();
            System.out.println("rowaffected "+rowsAffected);
        } catch (SQLException  throwables) {
            throwables.printStackTrace();
        }
        if (con != null && stmt != null) {
            try {
                stmt.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }

    @Override
    public int updateUserRecord(UserDto user) throws RemoteException {
        System.out.println("inside update");
        ds = DataSourceFactory.getMySQLDataSource();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            con = ds.getConnection();
            String insertQuery = "update user_data set user_name = ?, email = ?, user_password = ?, gender = ?, country = ?, date_birth = ?, bio = ?  WHERE phone_number = ?";
            //"update users set num_points = ? where first_name = ?";
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getCountry());
            preparedStatement.setDate(6, user.getBirthDate());
            preparedStatement.setString(7, user.getBio());
            preparedStatement.setString(8, user.getPhoneNumber());
            System.out.println("inside 2");
            rowsAffected = preparedStatement.executeUpdate();
            System.out.println("towsaffected" + rowsAffected);
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

    public boolean checkUserId(String userId) {
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
            if (rs.next() != false) {
                System.out.println("rs.next: " + rs.next());
                registered = true;
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
        return registered;
    }


    public static Image deserializeFromString(byte[] s) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(s));
        var img = SwingFXUtils.toFXImage(ImageIO.read(ois), null);
        return img;
    }

    public static byte[] serializeToString(Image o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        ImageIO.write(SwingFXUtils.fromFXImage(o, null), "png", oos);
        var arr = baos.toByteArray();
        baos.flush();
        baos.close();
//        ImageIO.write(SwingFXUtils.fromFXImage(o, null), "png", new File("D:/serialized.png")); 77kb
        return arr;
    }


}
