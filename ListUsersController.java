package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ListUsersController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField userDeleteTextField;

    @FXML
    private TableView<ModelTable> tableOfUsers;

    @FXML
    private TableColumn<ModelTable, Integer> col_id;

    @FXML
    private TableColumn<ModelTable, String> col_email;

    @FXML
    private Button userDeleteButton;

    @FXML
    private Button adminLogout;

    @FXML
    private Button backToAdminPage;

    @FXML
    private Button userSortAge;

    @FXML
    private Button userSortName;

    @FXML
    private Button userSortRegistered;

    @FXML
    private TableColumn<ModelTable, String> col_name;

    @FXML
    private TableColumn<ModelTable, String> col_phone;

    @FXML
    private TableColumn<ModelTable, String> col_password;

    @FXML
    private TableColumn<ModelTable, String> col_gender;

    @FXML
    private TableColumn<ModelTable, Integer> col_age;

    @FXML
    private TableColumn<ModelTable, String> col_registered;

    @FXML
    private TableColumn<ModelTable, Integer> col_country_id;

    ObservableList<ModelTable> obList = FXCollections.observableArrayList();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        /*Back to Admin page*/
        backToAdminPage.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page.fxml");
        });

        /*Back to Login page*/
        adminLogout.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Load users from database to table view*/
        loadToTable();

        /*Delete user from database*/
        userDeleteButton.setOnAction(actionEvent -> {
            Integer id = Integer.parseInt(userDeleteTextField.getText().trim());
            DatabaseHandler databaseHandler = new DatabaseHandler();
            try {
                databaseHandler.deleteUser(id);
                userDeleteTextField.setText("");
                obList.clear();
                loadToTable();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        /*Sort by name*/
        userSortName.setOnAction(actionEvent -> {
            try {
                obList.clear();
                sortUserNames();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        /*Sort by age*/
        userSortAge.setOnAction(actionEvent -> {
            try {
                obList.clear();
                sortUserAges();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        /*Group by gender*/
        userSortRegistered.setOnAction(actionEvent -> {
            try {
                obList.clear();
                sortUserRegistered();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadToTable() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + Const.USER_TABLE);
        listUsers(resultSet);
    }

    public void sortUserNames() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + Const.USER_TABLE + " order by " + Const.USER_NAME);
        listUsers(resultSet);
    }

    public void sortUserAges() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + Const.USER_TABLE + " order by " + Const.USER_AGE);
        listUsers(resultSet);
    }

    public void sortUserRegistered() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + Const.USER_TABLE + " order by " + Const.USER_REGISTERED);
        listUsers(resultSet);
    }

    public void listUsers(ResultSet resultSet) throws SQLException {
        while (resultSet.next()){
            obList.add(new ModelTable(
                    resultSet.getInt(Const.USER_ID),
                    resultSet.getString(Const.USER_NAME),
                    resultSet.getString(Const.USER_PHONE),
                    resultSet.getString(Const.USER_EMAIL),
                    resultSet.getString(Const.USER_PASSWORD),
                    resultSet.getString(Const.USER_GENDER),
                    resultSet.getInt(Const.USER_AGE),
                    resultSet.getInt(Const.USER_COUNTRY),
                    resultSet.getString(Const.USER_REGISTERED)));
        }

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        col_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_country_id.setCellValueFactory(new PropertyValueFactory<>("country_id"));
        col_registered.setCellValueFactory(new PropertyValueFactory<>("registered_time"));

        tableOfUsers.setItems(obList);
    }

    public void openNewWindow(String window) {
        backToAdminPage.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
