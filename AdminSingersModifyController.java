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

public class AdminSingersModifyController {
    final FileChooser fileChooser = new FileChooser();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> styleComboBox;

    @FXML
    private TableColumn<ModelTableSinger, Integer> col_id;

    @FXML
    private Button singersImageButton;

    @FXML
    private TableColumn<ModelTableSinger, String> col_gender;

    @FXML
    private TableColumn<ModelTableSinger, String> col_name;

    @FXML
    private TextField changeIdField;

    @FXML
    private TableColumn<ModelTableSinger, String> col_image;

    @FXML
    private TableColumn<ModelTableSinger, Integer> col_age;

    @FXML
    private TableView<ModelTableSinger> tableOfSingers;

    @FXML
    private TableColumn<ModelTableSinger, Integer> col_style;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private Button singersLogout;

    @FXML
    private Button backButton;

    @FXML
    private TextField changeAgeField;

    @FXML
    private Button singersUpdateButton;

    @FXML
    private TableColumn<ModelTableSinger, Integer> col_country;

    @FXML
    private TextField changeNameField;

    @FXML
    private TextField changeImageField;

    @FXML
    private Button singersDeleteButton;

    @FXML
    private TextField deleteIdField;

    ObservableList<ModelTableSinger> obList = FXCollections.observableArrayList();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        DatabaseHandlerSingers databaseHandlerSingers = new DatabaseHandlerSingers();

        /*Back to singers page*/
        backButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_singers.fxml");
        });

        /*Logout to login page*/
        singersLogout.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Load singers from DB to table*/
        loadToTable();

        /*Delete singer by id*/
        singersDeleteButton.setOnAction(actionEvent -> {
            Integer id = Integer.parseInt(deleteIdField.getText().trim());
            try {
                databaseHandlerSingers.deleteSinger(id);
                obList.clear();
                deleteIdField.setText("");
                loadToTable();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        /*Items to the style*/
        addInfoToStyle();

        /*Items to the country*/
        addInfoToCountry();

        /*Select image from desktop*/
        singersImageButton.setOnAction(actionEvent -> {
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(backButton.getScene().getWindow());

            String localUrl = "";

            if (file != null) {
                try {
                    localUrl = file.toURI().toURL().toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                changeImageField.setText(localUrl);
            }
        });

        /*Update singer*/
        singersUpdateButton.setOnAction(actionEvent -> {
            try {
                updateSingers();
                obList.clear();
                loadToTable();
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

    public void addInfoToCountry() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstCountry.COUNTRY_TABLE);

        ArrayList<String> arrayList = new ArrayList<>();
        while (resultSet.next()) {
            arrayList.add(resultSet.getString(ConstCountry.COUNTRY_NAME));
        }

        countryComboBox.getItems().addAll(arrayList);
    }

    public int getCountryId(String country_name) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandler.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstCountry.COUNTRY_TABLE + " where " + ConstCountry.COUNTRY_NAME + " = '" + country_name + "'");

        int id = 0;

        while (resultSet.next()) {
            id = resultSet.getInt(ConstCountry.COUNTRY_ID);
        }
        return id;
    }

    public void loadToTable() throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE);
        listSingers(resultSet);
    }

    public void listSingers(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
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

    public void updateSingers() throws SQLException, ClassNotFoundException {
        int id = Integer.parseInt(changeIdField.getText().trim());

        String name = changeNameField.getText().trim();
        String age_text = changeAgeField.getText();
        Integer age = null;
        String gender = "";
        Integer style_id = null;
        String style_name = styleComboBox.getValue().trim();
        Integer counter_id = null;
        String country_name = countryComboBox.getValue().trim();
        String image = changeImageField.getText().trim();

        Connection connection = DatabaseHandlerSingers.getDbConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("select * from " + ConstSingers.SINGER_TABLE + " where " + ConstSingers.SINGER_ID + " = " + id);

        while (resultSet.next()) {
            if (name.equals("")) {
                name = resultSet.getString(ConstSingers.SINGER_NAME);
            }
            if (country_name.equals("")) {
                counter_id = resultSet.getInt(ConstSingers.SINGER_COUNTRY_ID);
            }else {
                counter_id = getCountryId(country_name);
            }
            if (age_text.equals("")) {
                age = resultSet.getInt(ConstSingers.SINGER_AGE);
            } else {
                age = Integer.parseInt(age_text);
            }
            gender = resultSet.getString(ConstSingers.SINGER_GENDER);
            if (style_name.equals("")) {
                style_id = resultSet.getInt(ConstSingers.SINGER_STYLE_ID);
            }else {
                style_id = getStyleId(style_name);
            }
            if (image.equals("")) {
                image = resultSet.getString(ConstSingers.SINGER_IMAGE);
            }
        }

        Singer singer = new Singer(name, age, gender, style_id, counter_id, image);
        DatabaseHandlerSingers databaseHandlerSingers = new DatabaseHandlerSingers();
        databaseHandlerSingers.updateSinger(singer, id);
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
