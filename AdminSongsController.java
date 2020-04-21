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

public class AdminSongsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button adminLogout;

    @FXML
    private Button backToAdminPage;

    @FXML
    private Button listOfSongs;

    @FXML
    private Button modifySong;

    @FXML
    private Button addSong;

    @FXML
    void initialize() {
        /*Back to admin page*/
        backToAdminPage.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page.fxml");
        });

        /*Logout to login page*/
        adminLogout.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/login_page.fxml");
        });

        /*Add song to DB*/
        addSong.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_songs_add.fxml");
        });

        /*List of songs*/
        listOfSongs.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_songs_list.fxml");
        });

        /*Modify songs*/
        modifySong.setOnAction(actionEvent -> {
            openNewWindow("/sample/interfaces/admin_page_songs_modify.fxml");
        });
    }

    public void openNewWindow(String window) {
        backToAdminPage.getScene().getWindow().hide();

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
