package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.animations.Shake;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button loginSignInButton;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private TextField login_email_field;

    @FXML
    private PasswordField login_password_field;

    @FXML
    void initialize() {
        loginSignInButton.setOnAction(actionEvent -> {
            String login_email = login_email_field.getText().trim();
            String login_password = login_password_field.getText().trim();

            if (!login_email.equals("") && !login_password.equals("")) {
                try {
                    loginUser(login_email, login_password);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error!");
            }
        });

        loginSignUpButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/registration_page.fxml");
        });
    }

    private void loginUser(String login_email, String login_password) throws SQLException, ClassNotFoundException {
        /*Redirect to admin page*/
        if(login_email.equals("admin") && login_password.equals("admin777")){
            openNewWindow("/sample/interfaces/admin_page.fxml");
        }

        /*Login clients*/
        DatabaseHandler databaseHandler = new DatabaseHandler();
        User user = new User();
        user.setEmail(login_email);
        user.setPassword(login_password);
        ResultSet resultSet = databaseHandler.getUser(user);

        int counter = 0;

        while (resultSet.next()) {
            counter++;
        }

        /*Open menu if user exist*/
        if (counter >= 1) {
            openNewWindow("/sample/interfaces/menu_page.fxml");
        } else {
            Shake userEmailAnim = new Shake(login_email_field);
            Shake userPasswordAnim = new Shake(login_password_field);

            userEmailAnim.playAnim();
            userPasswordAnim.playAnim();
        }

    }

    public void openNewWindow(String window) {
        loginSignUpButton.getScene().getWindow().hide();

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