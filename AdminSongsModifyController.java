package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminSongsModifyController {
    final FileChooser fileChooser = new FileChooser();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<ModelTableSong> tableOfSongs;

    @FXML
    private TableColumn<ModelTableSong, Integer> col_singer;

    @FXML
    private Button songsDeleteButton;

    @FXML
    private ComboBox<String> styleComboBox;

    @FXML
    private TableColumn<ModelTableSong, Integer> col_id;

    @FXML
    private TableColumn<ModelTableSong, Integer> col_popular;

    @FXML
    private TableColumn<ModelTableSong, String> col_path;

    @FXML
    private Button songsSelectButton;

    @FXML
    private Button logoutButton;

    @FXML
    private TextField changeIdField;

    @FXML
    private TextField changeSongField;

    @FXML
    private TextField changeYearField;

    @FXML
    private TableColumn<ModelTableSong, String> col_title;

    @FXML
    private TableColumn<ModelTableSong, Integer> col_style;

    @FXML
    private Button backButton;

    @FXML
    private Button singersUpdateButton;

    @FXML
    private TableColumn<ModelTableSong, Integer> col_year;

    @FXML
    private TextField changeTitleField;

    @FXML
    private TextField deleteIdField;

    @FXML
    private ComboBox<String> singerComboBox;

    ObservableList<ModelTableSong> obList = FXCollections.observableArrayList();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        DatabaseHandlerSongs databaseHandlerSongs = new DatabaseHandlerSongs();

        /*Back to admin page*/
        backButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_songs.fxml");
        });

        /*Logout to login page*/
        logoutButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Add items to style*/
        addInfoToStyle();

        /*Add items to singers*/
        addInfoToSingers();

        /*Select music from desktop*/
        songsSelectButton.setOnAction(actionEvent -> {
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(backButton.getScene().getWindow());

            if (file != null) {
                try {
                    String localUrl = file.toURI().toURL().toString();
                    changeSongField.setText(localUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        /*Load songs to Table*/
        loadToTable();

        /*Update song*/
        singersUpdateButton.setOnAction(actionEvent -> {
            try {
                updateSongs();
                obList.clear();
                loadToTable();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        /*Delete song by id*/
        songsDeleteButton.setOnAction(actionEvent -> {
            Integer id = Integer.parseInt(deleteIdField.getText().trim());
            try {
                databaseHandlerSongs.deleteSinger(id);
                obList.clear();
                deleteIdField.setText("");
                loadToTable();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateSongs() throws SQLException, ClassNotFoundException {
        int id = Integer.parseInt(changeIdField.getText().trim());

        String title = changeTitleField.getText().trim();
        String year_text = changeYearField.getText().trim();
        int year = 0;
        int popular = 0;
        int singer_id = 0;
        String singer = singerComboBox.getValue();
        int style_id = 0;
        String style_name = styleComboBox.getValue();
        String path = changeSongField.getText().trim();

        Connection connection = DatabaseHandlerSongs.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSongs.SONG_TABLE + " where " + ConstSongs.SONG_ID + " = " + id);

        while (resultSet.next()) {
            if (title.equals("")) {
                title = resultSet.getString(ConstSongs.SONG_TITLE);
            }

            if (year_text.equals("")) {
                year = resultSet.getInt(ConstSongs.SONG_YEAR);
            } else {
                year = Integer.parseInt(year_text);
            }

            popular = resultSet.getInt(ConstSongs.SONG_POPULAR);

            if (style_name.equals("")) {
                style_id = resultSet.getInt(ConstSongs.SONG_STYLE_ID);
            } else {
                style_id = getStyleId(style_name);
            }

            if (singer.equals("")) {
                singer_id = resultSet.getInt(ConstSongs.SONG_SINGER_ID);
            } else {
                singer_id = getIdOfSinger(singer);
            }

            if (path.equals("")) {
                path = resultSet.getString(ConstSongs.SONG_FILE);
            }
        }

        DatabaseHandlerSongs databaseHandlerSongs = new DatabaseHandlerSongs();
        Song song = new Song(title, year, popular, style_id, singer_id, path);
        databaseHandlerSongs.updateSinger(song, id);
    }

    public void addInfoToSingers() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE);

        ArrayList<String> arrayList = new ArrayList<>();
        while (resultSet.next()) {
            arrayList.add(resultSet.getString(ConstSingers.SINGER_NAME));
        }

        singerComboBox.getItems().addAll(arrayList);
    }

    public int getIdOfSinger(String s) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE + " where " + ConstSingers.SINGER_NAME + " = '" + s + "'");

        int id = 0;
        while (resultSet.next()) {
            id = resultSet.getInt(ConstSingers.SINGER_ID);
        }
        return id;
    }

    public void addInfoToStyle() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstStyle.STYLE_TABLE);

        ArrayList<String> arrayList = new ArrayList<>();
        while (resultSet.next()) {
            arrayList.add(resultSet.getString(ConstStyle.STYLE_NAME));
        }

        styleComboBox.getItems().addAll(arrayList);
    }

    public int getStyleId(String style_name) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstStyle.STYLE_TABLE + " where " + ConstStyle.STYLE_NAME + " = '" + style_name + "'");

        int id = 0;

        while (resultSet.next()) {
            id = resultSet.getInt(ConstStyle.STYLE_ID);
        }
        return id;
    }

    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );
    }

    public void loadToTable() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSongs.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSongs.SONG_TABLE);
        listSongs(resultSet);
    }

    public void listSongs(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
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
