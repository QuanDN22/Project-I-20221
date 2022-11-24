package MyController;

import MyClass.Reader;
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
import javafx.scene.input.MouseEvent;

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

    public void back(ActionEvent event) {
        if(!Main.secondaryStage.isShowing()) Main.primaryStage.setScene(Main.menuScene);
        else Main.secondaryStage.toFront();
    }
    public void selectItem(){
        if(readerTable.getSelectionModel().getSelectedItem() != null) {
            System.out.println("Select successfully!");
            Reader selected = readerTable.getSelectionModel().getSelectedItem().getValue();

            idTf.setText(String.valueOf(selected.getId()));
            nameTf.setText(selected.getName());
            birthTf.setText(selected.getBirthdate());
            idCardTf.setText(selected.getIdcard());
            phoneTf.setText(selected.getPhone());
            expiryTf.setText(selected.getExpirydate());
            addressTf.setText(selected.getAddress());
        }
    }
    public void search(ActionEvent event){
        Main.secondaryStage.setTitle("Search");
        Main.secondaryStage.setScene(Main.readerSearchScene);
        Main.secondaryStage.show();
    }
    public void clickOnStage(MouseEvent event){
        Main.primaryStage.toFront();
    }
    public void edit(ActionEvent event){
        changeDisable();
        backB.setVisible(false);
        editB.setVisible(false);
        deleteB.setVisible(false);
        searchB.setVisible(false);
        cancelB.setVisible(true);
        updateB.setVisible(true);
    }
    public void update(ActionEvent event){

        String name = nameTf.getText();
        String birth = birthTf.getText();
        if (birth.contentEquals("")) birth = "0000-00-00";
        String phone = phoneTf.getText();
        if(phone.contentEquals("")) phone = "0000-000-000";
        String expiry = expiryTf.getText();
        if (expiry.contentEquals("")) expiry = "0000-00-00";
        String idCard = idCardTf.getText();
        if (idCard.contentEquals("")) idCard = "00000-00000-0000";
        String address = addressTf.getText();
        int id = Integer.parseInt(idTf.getText());


        Reader reader = new Reader(id,"","", name, birth, idCard, phone, expiry, address);
        if(reader.isValid()) {
            try {
                String sql;
                sql = String.format("update [Users] set name = N'%s', birthdate = '%s', " +
                        "idcard = '%s', phone = '%s', expirydate = '%s', address = N'%s' " +
                        "where id = %d ", name, birth, idCard, phone, expiry, address, id);
                if(Main.statement.executeUpdate(sql) > 0) System.out.println("Edit Successfully!");
                else  System.out.println("User is edited");
                readerTable.getSelectionModel().getSelectedItem().getValue().setReader(reader);
                readerTable.refresh();
//                books.remove(0);
//                status.setText("Register successfully!");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("User is invalid!");
//            status.setText("Register unsuccessfully!");
        }

        cancel(event);
    }
    public void delete(ActionEvent event){
        Integer id = Integer.parseInt(idTf.getText());
        try {
            String sql;
            sql = String.format("delete from [Users] where id = %d ", id);
            if (Main.statement.executeUpdate(sql) > 0) System.out.println("User is deleted!");
            userMap.remove(id);
            users.remove(readerTable.getSelectionModel().getSelectedIndex());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e){
            System.out.println("select nothing");
        }
        selectItem();
    }
    public void cancel(ActionEvent event){
        updateB.setVisible(false);
        cancelB.setVisible(false);
        deleteB.setVisible(true);
        searchB.setVisible(true);
        editB.setVisible(true);
        backB.setVisible(true);
        changeDisable();
        idTf.setDisable(true);
        selectItem();
    }
    public void changeDisable(){
        nameTf.setDisable(!nameTf.isDisable());
        birthTf.setDisable(!birthTf.isDisable());
        phoneTf.setDisable(!phoneTf.isDisable());
        expiryTf.setDisable(!expiryTf.isDisable());
        idCardTf.setDisable(!idCardTf.isDisable());
        addressTf.setDisable(!addressTf.isDisable());
    }
    public void setBlankTF(){
        nameTf.setText("");
        birthTf.setText("");
        phoneTf.setText("");
        expiryTf.setText("");
        idCardTf.setText("");
        addressTf.setText("");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateB.setVisible(false);
        cancelB.setVisible(false);

        idCol.setCellValueFactory(p-> p.getValue().getValue().idProperty());
        nameCol.setCellValueFactory(p->p.getValue().getValue().nameProperty());
        birthCol.setCellValueFactory(p->p.getValue().getValue().birthdateProperty());
        idCardCol.setCellValueFactory(p->p.getValue().getValue().idcardProperty());
        phoneCol.setCellValueFactory(p->p.getValue().getValue().phoneProperty());
        expiryCol.setCellValueFactory(p->p.getValue().getValue().expirydateProperty());
        addressCol.setCellValueFactory(p->p.getValue().getValue().addressProperty());
        try {
            String sql;
            sql = "select * from [Users]";
            ResultSet rs;
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                int rId = rs.getInt(1);
                String rUsername = rs.getString(2);
                String rPassword = rs.getString(3);
                String rName = rs.getString(4);
                String rBirth = rs.getString(5);
                String rIdCard = rs.getString(6);
                String rPhone = rs.getString(7);
                String rExpiry = rs.getString(8);
                String rAddress = rs.getString(9);
                Reader reader = new Reader(rId, rUsername, rPassword, rName, rBirth, rIdCard, rPhone, rExpiry, rAddress);
                userMap.put(rId, reader);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        users.addAll(userMap.entrySet());
        readerTable.setItems(users);
    }
}
