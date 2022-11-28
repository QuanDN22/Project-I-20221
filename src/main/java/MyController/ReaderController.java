package MyController;

import MyClass.Book;
import MyClass.Reader;
import MyClass.Title;
import MyMain.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ReaderController implements Initializable {
    @FXML
    private Button updateB, deleteB, searchB, backB, cancelB, editB;
    @FXML
    private TableView<Map.Entry<Integer, Reader>> readerTable;
    @FXML
    private TableColumn<Map.Entry<Integer, Reader>, String> nameCol,birthCol, idCardCol, phoneCol, expiryCol, addressCol;
    @FXML
    private TableColumn<Map.Entry<Integer, Reader>, Number> idCol;
    @FXML
    private TextField idTf, nameTf, birthTf, idCardTf, phoneTf, expiryTf, addressTf;
    public static Map<Integer, Reader> userMap = new HashMap<>();
    public static ObservableList<Map.Entry<Integer, Reader>> users = FXCollections.observableArrayList();
    private Mode mode = Mode.DO_NOTHING;
    private int id;
    private String name, birth, idCard, phone, expiry, address;


    private enum Mode{
        DO_NOTHING, EDIT_READER, DELETE_READER, SEARCH_READER
    }
    @FXML
    private void back(ActionEvent event){
        MenuController.stages[1].close();
    }
    @FXML
    private void search(ActionEvent event){
        changeBMode(true);
        changeTfMode(false);
        birthTf.setDisable(true);
        expiryTf.setDisable(true);
        addressTf.setDisable(true);
        mode = Mode.SEARCH_READER;
    }
    @FXML
    private void select(){
        if (readerTable.getSelectionModel().getSelectedItem() != null) {
            System.out.println(readerTable.getSelectionModel().getSelectedIndex());
            setReaderInfo(readerTable.getSelectionModel().getSelectedItem().getValue());
            getReaderInfo();
            System.out.println(readerTable.getSelectionModel().getSelectedItem());
        }
    }
    @FXML
    private void editReader(ActionEvent event){
        changeTfMode(false);
        changeBMode(true);
        mode = Mode.EDIT_READER;
    }
    @FXML
    private void deleteReader(ActionEvent event){
        changeTfMode(true);
        changeBMode(true);
        mode = Mode.DELETE_READER;
    }
    @FXML
    private void cancel(ActionEvent event){
        changeTfMode(true);
        changeBMode(false);
        mode = Mode.DO_NOTHING;
        updateReaderTable();
    }
    @FXML
    private void update(ActionEvent event){
        switch (mode){
            case EDIT_READER -> editR();
            case DELETE_READER -> deleteR();
            case SEARCH_READER -> searchR();
        }
    }

    private void changeTfMode(boolean b) {
        idTf.setDisable(b);
        nameTf.setDisable(b);
        birthTf.setDisable(b);
        idCardTf.setDisable(b);
        phoneTf.setDisable(b);
        expiryTf.setDisable(b);
        addressTf.setDisable(b);
    }
    private void changeBMode(boolean b){
        searchB.setDisable(b);
        editB.setDisable(b);
        deleteB.setDisable(b);
        updateB.setVisible(b);
        cancelB.setVisible(b);
    }

    private void getReaderInfo(){
        try{

            id = Integer.parseInt(idTf.getText());
        }catch (NumberFormatException e){
            id = 0;
        }
        name = nameTf.getText();
        birth = birthTf.getText();
        idCard = idCardTf.getText();
        phone = phoneTf.getText();
        expiry = expiryTf.getText();
        address = addressTf.getText();
    }
    private void setReaderInfo(@NotNull Reader reader){
        idTf.setText(String.valueOf(reader.getId()));
        nameTf.setText(reader.getName());
        birthTf.setText(reader.getBirthdate());
        idCardTf.setText(reader.getIdcard());
        phoneTf.setText(reader.getPhone());
        expiryTf.setText(reader.getExpirydate());
        addressTf.setText(reader.getAddress());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateB.setVisible(false);
        cancelB.setVisible(false);

        idCol.setCellValueFactory(p->p.getValue().getValue().idProperty());
        nameCol.setCellValueFactory(p->p.getValue().getValue().nameProperty());
        birthCol.setCellValueFactory(p->p.getValue().getValue().birthdateProperty());
        idCardCol.setCellValueFactory(p->p.getValue().getValue().idcardProperty());
        phoneCol.setCellValueFactory(p->p.getValue().getValue().phoneProperty());
        expiryCol.setCellValueFactory(p->p.getValue().getValue().expirydateProperty());
        addressCol.setCellValueFactory(p->p.getValue().getValue().addressProperty());

        updateReaderTable();
        readerTable.setItems(users);
        readerTable.getSelectionModel().select(0);
        select();

    }
    public static void updateReaderTable(){
        try {
            userMap.clear();
            String sql;
            sql = "select * from [Users]";
            ResultSet rs;
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String username = rs.getString(2);
                String password = rs.getString(3);
                String name = rs.getString(4);
                String birth = rs.getString(5);
                String idCard = rs.getString(6);
                String phone = rs.getString(7);
                String expiry = rs.getString(8);
                String address = rs.getString(9);

                userMap.put(id, new Reader(id,username,
                        password,name,birth,idCard, phone , expiry, address ));
            }
            users.setAll(userMap.entrySet());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void searchR() {

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
    }


    private void deleteR(){
        String id = idTf.getText();
        try {
            Main.statement.executeUpdate(String.format("delete from [Users] where id = %s", id));
            System.out.println("User is deleted!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e){
            System.out.println("SQL Exception");
            throw new RuntimeException(e);
        }
        cancel(new ActionEvent());
    }
    private void editR(){
        getReaderInfo();
        Reader reader = new Reader(id, "0", "0",name, birth, idCard, phone, expiry, address);
        if(reader.isValid()) {
            try {
                String sql;
                sql = String.format("update [Users] set name = N'%s', birthdate = '%s', idcard = '%s', " +
                                "phone = '%s', expirydate = '%s', address = N'%s' where id = %d"
                        ,name, birth, idCard, phone, expiry, address, id);
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

}
