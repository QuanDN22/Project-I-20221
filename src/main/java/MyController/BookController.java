package MyController;

import MyClass.Book;
import MyMain.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BookController implements Initializable {
    @FXML
    private Button addB, editB, deleteB, searchB, backB, updateB, cancelB, newB;
    @FXML
    private TableView<Map.Entry<String,Book>> bookTable;
    @FXML
    private TableColumn<Map.Entry<String,Book>, String> codeCol, nameCol, categoryCol, authorCol, publisherCol, shelfCol;
    @FXML
    private TableColumn<Map.Entry<String,Book>, Number>  yearCol, amountCol, availableCol;
    @FXML
    private TextField codeTf, nameTf, authorTf, publisherTf, yearTf, amountTf, availableTf, shelfTf;
    @FXML
    private ComboBox<String> categoryCb;
    private boolean selectable = true;

    public static Map<String, Book> bookMap = new HashMap<>();
    public static ObservableList<Map.Entry<String, Book>> books = FXCollections.observableArrayList();
    public static final ObservableList<String> Category = FXCollections.observableArrayList(
            "None", "Magazine", "Science", "Music");
    private String code, name, category, author, publisher, shelf;
    private int year, amount, available;


    public void back(ActionEvent event){
        if(!Main.secondaryStage.isShowing()) Main.primaryStage.setScene(Main.menuScene);
        else Main.secondaryStage.toFront();
    }
    public void search(ActionEvent event){
//        searchStage.toFront();
        Main.secondaryStage.setTitle("Search");
        Main.secondaryStage.setScene(Main.bookSearchScene);
        Main.secondaryStage.show();
    }
    public void selectItem(){
        if(selectable && bookTable.getSelectionModel().getSelectedItem() != null) {
            Book selected = bookTable.getSelectionModel().getSelectedItem().getValue();
            System.out.println(bookTable.getSelectionModel().getSelectedIndex());
            codeTf.setText(selected.getCode());
            nameTf.setText(selected.getName());
            categoryCb.setValue(selected.getCategory());
            authorTf.setText(selected.getAuthor());
            publisherTf.setText(selected.getPublisher());
            yearTf.setText(String.valueOf(selected.getYear()));
            amountTf.setText(String.valueOf(selected.getAmount()));
            availableTf.setText(String.valueOf(selected.getAvailable()));
            shelfTf.setText(selected.getShelf());
        }
    }
    public void newBook(){
        if(!Main.secondaryStage.isShowing()){
            setBlankTF();

            changeDisable(false);
            availableTf.setDisable(true);
            codeTf.setDisable(false);
            backB.setVisible(false);
            editB.setVisible(false);
            newB.setVisible(false);
            deleteB.setVisible(false);
            searchB.setVisible(false);
            cancelB.setVisible(true);
            updateB.setVisible(false);
            addB.setVisible(true);
            selectable = false;
        } else Main.secondaryStage.toFront();
    }
    public void add(ActionEvent event){
        getBook();
        Book book = new Book(code, name, category, author, publisher, year, amount, amount, shelf);
        if(book.isNew() && book.isValid()) {
            try {
                String sql;
                sql = String.format("insert into [Titles](Code, Name, Category, Author," +
                                " Publisher, Year, Amount, Available, Shelf)" +
                        " values('%s',N'%s',N'%s',N'%s',N'%s',%d,%d,%d,'%s')",
                        code, name, Category, author, publisher, year, amount, amount, shelf);
                Main.statement.executeUpdate(sql);
                System.out.println("Book is added");
                books.add(Map.entry(code,book));
                bookMap.put(code, book);
                selectIndex(books.size()-1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Book is invalid");

        }
        cancel(event);
    }
    public void edit(ActionEvent event){

        changeDisable(false);
        backB.setVisible(false);
        editB.setVisible(false);
        newB.setVisible(false);
        deleteB.setVisible(false);
        searchB.setVisible(false);
        cancelB.setVisible(true);
        updateB.setVisible(true);
        addB.setVisible(false);
    }
    public void update(ActionEvent event){
        getBook();
        String pCode = bookTable.getSelectionModel().getSelectedItem().getValue().getCode();
        Book book = new Book(code, name, category, author, publisher, year, amount, available, shelf);
        if(book.isValid()) {
            try {
                String sql;
                sql = String.format("update [Titles] set Code = '%s', Name = N'%s', Category = N'%s', " +
                        "Author = N'%s', Publisher = N'%s', Year = %d, Amount = %d, Available = %d" +
                        "where Code = '%s' ",code, name, category, author, publisher, year, amount, available, pCode);
                if(Main.statement.executeUpdate(sql) > 0) System.out.println("Edit Successfully!");
                else  System.out.println("Book is edited");
                int i = bookTable.getSelectionModel().getSelectedIndex();
                if(!pCode.equals(code)) {
                    books.remove(i);
                    bookMap.remove(pCode);
                    bookMap.put(code, book);
                    books.add(Map.entry(code, book));
                    selectIndex(books.size()-1);
                } else {
                    bookMap.get(code).setBook(book);
                    bookTable.refresh();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Book is invalid!");
        }
        cancel(event);
    }
    public void cancel(ActionEvent event){
        updateB.setVisible(false);
        cancelB.setVisible(false);
        addB.setVisible(false);
        deleteB.setVisible(true);
        searchB.setVisible(true);
        editB.setVisible(true);
        newB.setVisible(true);
        backB.setVisible(true);
        changeDisable(true);
        codeTf.setDisable(true);
        selectable = true;
    }
    public void delete(ActionEvent event){
        String code = codeTf.getText();
        try {
            String sql;
            sql = String.format("delete from [Titles] where Code = '%s' ", code);
            if (Main.statement.executeUpdate(sql) > 0) System.out.println("Book is deleted!");
            int i = bookTable.getSelectionModel().getSelectedIndex();
            books.remove(i);
            selectIndex(i);
            bookMap.remove(code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e){
            System.out.println("select nothing");
        }
    }

    private void selectIndex(int x) {
        bookTable.getSelectionModel().select(x);
        if(bookTable.getSelectionModel().getSelectedItem() != null) {
            Book selected = bookTable.getSelectionModel().getSelectedItem().getValue();
            System.out.println(bookTable.getSelectionModel().getSelectedIndex());
            codeTf.setText(selected.getCode());
            nameTf.setText(selected.getName());
            categoryCb.setValue(selected.getCategory());
            authorTf.setText(selected.getAuthor());
            publisherTf.setText(selected.getPublisher());
            yearTf.setText(String.valueOf(selected.getYear()));
            amountTf.setText(String.valueOf(selected.getAmount()));
            availableTf.setText(String.valueOf(selected.getAvailable()));
            shelfTf.setText(selected.getShelf());
        }
    }

    public void changeDisable(boolean b){
        codeTf.setDisable(b);
        nameTf.setDisable(b);
        publisherTf.setDisable(b);
        categoryCb.setDisable(b);
        authorTf.setDisable(b);
        yearTf.setDisable(b);
        amountTf.setDisable(b);
        availableTf.setDisable(b);
        shelfTf.setDisable(b);
    }
    public void setBlankTF(){
        codeTf.setText("");
        nameTf.setText("");
        publisherTf.setText("");
        authorTf.setText("");
        categoryCb.setValue("None");
        yearTf.setText("");
        amountTf.setText("");
        availableTf.setText("");
        shelfTf.setText("");
    }
    public void getBook(){
        code = codeTf.getText();
        name = nameTf.getText();
        category = categoryCb.getValue();
        author = authorTf.getText();
        publisher = publisherTf.getText();
        try{
            year = Integer.parseInt(yearTf.getText());
        }catch (NumberFormatException e){
            year = 0;
        }
        try{
            amount = Integer.parseInt(amountTf.getText());
        }catch (NumberFormatException e){
            amount = 0;
        }
        try{
            available = Integer.parseInt(amountTf.getText());
        }catch (NumberFormatException e){
            available = 0;
        }
        shelf = shelfTf.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateB.setVisible(false);
        cancelB.setVisible(false);
        addB.setVisible(false);

        codeCol.setCellValueFactory(p->p.getValue().getValue().codeProperty());
        nameCol.setCellValueFactory(p->p.getValue().getValue().nameProperty());
        categoryCol.setCellValueFactory(p->p.getValue().getValue().categoryProperty());
        authorCol.setCellValueFactory(p->p.getValue().getValue().authorProperty());
        publisherCol.setCellValueFactory(p->p.getValue().getValue().publisherProperty());
        yearCol.setCellValueFactory(p->p.getValue().getValue().yearProperty());
        amountCol.setCellValueFactory(p->p.getValue().getValue().amountProperty());
        availableCol.setCellValueFactory(p->p.getValue().getValue().availableProperty());
        shelfCol.setCellValueFactory(p->p.getValue().getValue().shelfProperty());

        try {
            String sql;
            sql = "select * from [Titles]";
            ResultSet rs;
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                String code = rs.getString(1);
                String name = rs.getString(2);
                String category = rs.getString(3);
                String author = rs.getString(4);
                String publisher = rs.getString(5);
                int year = rs.getInt(6);
                int amount = rs.getInt(7);
                int available = rs.getInt(8);
                String shelf = rs.getString(9);
                Book book = new Book(code, name, category, author, publisher, year, amount, available, shelf);
                bookMap.put(code,book);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        books.addAll(bookMap.entrySet());
        bookTable.setItems(books);

        bookTable.getSortOrder().add(codeCol);
        selectIndex(0);
        categoryCb.setItems(Category);
        for(Book b: bookMap.values()){
            String code = b.getCode();
            int amount = b.getAmount();
            for(int i = 1; i <= amount; i++){
                    System.out.println(code);
            }
        }
    }
}
