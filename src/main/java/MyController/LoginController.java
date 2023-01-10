package MyController;

import MyClass.User;
import MyMain.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button loginB,registerB,exitB;
//    @FXML
//    private Label status;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    public static final ObservableList<User> USERS = FXCollections.observableArrayList();
    private User user;

    @FXML
    private void login(ActionEvent event){
        String username = usernameField.getText();
        String password = passwordField.getText();
        user = new User(username, password);
        int c = 0;
        for(User u : USERS)
            if(u.equals(user) && user.getUsername().equals("null") == false) {
                Main.primaryStage.setScene(Main.menuScene);
                System.out.println("Successfully!");
//                status.setText("Login successfully!");
                c = 1;
                break;

//        Main.primaryStage.setScene(Main.menuScene)
            };
        if(c == 0) {
            System.out.println("Unsuccessfully!");
//            status.setText("Login unsuccessfully!");
            String text = "Wrong username or password. Retype!";
            showAlertErrorLogin(text);
        }
    }
    @FXML
    private void exit(ActionEvent event) throws SQLException {
        Main.statement.close();
        Main.connection.close();
        Main.primaryStage.close();
        System.out.println("Successfully!");
    }
    @FXML
    private void register(ActionEvent event){
        Main.primaryStage.setScene(Main.registerScene);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            String sql;
            sql = "select username, password from [Users]";
            ResultSet rs;
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                USERS.add(new User(rs.getString(1), rs.getString(2)));
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlertErrorLogin(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Warning");
        alert.setHeaderText(null);
        alert.setContentText(text);

        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(close);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == close) {
            alert.close();
        }
    }


    // Phương thức chuyển màn hình
    public static void changeScene(ActionEvent event, String fxmlFile, String title) {
        Parent root = null;

        try {
            root = FXMLLoader.load(LoginController.class.getResource(fxmlFile));
        } catch(Exception e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
