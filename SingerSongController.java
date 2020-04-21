package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SingerSongController {
    private int id;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label usernameField;

    @FXML
    private Pane singerBio;

    @FXML
    private Button menuLogout;

    @FXML
    private ScrollPane testScrollPane;

    @FXML
    private Button backButton;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        /*Back button*/
        backButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/singer_page.fxml");
        });
        /*Logout button*/
        menuLogout.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Username for title*/
        usernameField.setText(DatabaseHandler.getUsernameForTitle());

        /*Get singer*/
        getSinger(Singer.id);

        /*Get songs for this singer*/
        getSong();
    }

    public void getSinger(Integer id) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE + " where " + ConstSingers.SINGER_ID + " = " + id);
        loadSinger(resultSet);
    }

    public void loadSinger(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        VBox vBox = new VBox();

        while (resultSet.next()) {
            String name = resultSet.getString(ConstSingers.SINGER_NAME);
            String image_path = resultSet.getString(ConstSingers.SINGER_IMAGE);
            String style = getStyleName(resultSet.getInt(ConstSingers.SINGER_STYLE_ID));
            String country = getCountryName(resultSet.getInt(ConstSingers.SINGER_COUNTRY_ID));

            /*Pane for every singer*/
            BorderPane borderPane = new BorderPane();
            borderPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            borderPane.setPadding(new Insets(10, 10, 10, 10));
            borderPane.setBackground(new Background(new BackgroundFill(Color.web("#551975"), CornerRadii.EMPTY, Insets.EMPTY)));
            borderPane.setPrefHeight(150);
            borderPane.setPrefWidth(515);

            VBox contentV = new VBox();
            HBox contentH = new HBox();

            /*Name of singer*/
            Label name_singer = new Label();
            name_singer.setText(name);
            name_singer.setFont(new Font("Georgia Italic", 28));
            name_singer.setTextFill(Color.web("#eee7e7"));
            contentV.getChildren().add(name_singer);

            /*Country of singer*/
            Label country_singer = new Label();
            country_singer.setText("Country: " + country);
            country_singer.setFont(new Font("Arial", 15));
            country_singer.setTextFill(Color.web("#eee7e7"));
            contentV.getChildren().add(country_singer);

            /*Style of singer*/
            Label style_singer = new Label();
            style_singer.setText("Style: " + style);
            style_singer.setFont(new Font("Arial", 15));
            style_singer.setTextFill(Color.web("#eee7e7"));
            contentV.getChildren().add(style_singer);

            /*Style of singer*/
            Label countOfSongs = new Label();
            countOfSongs.setText("Count of songs: " + getCountOfSongs());
            countOfSongs.setFont(new Font("Arial", 15));
            countOfSongs.setTextFill(Color.web("#eee7e7"));
            contentV.getChildren().add(countOfSongs);

            contentV.setSpacing(13);

            /*Photo of singer*/
            Circle myCircle = new Circle();
            Image im = new Image(image_path, false);
            myCircle.setFill(new ImagePattern(im));
            myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            myCircle.setRadius(60);

            contentH.getChildren().add(myCircle);
            contentH.getChildren().add(contentV);
            contentH.setSpacing(20);

            borderPane.setCenter(contentH);

            /*Adding*/
            vBox.getChildren().add(borderPane);
            vBox.setSpacing(10);

            singerBio.getChildren().add(vBox);
        }
    }

    public String getStyleName(Integer style_id) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstStyle.STYLE_TABLE + " where " + ConstStyle.STYLE_ID + " = " + style_id);

        String name = "";

        while (resultSet.next()){
            name = resultSet.getString(ConstStyle.STYLE_NAME);
        }
        return name;
    }

    public String getCountryName(Integer country_id) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstCountry.COUNTRY_TABLE + " where " + ConstCountry.COUNTRY_ID + " = " + country_id);

        String name = "";

        while (resultSet.next()){
            name = resultSet.getString(ConstCountry.COUNTRY_NAME);
        }
        return name;
    }

    public void getSong() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSongs.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM SYSTEM.SINGERS A JOIN SYSTEM.SONGS B ON(a.singer_id = b.singer_id) WHERE a.singer_id = " + Singer.id);
        loadSong(resultSet);
    }


    public void loadSong(ResultSet resultSet) throws SQLException, ClassNotFoundException {
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

    public int getCountOfSongs() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSongs.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT COUNT(a.singer_id) Count FROM SYSTEM.SINGERS A JOIN SYSTEM.SONGS B ON(a.singer_id = b.singer_id) HAVING(A.SINGER_ID = " + Singer.id + ") GROUP BY (a.singer_id)");

        int count = 0;
        while (resultSet.next()){
            count = resultSet.getInt("Count");
        }
        return count;
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
