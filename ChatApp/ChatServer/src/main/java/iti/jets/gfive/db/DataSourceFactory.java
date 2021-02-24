package iti.jets.gfive.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataSourceFactory {
    public static DataSource getMySQLDataSource() {
        Properties props = new Properties();
        InputStream fis;
        MysqlDataSource mysqlDS = null;
        try {
            //fis = new FileInputStream("db.properties");
            fis = DataSourceFactory.class.getResourceAsStream("/iti/jets/gfive/db.properties");
            props.load(fis);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
            System.out.println("A Data Source object is created: " + mysqlDS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }
}
