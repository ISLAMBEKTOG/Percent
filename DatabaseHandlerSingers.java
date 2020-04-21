package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandlerSingers extends Configs {
    static Connection dbConnection;

    public static Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:oracle:thin:@" + dbHost + ":"
                + dbPort + ":" + dbName;

        Class.forName("oracle.jdbc.driver.OracleDriver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void addSinger(Singer singer) throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();

        String insert = "INSERT INTO SYSTEM.SINGERS (SINGER_NAME, AGE, GENDER, STYLE_ID, COUNTRY_ID, IMAGE_PATH) " +
                "VALUES ('" + singer.getName() + "'," + singer.getAge() + ",'" + singer.getGender() + "'," + singer.getStyle_id() + "," + singer.getCountry_id() + ",'" + singer.getImagePath() + "')";

        st.executeUpdate(insert);
        st.close();
    }

    public void deleteSinger(Integer id_delete) throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();

        String delete = "delete from " + ConstSingers.SINGER_TABLE + " where " + ConstSingers.SINGER_ID + "=" + id_delete;

        st.executeUpdate(delete);
        st.close();
    }

    public void updateSinger(Singer singer, int id) throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();

        String update = "UPDATE SYSTEM.SINGERS " +
                "SET SINGER_NAME = '" + singer.getName() + "', AGE = " + singer.getAge() + ", GENDER = '" + singer.getGender() + "', STYLE_ID = " + singer.getStyle_id() + ", COUNTRY_ID = " + singer.getCountry_id() + ", IMAGE_PATH = '" + singer.getImagePath() + "' WHERE SINGER_ID = " + id;

        st.executeUpdate(update);
        st.close();
    }
}