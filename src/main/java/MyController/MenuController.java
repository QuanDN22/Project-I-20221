package MyController;

import MyMain.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    Button bookB, readerB, borrowB, logoutB;
    public void bookManager(ActionEvent event){
        Main.primaryStage.setScene(Main.bookScene);
    }
    public void readerManager(ActionEvent event){
        Main.primaryStage.setScene(Main.readerScene);
    }
    public void borrowManager(ActionEvent event){
        Main.primaryStage.setScene(Main.callScene);
    }
    public void logout(ActionEvent event){
        Main.primaryStage.setScene(Main.loginScene);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
