package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class AdminSongsAddController {
    final FileChooser fileChooser = new FileChooser();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button singersSubmitButton;

    @FXML
    private ComboBox<String> styleComboBox;

    @FXML
    private TextField song_title_field;

    @FXML
    private TextField pathToSongField;

    @FXML
    private Label successMessage;

    @FXML
    private TextField song_popular_field;

    @FXML
    private Button backButton;

    @FXML
    private TextField song_year_field;

    @FXML
    private Button pathToSongButton;

    @FXML
    private ComboBox<String> singerComboBox;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        DatabaseHandlerSongs databaseHandlerSongs = new DatabaseHandlerSongs();

        /*Back to admin page*/
        backButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_songs.fxml");
        });

        /*Add items to style*/
        addInfoToStyle();

        /*Add items to singers*/
        addInfoToSingers();

        /*Select music from desktop*/
        pathToSongButton.setOnAction(actionEvent -> {
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(backButton.getScene().getWindow());

            if (file != null) {
                try {
                    String localUrl = file.toURI().toURL().toString();
                    pathToSongField.setText(localUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        /*Add song to DB*/
        singersSubmitButton.setOnAction(actionEvent -> {
            String title = song_title_field.getText().trim();
            String year_text = song_year_field.getText().trim();
            Integer year = null;
            if(year_text.equals("")){
                year = 2020;
            }else{
                year = Integer.parseInt(year_text);
            }

            String popular_text = song_popular_field.getText().trim();
            Integer popular = null;
            if(popular_text.equals("")){
                popular = 1_000_000;
            }else {
                popular = Integer.parseInt(popular_text);
            }

            Integer style_id = null;
            String style_name = styleComboBox.getValue().trim();
            if (style_name.equals("")) {
                style_id = 6;
            }else {
                try {
                    style_id = getStyleId(style_name);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            String singer_id_text = singerComboBox.getValue().trim();
            Integer singer_id = null;
            if(singer_id_text.equals("")){
                try {
                    singer_id = getIdOfSinger("Other");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    singer_id = getIdOfSinger(singer_id_text);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            String pathToImage = pathToSongField.getText().trim();

            Song song = new Song(title, year,popular, style_id, singer_id, pathToImage);

            try {
                databaseHandlerSongs.addSong(song);
                successMessage.setText("Song added!");
                song_title_field.setText("");
                song_year_field.setText("");
                song_popular_field.setText("");
                pathToSongField.setText("");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void addInfoToSingers() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE);

        ArrayList<String> arrayList = new ArrayList<>();
        while (resultSet.next()){
            arrayList.add(resultSet.getString(ConstSingers.SINGER_NAME));
        }

        singerComboBox.getItems().addAll(arrayList);
    }

    public int getIdOfSinger(String s) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE + " where " + ConstSingers.SINGER_NAME + " = '" + s + "'");

        int id = 0;
        while (resultSet.next()){
            id = resultSet.getInt(ConstSingers.SINGER_ID);
        }
        return id;
    }

    public void addInfoToStyle() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstStyle.STYLE_TABLE);

        ArrayList<String> arrayList = new ArrayList<>();
        while (resultSet.next()){
            arrayList.add(resultSet.getString(ConstStyle.STYLE_NAME));
        }

        styleComboBox.getItems().addAll(arrayList);
    }

    public int getStyleId(String style_name) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstStyle.STYLE_TABLE + " where " + ConstStyle.STYLE_NAME + " = '" + style_name + "'");

        int id = 0;

        while (resultSet.next()){
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
