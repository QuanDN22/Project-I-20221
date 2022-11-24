package MyController;

import MyClass.Book;
import MyMain.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class BookSearchController implements Initializable {
    @FXML
    private Button searchB, cancelB;
    @FXML
    private ComboBox<String> categoryCb;
    @FXML
    private TextField codeTf, nameTf, authorTf, publisherTf, yearTf, shelfTf;

    private ObservableList<Map.Entry<String, Book>> list;


    public void search(ActionEvent event){

        BookController.books.clear();
        String code = '%'+ codeTf.getText() +'%';
        String name ='%'+ nameTf.getText() +'%';
        String category = categoryCb.getValue();
        if(category == null || category.contentEquals("None")) category = "";
        category = '%'+ category+ '%';
        String author ='%'+ authorTf.getText() +'%';
        String publisher ='%'+ publisherTf.getText() +'%';
        String year;
        try{
            year = "and year = " + Integer.parseInt(yearTf.getText());
        }catch (NumberFormatException e){
            year = "";
        }
        String shelf ='%'+ shelfTf.getText() +'%';
        try {
            String sql;
            sql = String.format("select code from [Titles] where name like N'%s' and category like N'%s' and " +
                    "author like N'%s' and publisher like N'%s' and code like '%s' and shelf like '%s'",
                    name, category, author, publisher, code, shelf) + year;
            ResultSet rs;
            System.out.println(sql);
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                String rCode = rs.getString(1);
                BookController.books.add(Map.entry(rCode, BookController.bookMap.get(rCode)));
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        BookController.books = result;
    }
    public void cancel(ActionEvent event){
//        BookController.bookTable.setItems(BookController.books);
        BookController.books.clear();
        BookController.books.addAll(list);
        Main.secondaryStage.close();
        codeTf.setText("");
        nameTf.setText("");
        publisherTf.setText("");
        authorTf.setText("");
        categoryCb.getEditor().setText("None");
        yearTf.setText("");

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list = FXCollections.observableArrayList(BookController.books);
        categoryCb.setItems(BookController.Category);
    }

}
