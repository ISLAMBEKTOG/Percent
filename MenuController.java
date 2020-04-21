package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label usernameField;

    @FXML
    private Button singerShow;

    @FXML
    private Button songShow;

    @FXML
    private Button settingsButton;

    @FXML
    private Button menuLogout;


    @FXML
    void initialize() throws SQLException {
        /*Logout button*/
        menuLogout.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Username for title*/
        usernameField.setText(DatabaseHandler.getUsernameForTitle());

        /*Settings button*/
        settingsButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/settings_page.fxml");
        });

        /*Show singer page*/
        singerShow.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/singer_page.fxml");
        });

        /*Show song page*/
        songShow.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/song_page.fxml");
        });
    }

    public void openNewWindow(String window) {
        menuLogout.getScene().getWindow().hide();

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
