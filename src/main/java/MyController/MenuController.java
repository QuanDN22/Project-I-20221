package MyController;

import MyMain.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private Button bookB, readerB, borrowB, logoutB;
    public static final Stage[] stages = new Stage[3];
    @FXML
    private void bookManager(ActionEvent event){
        BookController.updateTitleTable();
        stages[0].setScene(Main.bookScene);
        stages[0].show();
    }
    @FXML
    private void readerManager(ActionEvent event){
        ReaderController.updateReaderTable();
        stages[1].setScene(Main.readerScene);
        stages[1].show();
    }
    @FXML
    private void borrowManager(ActionEvent event){
        stages[2].setScene(Main.callScene);
        stages[2].show();
    }
    @FXML
    private void logout(ActionEvent event){
        for (int i = 0; i < 3; i++){
            if (stages[i].isShowing()){
                stages[i].toFront();
                return;
            }
        }
        Main.primaryStage.setScene(Main.loginScene);
    }

    @FXML
    private void register(ActionEvent event){
        Main.primaryStage.setScene(Main.registerScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stages[0] = new Stage();
        stages[1] = new Stage();
        stages[2] = new Stage();
        Main.primaryStage.setOnCloseRequest(p->{
            for (int i = 0; i < 3; i++){
                if (stages[i].isShowing()){
                    stages[i].toFront();
                    p.consume();
                }
            }
        });
    }
}
