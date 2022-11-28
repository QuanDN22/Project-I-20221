package MyController;

import MyClass.Book;
import MyClass.Title;
import MyMain.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BookController implements Initializable {
    @FXML
    private Button buttonAdd, buttonRemove, buttonNew, buttonDelete, buttonEdit,
            buttonCancel, buttonUpdate, buttonBack, buttonSearch;
    @FXML
    private TableView<Map.Entry<String, Title>> titleTable;
    @FXML
    private TableColumn<Map.Entry<String, Title>, String> tCodeCol, nameCol, categoryCol, authorCol, publisherCol, shelfCol;
    @FXML
    private TableColumn<Map.Entry<String, Title>, Number>  yearCol, amountCol, availableCol;
    @FXML
    private TableView<Map.Entry<Integer, Book>> bookTable;
    @FXML
    private TableColumn<Map.Entry<Integer, Book>, String> bCodeCol, statusCol;
    @FXML
    private TableColumn<Map.Entry<Integer, Book>, Number> idCol;
    @FXML
    private TextField codeTf, nameTf, authorTf, publisherTf, yearTf, amountTf, availableTf, shelfTf, idTf;
    @FXML
    private ComboBox<String> categoryCb;
    private Mode mode = Mode.DO_NOTHING;

    public static Map<String, Title> titleMap = new HashMap<>();
    public static ObservableList<Map.Entry<String, Title>> titles = FXCollections.observableArrayList();
    public static Map<Integer, Book> bookMap = new HashMap<>();
    public static ObservableList<Map.Entry<Integer, Book>> books = FXCollections.observableArrayList();
    public static final ObservableList<String> Category = FXCollections.observableArrayList("None");
    private String code, name, category, author, publisher, shelf;
    private int year, amount, available;


    @FXML
    private void back(ActionEvent event){
        MenuController.stages[0].close();
    }
    @FXML
    private void search(ActionEvent event){
        changeBMode(true);
        changeTfMode(false);
        amountTf.setDisable(true);
        availableTf.setDisable(true);
        mode = Mode.SEARCH_TITLE;
    }
    @FXML
    private void select(){

        if (mode != Mode.NEW_TITLE && titleTable.getSelectionModel().getSelectedItem() != null) {

            System.out.println(titleTable.getSelectionModel().getSelectedIndex());
            setTitleInfo(titleTable.getSelectionModel().getSelectedItem().getValue());
            if (mode == Mode.ADD_BOOK) amountTf.setText("0");
            getTitleInfo();

            System.out.println(titleTable.getSelectionModel().getSelectedItem().getValue().getCode());
            System.out.println(code);
            try {
                bookMap.clear();
                ResultSet rs = Main.statement.executeQuery(
                        String.format("select * from [Books] where code = '%s'", code)
                );
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String code = rs.getString(2);
                    String status = rs.getString(3);
                    bookMap.put(id, new Book(id, code, status));
                }
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            books.setAll(bookMap.entrySet());
            bookTable.getItems().setAll(bookMap.entrySet());
            bookTable.getSelectionModel().select(0);
        }
    }

    @FXML
    private void addBook(ActionEvent event){
        changeBMode(true);
        changeTfMode(true);
        codeTf.setDisable(false);
        amountTf.setDisable(false);
        amountTf.setText("0");
        mode = Mode.ADD_BOOK;
    }
    @FXML
    private void removeBook(ActionEvent event){
        changeBMode(true);
        changeTfMode(true);
        mode = Mode.REMOVE_BOOK;
    }
    @FXML
    private void newTitle(ActionEvent event){
        if(!MenuController.stages[0].isShowing()){
            setBlankTf();
            changeTfMode(false);
            changeBMode(true);
            categoryCb.setEditable(true);
            availableTf.setDisable(true);
            mode = Mode.NEW_TITLE;
        } else MenuController.stages[0].toFront();
    }
    @FXML
    private void editTitle(ActionEvent event){
        changeTfMode(false);
        changeBMode(true);
        amountTf.setDisable(true);
        availableTf.setDisable(true);
        mode = Mode.EDIT_TITLE;
    }
    @FXML
    private void deleteTitle(ActionEvent event){
        changeTfMode(true);
        changeBMode(true);
        mode = Mode.DELETE_TITLE;
    }
    @FXML
    private void cancel(ActionEvent event){
        changeTfMode(true);
        changeBMode(false);
        categoryCb.setEditable(false);
        mode = Mode.DO_NOTHING;
        updateTitleTable();
    }
    @FXML
    private void update(ActionEvent event){
        switch (mode){
            case ADD_BOOK -> addB();
            case REMOVE_BOOK -> removeB();
            case NEW_TITLE -> newT();
            case DELETE_TITLE -> deleteT();
            case EDIT_TITLE -> editT();
            case SEARCH_TITLE ->searchT();
        }
    }

    private void changeTfMode(boolean b) {
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
    private void changeBMode(boolean b){
        buttonBack.setDisable(b);
        buttonSearch.setDisable(b);
        buttonNew.setDisable(b);
        buttonRemove.setDisable(b);
        buttonAdd.setDisable(b);
        buttonDelete.setDisable(b);
        buttonEdit.setDisable(b);
        buttonCancel.setVisible(b);
        buttonUpdate.setVisible(b);
    }
    private void setBlankTf(){
        codeTf.setText("");
        nameTf.setText("");
        publisherTf.setText("");
        authorTf.setText("");
        categoryCb.setValue("");
        yearTf.setText("");
        amountTf.setText("");
        availableTf.setText("");
        shelfTf.setText("");
    }
    private void getTitleInfo(){
        code = codeTf.getText();
        name = nameTf.getText();
        category = categoryCb.getValue();
        if(category.contentEquals("")) category = "None";
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
    private void setTitleInfo(@NotNull Title title){
        codeTf.setText(title.getCode());
        nameTf.setText(title.getName());
        categoryCb.setValue(title.getCategory());
        authorTf.setText(title.getAuthor());
        publisherTf.setText(title.getPublisher());
        yearTf.setText(String.valueOf(title.getYear()));
        amountTf.setText(String.valueOf(title.getAmount()));
        availableTf.setText(String.valueOf(title.getAvailable()));
        shelfTf.setText(title.getShelf());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonUpdate.setVisible(false);
        buttonCancel.setVisible(false);

        tCodeCol.setCellValueFactory(p->p.getValue().getValue().codeProperty());
        nameCol.setCellValueFactory(p->p.getValue().getValue().nameProperty());
        categoryCol.setCellValueFactory(p->p.getValue().getValue().categoryProperty());
        authorCol.setCellValueFactory(p->p.getValue().getValue().authorProperty());
        publisherCol.setCellValueFactory(p->p.getValue().getValue().publisherProperty());
        yearCol.setCellValueFactory(p->p.getValue().getValue().yearProperty());
        amountCol.setCellValueFactory(p->p.getValue().getValue().amountProperty());
        availableCol.setCellValueFactory(p->p.getValue().getValue().availableProperty());
        shelfCol.setCellValueFactory(p->p.getValue().getValue().shelfProperty());

        idCol.setCellValueFactory(p->p.getValue().getValue().idProperty());
        bCodeCol.setCellValueFactory(p->p.getValue().getValue().codeProperty());
        statusCol.setCellValueFactory(p->p.getValue().getValue().statusProperty());

        updateTitleTable();
        titleTable.setItems(titles);
        categoryCb.setItems(Category);
        titleTable.getSelectionModel().select(0);
        select();
        for(Title b: titleMap.values()){
            String code = b.getCode();
            int amount = b.getAmount();
            for(int i = 1; i <= amount; i++){
                    System.out.println(code);
            }
        }
    }
    public static void updateTitleTable(){
        try {
            titleMap.clear();
            String sql;
            sql = "select a.*, b.Available " +
                    "from( select t.Code, Name, Category, Author, Publisher, t.Year, t.Shelf, count(id) 'Amount' " +
                    "from Titles t inner join Books b on t.Code = b.Code " +
                    "group by t.Code, Name, Category, Author, Publisher, t.Year, t.Shelf) a " +
                    "join(select t.Code, count(id) 'Available' " +
                    "from Titles t inner join Books b on t.Code = b.Code " +
                    "where status ='available' group by t.Code) b " +
                    "on a.Code = b.Code";
            ResultSet rs;
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                String code = rs.getString(1);
                String name = rs.getString(2);
                String category = rs.getString(3);
                String author = rs.getString(4);
                String publisher = rs.getString(5);
                int year = rs.getInt(6);
                String shelf = rs.getString(7);
                int amount = rs.getInt(8);
                int available = rs.getInt(9);
                titleMap.put(code, new Title(code, name, category, author, publisher, year, amount, available, shelf));
            }
            rs = Main.statement.executeQuery(
                    "select category from [Titles] group by category"
            );
            Category.clear();
            while (rs.next()){
                Category.add(rs.getString(1));
            }
            rs.close();
            titles.setAll(titleMap.entrySet());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void updateBookTable() throws SQLException {
        ResultSet rs = Main.statement.executeQuery(
                String.format("select id, status from [Books] where code = '%s'", code)
        );
        bookMap.clear();
        while (rs.next()){
            int id = rs.getInt(1);
            String status = rs.getString(2);
            System.out.println(id);
            bookMap.put(id, new Book(id,code,status));
        }
    }
    private void searchT() {
        titles.clear();
        String code = '%' + codeTf.getText() + '%';
        String name = '%' + nameTf.getText() + '%';
        String category = categoryCb.getValue();
        if (category == null || category.contentEquals("None")) category = "";
        category = '%' + category + '%';
        String author = '%' + authorTf.getText() + '%';
        String publisher = '%' + publisherTf.getText() + '%';
        String year;
        try {
            year = "and year = " + Integer.parseInt(yearTf.getText());
        } catch (NumberFormatException e) {
            year = "";
        }
        String shelf = '%' + shelfTf.getText() + '%';

        try {
            String sql;
            sql = String.format("select code from [Titles] t " +
                            "where name like N'%s' and category like N'%s' and " +
                            "author like N'%s' and publisher like N'%s' and " +
                            "code like '%s' and shelf like '%s'",
                    name, category, author, publisher, code, shelf) + year;
            ResultSet rs;
            System.out.println(sql);
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                String rCode = rs.getString(1);
               titles.add(Map.entry(rCode, titleMap.get(rCode)));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            throw new RuntimeException(e);
        }
    }
    private void addB() {
        getTitleInfo();
        if(titleMap.containsKey(code) && amount > 0){
            try {
                for(int i = 0; i < amount; i++){
                    Main.statement.executeUpdate(
                            String.format("insert into [Books](Code, Status) values('%s','available')", code)
                    );
                    System.out.println("Book is added");
                }
                updateBookTable();
            } catch (SQLException e) {
                System.out.println("SQL Exception");
                throw new RuntimeException(e);
            }
        }
        cancel(new ActionEvent());
    }

    private void removeB() {
        getTitleInfo();
        Title title = titleMap.get(code);
        title.setAmount(title.getAmount()-1);
        title.setAvailable(title.getAvailable()-1);
        try {
            Main.statement.executeUpdate(String.format("delete from [Books] where id = %d",
                    bookTable.getSelectionModel().getSelectedItem().getValue().getId()));
            System.out.println("Book is added");
            updateBookTable();

        } catch (SQLException e) {
            System.out.println("SQL Exception");
            throw new RuntimeException(e);
        }
        cancel(new ActionEvent());
    }
    private void newT(){
        getTitleInfo();
        Title title = new Title(code, name, category, author, publisher, year, amount, amount, shelf);
        System.out.println(category);
        if(title.isNew() && title.isValid()) {
            try {
                String sql;
                sql = String.format("insert into [Titles](Code, Name, Category, Author," +
                                " Publisher, Year, Shelf)" +
                                " values('%s',N'%s',N'%s',N'%s',N'%s',%d,'%s')",
                        code, name, category, author, publisher, year, shelf);
                System.out.println(sql);
                Main.statement.executeUpdate(sql);
                for(int i = 0; i < amount; i++){
                    Main.statement.executeUpdate(
                            String.format("insert into [Books](Code, Status) values('%s','available')", code)
                    );
                    System.out.println("Book is added");

                }
//                System.out.println("New title is created");
//                if(!Category.contains(category)) Category.add(category);
//                titleMap.put(code, title);
//                select(titles.size()-1);
//                titles.setAll(titleMap.entrySet());
            } catch (SQLException e) {
                System.out.println("SQL Exception");
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Title is invalid");

        }
        cancel(new ActionEvent());
    }
    private void deleteT(){
        String code = codeTf.getText();
        try {
            Main.statement.executeUpdate(String.format("delete from [Books] where code = '%s'", code));
            Main.statement.executeUpdate(String.format("delete from [Titles] where Code = '%s' ", code));
            System.out.println("Title is deleted!");
//            titleMap.remove(code);
//            select();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e){
            System.out.println("SQL Exception");
            throw new RuntimeException(e);
        }
        cancel(new ActionEvent());
    }
    private void editT(){
        getTitleInfo();
        String pCode = titleTable.getSelectionModel().getSelectedItem().getValue().getCode();
        Title title = new Title(code, name, category, author, publisher, year, amount, available, shelf);
        if(title.isValid()) {
            try {
                String sql;
                sql = String.format("update [Titles] set Code = '%s', Name = N'%s', Category = N'%s', " +
                        "Author = N'%s', Publisher = N'%s', Year = %d where Code = '%s' "
                        ,code, name, category, author, publisher, year, pCode);
                if(Main.statement.executeUpdate(sql) > 0) System.out.println("Edit Successfully!");
                else  System.out.println("Book is edited");
//                int i = titleTable.getSelectionModel().getSelectedIndex();
//                if(!pCode.equals(code)) {
//                    titles.remove(i);
//                    titleMap.remove(pCode);
//                    titleMap.put(code, title);
//                    titles.add(Map.entry(code, title));
//                    select(titles.size()-1);
//                } else {
//                    titleMap.get(code).setBook(title);
//                    titleTable.refresh();
//                }
            } catch (SQLException e) {
                System.out.println("SQL Exception");
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Title is invalid!");
        }
        cancel(new ActionEvent());
    }
    private enum Mode{
        DO_NOTHING, ADD_BOOK, REMOVE_BOOK, NEW_TITLE, DELETE_TITLE, SEARCH_TITLE, EDIT_TITLE
    }
}
