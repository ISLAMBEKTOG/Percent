package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button adminSingersButton;

    @FXML
    private Button adminLogout;

    @FXML
    private Button adminUsersButton;

    @FXML
    private Button adminSongsButton;


    @FXML
    void initialize() {
        /*Logout to login page*/
        adminLogout.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Users page*/
        adminUsersButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_user.fxml");
        });

        /*Singers page*/
        adminSingersButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_singers.fxml");
        });

        /*Songs page*/
        adminSongsButton.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_songs.fxml");
        });
    }

    public void openNewWindow(String window) {
        adminLogout.getScene().getWindow().hide();

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
