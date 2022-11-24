package MyController;

import MyClass.Reader;
import MyMain.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class ReaderSearchController implements Initializable {
    @FXML
    private Button searchB, cancelB;

    @FXML
    private TextField idTf, nameTf, phoneTf, idCardTf;

    private ObservableList<Map.Entry<Integer, Reader>> list = FXCollections.observableArrayList();


    public void search(ActionEvent event){

        ReaderController.users.clear();
        String name ='%'+ nameTf.getText() +'%';
        String idCard ='%'+ idCardTf.getText() +'%';
        String phone ='%'+ phoneTf.getText() +'%';
        String id;
        try{
            id = "and id = " + Integer.parseInt(idTf.getText());
        }catch (NumberFormatException e){
            id = "";
        }

        try {
            String sql;
            sql = String.format("select id from [Users] where name like N'%s' and " +
                            "idcard like N'%s' and phone like N'%s' ",
                    name, idCard, phone) + id;
            ResultSet rs;
            System.out.println(sql);
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                Integer rId = rs.getInt(1);
                ReaderController.users.add(Map.entry(rId, ReaderController.userMap.get(rId)));
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        BookController.books = result;
    }
    public void cancel(ActionEvent event){
        ReaderController.users.clear();
        ReaderController.users.addAll(list);
        Main.secondaryStage.close();
        idTf.setText("");
        nameTf.setText("");
        phoneTf.setText("");
        idCardTf.setText("");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list = FXCollections.observableArrayList(ReaderController.users);
    }
}
