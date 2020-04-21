package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SongController {
    private int id;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label usernameField;

    @FXML
    private Button menuLogout;

    @FXML
    private ScrollPane testScrollPane;

    @FXML
    private Button backButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        /*Back button*/
        backButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/menu_page.fxml");
        });
        /*Logout button*/
        menuLogout.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Username for title*/
        usernameField.setText(DatabaseHandler.getUsernameForTitle());

        /*List of songs*/
        getSongs();

        /*Search songs by title*/
        searchButton.setOnAction(actionEvent -> {
            String text = searchField.getText().trim();
            try {
                getSearchSongs(text);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void getSearchSongs(String s) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSongs.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSongs.SONG_TABLE + " where LOWER(TITLE) like '%" + s.toLowerCase() + "%'");
        loadSongs(resultSet);
    }

    public void getSongs() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSongs.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSongs.SONG_TABLE);
        loadSongs(resultSet);
    }

    public String getSingerName() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE + " a join " + ConstSongs.SONG_TABLE + " b" +
                " on (a." + ConstSingers.SINGER_ID + " = b." + ConstSongs.SONG_SINGER_ID + ") where a." + ConstSingers.SINGER_ID + " = " + id);

        String name = "";

        while (resultSet.next()){
            name = resultSet.getString(ConstSingers.SINGER_NAME);
            break;
        }

        return name;
    }


    public void loadSongs(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        VBox vBox = new VBox();
        testScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; ");

        while (resultSet.next()) {
            id = resultSet.getInt(ConstSongs.SONG_SINGER_ID);
            String title = resultSet.getString(ConstSongs.SONG_TITLE);
            String year = resultSet.getString(ConstSongs.SONG_YEAR);
            String popular = resultSet.getString(ConstSongs.SONG_POPULAR);
            Integer style_id = resultSet.getInt(ConstSongs.SONG_STYLE_ID);
            String path = resultSet.getString(ConstSongs.SONG_FILE);

            /*Pane for every singer*/
            BorderPane borderPane = new BorderPane();
            borderPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            borderPane.setPadding(new Insets(10, 10, 10, 10));
            borderPane.setBackground(new Background(new BackgroundFill(Color.web("#551975"), CornerRadii.EMPTY, Insets.EMPTY)));
            borderPane.setPrefHeight(70);
            borderPane.setPrefWidth(500);

            VBox contentV = new VBox();
            contentV.setSpacing(10);

            HBox contentH = new HBox();
            contentH.setSpacing(30);

            HBox contentMedia = new HBox();
            contentMedia.setSpacing(10);

            /*Media Player*/
            Media media = new Media(path);
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            /*Media Controller class*/
            MediaControl mediaControl = new MediaControl(mediaPlayer);

            contentMedia.getChildren().add(mediaControl);

            /*Title of song*/
            Label title_song = new Label();
            title_song.setText(title);
            title_song.setFont(new Font("Georgia Italic", 18));
            contentV.getChildren().add(title_song);

            /*Name of singer*/
            Label name_singer = new Label();
            name_singer.setText("Singer: " + getSingerName());
            name_singer.setFont(new Font("Arial", 14));
            contentH.getChildren().add(name_singer);

            /*Year og song*/
            Label year_song = new Label();
            year_song.setText("Year: " + year);
            year_song.setFont(new Font("Arial", 14));
            contentH.getChildren().add(year_song);

            /*Style of song*/
            Label style_song = new Label();
            style_song.setText("Style: " + getStyleName(style_id));
            style_song.setFont(new Font("Arial", 14));
            contentH.getChildren().add(style_song);

            /*Popularity of song*/
            Label popular_song = new Label();
            popular_song.setText("Popularity: " + popular);
            popular_song.setFont(new Font("Arial", 14));
            contentH.getChildren().add(popular_song);

            contentV.getChildren().add(contentH);
            contentV.getChildren().add(contentMedia);


            borderPane.setCenter(contentV);

            /*Adding*/
            vBox.getChildren().add(borderPane);
            vBox.setSpacing(7);

            testScrollPane.setContent(vBox);
        }
    }

    public String getStyleName(Integer id) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstStyle.STYLE_TABLE + " where " + ConstStyle.STYLE_ID + " = " + id);

        String name = "";

        while (resultSet.next()){
            name = resultSet.getString(ConstStyle.STYLE_NAME);
        }
        return name;
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
