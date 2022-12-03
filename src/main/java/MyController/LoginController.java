package MyController;

import MyClass.User;
import MyMain.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button loginB,registerB,exitB;
    @FXML
    private Label status;
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
            if(u.equals(user)) {
                Main.primaryStage.setScene(Main.menuScene);
                System.out.println("Successfully!");
                status.setText("Login successfully!");
                c = 1;
                break;
            }
//        Main.primaryStage.setScene(Main.menuScene);
        if(c == 0) {
            System.out.println("Unsuccessfully!");
            status.setText("Login unsuccessfully!");
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
        String username = usernameField.getText();
        String password = passwordField.getText();
        user = new User(username, password);
        if(user.isValidAccount()) {
            try {
                String sql;
                sql = String.format("insert into [Users](username, password) values('%s', '%s')", username, password);
                Main.statement.executeUpdate(sql);
                System.out.println("Successfully!");
                status.setText("Register successfully!");
                USERS.add(user);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Unsuccessfully!");
            status.setText("Register unsuccessfully!");
        }
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
}
