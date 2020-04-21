package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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

public class SingerController {
    private Stage stage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label usernameField;

    @FXML
    private Button menuLogout;

    @FXML
    private Button backButton;

    @FXML
    private ScrollPane testScrollPane;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        /*Username for title*/
        usernameField.setText(DatabaseHandler.getUsernameForTitle());

        /*Back to menu page*/
        backButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/menu_page.fxml");
        });

        /*Logout to login page*/
        menuLogout.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*List of singers*/
        getSingers();

        /*Search singers by name*/
        searchButton.setOnAction(actionEvent -> {
            String text = searchTextField.getText().trim();
            try {
                getSearchSingers(text);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void getSearchSingers(String s) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE + " WHERE LOWER(SINGER_NAME) LIKE '%" + s.toLowerCase() + "%'");
        loadSingers(resultSet);
    }

    public void getSingers() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE);
        loadSingers(resultSet);
    }

    public void loadSingers(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        VBox vBox = new VBox();
        testScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; ");

        while (resultSet.next()) {
            Integer id = resultSet.getInt(ConstSingers.SINGER_ID);
            String name = resultSet.getString(ConstSingers.SINGER_NAME);
            String image_path = resultSet.getString(ConstSingers.SINGER_IMAGE);
            String style = getStyleName(resultSet.getInt(ConstSingers.SINGER_STYLE_ID));
            String country = getCountryName(resultSet.getInt(ConstSingers.SINGER_COUNTRY_ID));


            /*Pane for every singer*/
            BorderPane borderPane = new BorderPane();
            borderPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            borderPane.setPadding(new Insets(10, 10, 10, 10));
            borderPane.setBackground(new Background(new BackgroundFill(Color.web("#551975"), CornerRadii.EMPTY, Insets.EMPTY)));
            borderPane.setPrefHeight(100);
            borderPane.setPrefWidth(400);

            VBox contentV = new VBox();
            HBox contentH = new HBox();

            /*Name of singer*/
            Label name_singer = new Label();
            name_singer.setText(name);
            name_singer.setFont(new Font("Georgia Italic", 22));
            contentV.getChildren().add(name_singer);

            /*Country of singer*/
            Label country_singer = new Label();
            country_singer.setText("Country: " + country);
            country_singer.setFont(new Font("Arial", 14));
            contentV.getChildren().add(country_singer);

            /*Style of singer*/
            Label style_singer = new Label();
            style_singer.setText("Style: " + style);
            style_singer.setFont(new Font("Arial", 14));
            contentV.getChildren().add(style_singer);
            contentV.setSpacing(10);

            /*Photo of singer*/
            Circle myCircle = new Circle();
            Image im = new Image(image_path, false);
            myCircle.setFill(new ImagePattern(im));
            myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            myCircle.setRadius(50);

            /*Button show songs*/
            Button button = new Button("Show songs");
            button.setFont(new Font("Arial", 14));
            button.setStyle("-fx-background-color: #82177e; -fx-text-fill: #eee7e7");

            /*Action when clicked*/
            button.setOnAction(actionEvent -> {
                Singer.id = id;
                openNewWindow("/sample/interfaces/singer_song_page.fxml");
            });

            contentH.getChildren().add(myCircle);
            contentH.getChildren().add(contentV);
            contentH.setSpacing(20);

            borderPane.setCenter(contentH);
            borderPane.setRight(button);

            /*Adding*/
            vBox.getChildren().add(borderPane);
            vBox.setSpacing(10);

            testScrollPane.setContent(vBox);
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
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
