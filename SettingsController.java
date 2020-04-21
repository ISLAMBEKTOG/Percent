package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SettingsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField settings_password_field;

    @FXML
    private Label placeEmailAddress;

    @FXML
    private Label error_setting_phone;

    @FXML
    private TextField settings_age_field;

    @FXML
    private Label error_setting_password;

    @FXML
    private TextField settings_name_field;

    @FXML
    private Label messageIfChanged;

    @FXML
    private RadioButton settings_male;

    @FXML
    private RadioButton settings_female;

    @FXML
    private Button backButton;

    @FXML
    private Label error_setting_name;

    @FXML
    private Button settingsSubmitButton;

    @FXML
    private TextField settings_phone_field;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        /*Back to menu*/
        backButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/menu_page.fxml");
        });

        /*Email address for title*/
        placeEmailAddress.setText(DatabaseHandler.getEmailForSettings());

        /*Add info to country*/
        addInfoToCountry();

        /*Submit changed info*/
        settingsSubmitButton.setOnAction(actionEvent -> {
            DatabaseHandler databaseHandler = new DatabaseHandler();

            String name = settings_name_field.getText().trim();
            String phone = settings_phone_field.getText().trim();
            String email = DatabaseHandler.getEmailForSettings().trim();
            String password = settings_password_field.getText().trim();
            String gender = "";
            if (settings_male.isSelected()) {
                gender = "Male";
            } else {
                gender = "Female";
            }
            Integer age = Integer.parseInt(settings_age_field.getText().trim());

            String country_name = countryComboBox.getValue().trim();

            boolean isCorrect = true;

            if (name.equals("")) {
                error_setting_name.setText("Write correct name!");
                isCorrect = false;
            } else {
                error_setting_name.setText("");
            }

            if (phone.equals("")) {
                error_setting_phone.setText("Write correct phone number!");
                isCorrect = false;
            } else {
                error_setting_phone.setText("");
            }

            if (password.equals("") || password.length() < 8) {
                error_setting_password.setText("Password length must be 8!");
                isCorrect = false;
            } else {
                error_setting_password.setText("");
            }

            if (isCorrect) {
                User user = null;
                try {
                    user = new User(name, phone, email, password, gender, age,getCountryId(country_name));
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    databaseHandler.updateUser(user);
                    messageIfChanged.setText("Changes saved!");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
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
