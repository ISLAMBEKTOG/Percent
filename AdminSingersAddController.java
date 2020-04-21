package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
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

public class AdminSingersAddController {
    final FileChooser fileChooser = new FileChooser();
    private String localUrl;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private RadioButton maleButton;

    @FXML
    private TextField singers_name_field;

    @FXML
    private Button singersSubmitButton;

    @FXML
    private Button BackButton;

    @FXML
    private TextField singers_age_field;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private RadioButton femaleButton;

    @FXML
    private Button importImageButton;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Label messageSuccess;

    @FXML
    private Circle myCircle;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        DatabaseHandlerSingers databaseHandlerSingers = new DatabaseHandlerSingers();

        /*Back to singers admin page*/
        BackButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_singers.fxml");
        });

        /*Select image from desktop*/
        importImageButton.setOnAction(actionEvent -> {
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(BackButton.getScene().getWindow());

            if (file != null) {
                try {
                    localUrl = file.toURI().toURL().toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Image im = new Image(localUrl, false);
                myCircle.setFill(new ImagePattern(im));
                myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            }
        });

        /*Add items to style*/
        addInfoToStyle();

        /*Add items to country*/
        addInfoToCountry();

        /*Add singer to DB*/
        singersSubmitButton.setOnAction(actionEvent -> {
            String name = singers_name_field.getText().trim();
            Integer age = Integer.parseInt(singers_age_field.getText().trim());
            String gender = "";
            if (maleButton.isSelected()) {
                gender = "Male";
            } else {
                gender = "Female";
            }
            String style_name = comboBox.getValue().trim();
            String country_name = countryComboBox.getValue().trim();
            String imagePath = localUrl;

            Singer singer = null;
            try {
                singer = new Singer(name, age, gender, getStyleId(style_name),getCountryId(country_name),imagePath);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                databaseHandlerSingers.addSinger(singer);
                messageSuccess.setText("Singer added!");
                singers_age_field.setText("");
                singers_name_field.setText("");
                myCircle.setFill(null);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void addInfoToStyle() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstStyle.STYLE_TABLE);

        ArrayList<String> arrayList = new ArrayList<>();
        while (resultSet.next()){
            arrayList.add(resultSet.getString(ConstStyle.STYLE_NAME));
        }

        comboBox.getItems().addAll(arrayList);
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

    public void addInfoToCountry() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstCountry.COUNTRY_TABLE);

        ArrayList<String> arrayList = new ArrayList<>();
        while (resultSet.next()){
            arrayList.add(resultSet.getString(ConstCountry.COUNTRY_NAME));
        }

        countryComboBox.getItems().addAll(arrayList);
    }

    public int getCountryId(String country_name) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstCountry.COUNTRY_TABLE + " where " + ConstCountry.COUNTRY_NAME + " = '" + country_name + "'");

        int id = 0;

        while (resultSet.next()){
            id = resultSet.getInt(ConstCountry.COUNTRY_ID);
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
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    public void openNewWindow(String window) {
        BackButton.getScene().getWindow().hide();

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
