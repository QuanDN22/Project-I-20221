package MyMain;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.*;

public class Main extends Application {
    public static int NumberOfFloors = 3;
    public static int NumberOfRooms = 6;
    public static int NumberOfBookCases = 12;
    public static int NumberOfBookShelves = 4;
    public static Stage primaryStage;
    public static Stage secondaryStage;
    public static Scene loginScene;
    public static Scene menuScene;
    public static Scene bookScene;
    public static Scene readerScene;
    public static Scene callScene;
    public static Scene bookSearchScene;
    public static Scene readerSearchScene;
    public static Connection connection;
    public static Statement statement;


    @Override
    public void start(Stage stage) throws IOException {
        connectToDatabase();
        primaryStage = stage;
        secondaryStage = new Stage();
        loginScene = new Scene(new FXMLLoader(MyMain.Main.class.getResource("/MyFXML/Login.fxml")).load());
        menuScene = new Scene(new FXMLLoader(MyMain.Main.class.getResource("/MyFXML/Menu.fxml")).load());
        bookScene = new Scene(new FXMLLoader(MyMain.Main.class.getResource("/MyFXML/Book.fxml")).load());
        readerScene = new Scene(new FXMLLoader(MyMain.Main.class.getResource("/MyFXML/Reader.fxml")).load());
        readerSearchScene = new Scene(new FXMLLoader(MyMain.Main.class.getResource("/MyFXML/ReaderSearch.fxml")).load());
        bookSearchScene = new Scene(new FXMLLoader(MyMain.Main.class.getResource("/MyFXML/BookSearch.fxml")).load());
//        callScene = new Scene(new FXMLLoader(MyMain.Main.class.getResource("/MyFXML/Call.fxml")).load());
        stage.setOnCloseRequest(p->{
            if(secondaryStage.isShowing()){
                secondaryStage.toFront();
                p.consume();
            }
        });
        stage.setTitle("Library Project");
        stage.setScene(loginScene);
        stage.show();
    }
    public static void connectToDatabase() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=PROJECT_1;encrypt=true;trustServerCertificate=true;";
            String user = "Thanh";
            String pass = "123456";

            connection = DriverManager.getConnection(dbURL, user, pass);
            statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        launch();
    }
}