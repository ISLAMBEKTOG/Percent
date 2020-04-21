package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandlerSongs extends Configs {
    static Connection dbConnection;

    public static Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:oracle:thin:@" + dbHost + ":"
                + dbPort + ":" + dbName;

        Class.forName("oracle.jdbc.driver.OracleDriver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void addSong(Song song) throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();

        String insert = "INSERT INTO SYSTEM.SONGS (TITLE, SONG_YEAR, POPULAR, STYLE_ID, SINGER_ID, FILE_PATH) " +
                "VALUES('" + song.getTitle() + "', " + song.getYear() + ", " + song.getPopular() + ", " + song.getStyle_id() + ", " + song.getSinger_id() + ", '" + song.getPath() + "')";

        st.executeUpdate(insert);
        st.close();
    }

    public void updateSinger(Song song, int id) throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();

        String update = "UPDATE SYSTEM.SONGS " +
                "SET TITLE = '" + song.getTitle() + "', SONG_YEAR = " + song.getYear() + ", POPULAR = " + song.getPopular() + ", STYLE_ID = " + song.getStyle_id() + ", SINGER_ID = " + song.getSinger_id() + ", FILE_PATH = '" + song.getPath() + "' WHERE SONG_ID = " + id;

        st.executeUpdate(update);
        st.close();
    }

    public void deleteSinger(Integer id_delete) throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();

        String delete = "delete from " + ConstSongs.SONG_TABLE + " where " + ConstSongs.SONG_ID + "=" + id_delete;

        st.executeUpdate(delete);
        st.close();
    }
}
