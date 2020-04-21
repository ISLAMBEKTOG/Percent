package sample;

import java.sql.*;

public class DatabaseHandler extends Configs {
    static Connection dbConnection;
    private static String usernameForTitle;
    private static String emailForSettings;

    public static Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:oracle:thin:@" + dbHost + ":"
                + dbPort + ":" + dbName;

        Class.forName("oracle.jdbc.driver.OracleDriver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void addUser(User user) throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();
        String new_insert = "insert into SYSTEM.USERS (FULL_NAME, PHONE_NUMBER, EMAIL_ADDRESS, USER_PASSWORD, GENDER, AGE, COUNTRY_ID)\n" +
                " VALUES('" + user.getName() + "','" + user.getPhone() + "','" + user.getEmail() + "','" + user.getPassword() + "','" + user.getGender() + "'," + user.getAge() + "," + user.getCountry_id() + ")";

        st.executeUpdate(new_insert);
        st.close();
    }

    public ResultSet getUser(User user) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;

        String select = "select * from " + Const.USER_TABLE + " where " + Const.USER_EMAIL + "=? and " + Const.USER_PASSWORD + "=?";

        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, user.getEmail());
        prSt.setString(2, user.getPassword());

        /*Email address for settings*/
        emailForSettings = user.getEmail();

        /*Method for finding username*/
        getUserName(user.getEmail());

        resultSet = prSt.executeQuery();

        return resultSet;
    }

    public void updateUser(User user) throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();

        String update = "update SYSTEM.USERS set full_name = '" + user.getName() + "', phone_number = '" + user.getPhone() + "', user_password = '" + user.getPassword() + "', gender = '" + user.getGender() + "', age = " + user.getAge() + ", country_id = " + user.getCountry_id() + " where email_address = '" + emailForSettings + "'";

        st.executeUpdate(update);
        st.close();
    }

    public void deleteUser(Integer id_delete) throws SQLException, ClassNotFoundException {
        Statement st = getDbConnection().createStatement();

        String delete = "delete from " + Const.USER_TABLE + " where USER_ID = " + id_delete;

        st.executeUpdate(delete);
        st.close();
    }

    public void getUserName(String emailUser) throws SQLException {
        String selectUsername = "select * from " + Const.USER_TABLE + " where " + Const.USER_EMAIL + "= '" + emailUser + "'";

        PreparedStatement stPr = dbConnection.prepareStatement(selectUsername);
        ResultSet resultSet = stPr.executeQuery();

        String username = null;

        while (resultSet.next()) {
            username = resultSet.getString("FULL_NAME");
        }
        usernameForTitle = username;
    }

    public static String getUsernameForTitle() {
        return usernameForTitle;
    }

    public static String getEmailForSettings() {
        return emailForSettings;
    }
}

