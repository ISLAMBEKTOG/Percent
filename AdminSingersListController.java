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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminSingersListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<ModelTableSinger, Integer> col_id;


    @FXML
    private TableColumn<ModelTableSinger, String> col_gender;


    @FXML
    private TableColumn<ModelTableSinger, String> col_name;

    @FXML
    private TableColumn<ModelTableSinger, String> col_image;

    @FXML
    private TableColumn<ModelTableSinger, Integer> col_age;

    @FXML
    private TableView<ModelTableSinger> tableOfSingers;

    @FXML
    private TableColumn<ModelTableSinger, Integer> col_style;

    @FXML
    private Button singerSortName;

    @FXML
    private Button adminLogout;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<ModelTableSinger, Integer> col_country;

    @FXML
    private Button singerSortAge;

    ObservableList<ModelTableSinger> obList = FXCollections.observableArrayList();


    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        /*Back to singer page*/
        backButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_singers.fxml");
        });

        /*Logout to login page*/
        adminLogout.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Load singers from database to table view*/
        loadToTable();

        /*Sort by name*/
        singerSortName.setOnAction(actionEvent -> {
            try {
                obList.clear();
                sortSingerNames();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        /*Sort by age*/
        singerSortAge.setOnAction(actionEvent -> {
            try {
                obList.clear();
                sortSingerAges();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

    public void loadToTable() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE);
        listUsers(resultSet);
    }

    public void sortSingerNames() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE + " order by " + ConstSingers.SINGER_NAME);
        listUsers(resultSet);
    }

    public void sortSingerAges() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE + " order by " + ConstSingers.SINGER_AGE);
        listUsers(resultSet);
    }

    public void listUsers(ResultSet resultSet) throws SQLException {
        while (resultSet.next()){
            obList.add(new ModelTableSinger(
                    resultSet.getInt(ConstSingers.SINGER_ID),
                    resultSet.getString(ConstSingers.SINGER_NAME),
                    resultSet.getInt(ConstSingers.SINGER_AGE),
                    resultSet.getString(ConstSingers.SINGER_GENDER),
                    resultSet.getInt(ConstSingers.SINGER_STYLE_ID),
                    resultSet.getInt(ConstSingers.SINGER_COUNTRY_ID),
                    resultSet.getString(ConstSingers.SINGER_IMAGE)));
        }

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        col_style.setCellValueFactory(new PropertyValueFactory<>("style_id"));
        col_country.setCellValueFactory(new PropertyValueFactory<>("country_id"));
        col_image.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        tableOfSingers.setItems(obList);
    }

    public void openNewWindow(String window) {
        backButton.getScene().getWindow().hide();

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
