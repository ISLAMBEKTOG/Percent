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

public class AdminSongsListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<ModelTableSong> tableOfSongs;

    @FXML
    private TableColumn<ModelTableSong, Integer> col_singer;

    @FXML
    private TableColumn<ModelTable, String> col_title;

    @FXML
    private TableColumn<ModelTable, Integer> col_style;

    @FXML
    private TableColumn<ModelTable, Integer> col_id;

    @FXML
    private TableColumn<ModelTable, Integer> col_popular;

    @FXML
    private TableColumn<ModelTable, String> col_path;

    @FXML
    private Button singerSortTitle;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<ModelTable, Integer> col_year;

    @FXML
    private Button logoutButton;

    @FXML
    private Button singerSortPopular;

    ObservableList<ModelTableSong> obList = FXCollections.observableArrayList();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        /*Back to admin page*/
        backButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_songs.fxml");
        });

        /*Logout to login page*/
        logoutButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Load songs from DB to table view*/
        loadToTable();

        /*Sort by titles*/
        singerSortTitle.setOnAction(actionEvent -> {
            try {
                obList.clear();
                sortSortTitles();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        /*Sort by populates*/
        singerSortPopular.setOnAction(actionEvent -> {
            try {
                obList.clear();
                sortSortPopulates();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadToTable() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSongs.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSongs.SONG_TABLE);
        listSongs(resultSet);
    }

    public void sortSortTitles() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSongs.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSongs.SONG_TABLE + " order by " + ConstSongs.SONG_TITLE);
        listSongs(resultSet);
    }

    public void sortSortPopulates() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSongs.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSongs.SONG_TABLE + " order by " + ConstSongs.SONG_POPULAR);
        listSongs(resultSet);
    }

    public void listSongs(ResultSet resultSet) throws SQLException {
        while (resultSet.next()){
            obList.add(new ModelTableSong(
                    resultSet.getInt(ConstSongs.SONG_ID),
                    resultSet.getString(ConstSongs.SONG_TITLE),
                    resultSet.getInt(ConstSongs.SONG_YEAR),
                    resultSet.getInt(ConstSongs.SONG_POPULAR),
                    resultSet.getInt(ConstSongs.SONG_STYLE_ID),
                    resultSet.getInt(ConstSongs.SONG_SINGER_ID),
                    resultSet.getString(ConstSongs.SONG_FILE)));
        }

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        col_popular.setCellValueFactory(new PropertyValueFactory<>("popular"));
        col_style.setCellValueFactory(new PropertyValueFactory<>("style_id"));
        col_singer.setCellValueFactory(new PropertyValueFactory<>("singer_id"));
        col_path.setCellValueFactory(new PropertyValueFactory<>("path"));

        tableOfSongs.setItems(obList);
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
