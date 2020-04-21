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

public class AdminSingersController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button modifySinger;

    @FXML
    private Button adminLogout;

    @FXML
    private Button backToAdminPage;

    @FXML
    private Button addSinger;

    @FXML
    private Button listOfSingers;


    @FXML
    void initialize() {
        /*Logout to login page*/
        adminLogout.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Back to admin page*/
        backToAdminPage.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page.fxml");
        });

        /*Add singer*/
        addSinger.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_singers_add.fxml");
        });

        /*List of singers*/
        listOfSingers.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_singers_list.fxml");
        });

        /*Modify singer*/
        modifySinger.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_singers_modify.fxml");
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
