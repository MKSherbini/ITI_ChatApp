package iti.jets.gfive.db;

import java.sql.ResultSet;

public interface Dao<T> {

    ResultSet selectFromDB(String sql);
    int insertToDB(String sql);
    int updateRecordToDB(String sql);
    int deleteFromDB(String sql);
}
