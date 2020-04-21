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

public class RegistrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button registrationBackButton;

    @FXML
    private RadioButton femaleButton;

    @FXML
    private RadioButton maleButton;

    @FXML
    private TextField registration_age_field;

    @FXML
    private TextField registration_phone_field;

    @FXML
    private PasswordField registration_password_field;

    @FXML
    private Button registrationSubmitButton;

    @FXML
    private TextField registration_email_field;

    @FXML
    private TextField registration_name_field;

    @FXML
    private Label ErrorForPhone;

    @FXML
    private Label ErrorForName;

    @FXML
    private Label ErrorForEmail;

    @FXML
    private Label ErrorForPassword;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        /*Submit button*/
        registrationSubmitButton.setOnAction(actionEvent -> {
            try {
                signUpNewUser();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        /*To move login page*/
        registrationBackButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Add items to country*/
        addInfoToCountry();
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

    /*To add new user*/
    private void signUpNewUser() throws SQLException, ClassNotFoundException {
        DatabaseHandler databaseHandler = new DatabaseHandler();

        String name = registration_name_field.getText().trim();
        String phone = registration_phone_field.getText().trim();
        String email = registration_email_field.getText().trim();
        String password = registration_password_field.getText().trim();
        String gender = "";
        if(maleButton.isSelected()){
            gender = "Male";
        }else {
            gender = "Female";
        }
        Integer age = Integer.parseInt(registration_age_field.getText().trim());

        String country_name = countryComboBox.getValue().trim();

        boolean isCorrect = true;

        if(name.equals("")){
            ErrorForName.setText("Write correct name!");
            isCorrect = false;
        }else {
            ErrorForName.setText("");
        }

        if(phone.equals("")){
            ErrorForPhone.setText("Write correct phone number!");
            isCorrect = false;
        }else {
            ErrorForPhone.setText("");
        }

        if(!email.equals("") && (email.endsWith(".com") || email.endsWith(".ru") || email.endsWith(".kz"))){
            ErrorForEmail.setText("");
        }else {
            ErrorForEmail.setText("Write correct email (.com, .kz, .ru)!");
            isCorrect = false;
        }

        if(password.equals("") || password.length() < 8){
            ErrorForPassword.setText("Password length must be 8!");
            isCorrect = false;
        }else {
            ErrorForPassword.setText("");
        }

        if(isCorrect){
            User user = new User(name, phone, email, password, gender, age, getCountryId(country_name));

            try {
                databaseHandler.addUser(user);
                openNewWindow("/sample/interfaces/login_page.fxml");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
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
        registrationBackButton.getScene().getWindow().hide();

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
