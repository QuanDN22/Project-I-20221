package MyController;

import MyClass.Ticket;
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
import java.util.ResourceBundle;

public class CallController implements Initializable {

    @FXML
    private TextField bookIDTf, nameTf,  categoryTf, authorTf, publisherTf,
            codeTf, yearTf, shelfTf, statusTf, readerIDTf,  bDateTf, aDateTf, uDateTf;
    @FXML
    private Button checkB, backB, updateB, borrowB, returnB, extendB, penalizeB, cancelB;
    @FXML
    private Label returnL, extendL, penaltyL;
    @FXML
    private TableView<Ticket> tTable;
    @FXML
    private TableColumn<Ticket, Number> idCol, bookIdCol, readerIdCol;
    @FXML
    private TableColumn<Ticket, String> bDateCol, aDateCol, eDateCol, rDateCol, pDateCol;

    private static final ObservableList<Ticket> tickets = FXCollections.observableArrayList();

    private Mode mode = Mode.BORROW_MODE;

    @FXML
    private void back(ActionEvent event){
        MenuController.stages[2].close();
    }

    private void returnBook() throws SQLException {
        int bookId;
        try{
            bookId = Integer.parseInt(bookIDTf.getText());
        }catch (NumberFormatException e){
            bookId = 0;
        }
        int readerId;
        try{
            readerId = Integer.parseInt(readerIDTf.getText());
        }catch (NumberFormatException e){
            readerId = 0;
        }
        String rDate = uDateTf.getText();
        Ticket ticket = new Ticket(readerId, bookId);
        ticket.setRDate(rDate);
        if(ticket.isValidToReturn()){
            ResultSet rs = Main.statement.executeQuery(String.format(
                    "select top 1 ID from [Tickets] where BookId = %d order by id desc", bookId)
            );
            if(rs.next()) {
                int Id = rs.getInt(1);
                Main.statement.executeUpdate(String.format(
                        "update [Tickets] set ReturnDate = '%s' where id = %d", rDate, Id)
                );

                Main.statement.executeUpdate(String.format(
                        "update [Books] set status = 'available' where id = %d", bookId)
                );
                updateData();
                System.out.println("Returned !");
            }
        } else {
            System.out.println("Invalid!");
//            status.setText("Register unsuccessfully!");
        }
    }
    private void borrow() throws SQLException {
        int bookId;
        try{
            bookId = Integer.parseInt(bookIDTf.getText());
        }catch (NumberFormatException e){
            bookId = 0;
        }
        int readerId;
        try{
            readerId = Integer.parseInt(readerIDTf.getText());
        }catch (NumberFormatException e){
            readerId = 0;
        }
        Ticket ticket = new Ticket(readerId, bookId);
        String bDate = bDateTf.getText();
        String aDate = aDateTf.getText();
        ticket.setBDate(bDate);
        ticket.setADate(aDate);
        if(ticket.isValidToBorrow()){
            String sql = String.format(
                    "insert into [Tickets](ReaderID, BookID, BorrowDate, AppointmentDate)" +
                            " values(%d,%d,'%s','%s')", readerId, bookId, bDate, aDate);
            System.out.println(sql);
            Main.statement.executeUpdate(sql);
            ResultSet rs = Main.statement.executeQuery(String.format(
                    "select top 1 ID from [Tickets] where BookId = %d order by id desc", bookId)
            );
            if(rs.next()) {
                int Id = rs.getInt(1);
                Main.statement.executeUpdate(String.format(
                        "update [Tickets] set BorrowDate = '%s', AppointmentDate = '%s' where id = %d", bDate, aDate, Id)
                );
                Main.statement.executeUpdate(String.format(
                        "update [Books] set status = 'borrowed' where id = %d", bookId)
                );
                updateData();
                System.out.println("Borrow successfully!");
            }
        } else {
            System.out.println("Invalid!");
//            status.setText("Register unsuccessfully!");
        }
    }
    private void extendBook() throws SQLException {
        int bookId;
        try{
            bookId = Integer.parseInt(bookIDTf.getText());
        }catch (NumberFormatException e){
            bookId = 0;
        }
        int readerId;
        try{
            readerId = Integer.parseInt(readerIDTf.getText());
        }catch (NumberFormatException e){
            readerId = 0;
        }
        String eDate = uDateTf.getText();
        Ticket ticket = new Ticket(readerId, bookId);
        ticket.setEDate(eDate);
        if(ticket.isValidToExtend()){
            ResultSet rs = Main.statement.executeQuery(String.format(
                    "select top 1 ID from [Tickets] where BookId = %d order by id desc", bookId)
            );
            if(rs.next()) {
                int Id = rs.getInt(1);
                Main.statement.executeUpdate(String.format(
                        "update [Tickets] set ExtendDate = '%s' where id = %d", eDate, Id)
                );
                Main.statement.executeUpdate(String.format(
                        "update [Books] set status = 'extended' where id = %d", bookId)
                );
                updateData();
            }

        } else {
            System.out.println("Invalid!");
//            status.setText("Register unsuccessfully!");
        }
    }
    @FXML
    private void update(ActionEvent event){
        try {
            switch (mode){
                case BORROW_MODE -> borrow();
                case RETURN_MODE -> returnBook();
                case EXTEND_MODE -> extendBook();
            }
            check(event);
        }catch (SQLException e){
            throw new RuntimeException();
        }
        cancel(event);
    }
    @FXML
    private void check(ActionEvent event){
        String id = bookIDTf.getText();
        try {
            Integer.parseInt(id);
        }catch (NumberFormatException e){
            id = "0";
        }

        try {
            ResultSet rs = Main.statement.executeQuery(String.format(
                    "select * from [Titles] t left join [Books] b on t.code = b.code where id = %s", id)
            );
            if (rs.next()) {
                nameTf.setText(rs.getString(2));
                categoryTf.setText(rs.getString(3));
                authorTf.setText(rs.getString(4));
                publisherTf.setText(rs.getString(5));
                yearTf.setText(rs.getString(6));
                shelfTf.setText(rs.getString(7));
                codeTf.setText(rs.getString(9));
                statusTf.setText(rs.getString(10));
                if(!statusTf.getText().equals("available")) {
                    rs = Main.statement.executeQuery(String.format(
                            "select top 1 ReaderID, BorrowDate, AppointmentDate" +
                                    " from [Tickets] where bookId = %s order by id desc", id)
                    );
                    if (rs.next()) {
                        readerIDTf.setText(rs.getString(1));
                        bDateTf.setText(rs.getString(2));
                        aDateTf.setText(rs.getString(3));
                    }
                } else {
                    readerIDTf.setText("No one");
                    bDateTf.setText("");
                    aDateTf.setText("");
                }
            } else {
                setBlank();
                nameTf.setText("book ID 's not found!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void borrow(ActionEvent event){
        changeMode(false);
        uDateTf.setVisible(false);
        bDateTf.setEditable(true);
        aDateTf.setEditable(true);
        mode = Mode.BORROW_MODE;
    }
    @FXML
    private void returnBook(ActionEvent event){
        changeMode(false);
        returnL.setVisible(true);
        mode = Mode.RETURN_MODE;
    }
    @FXML
    private void extendBook(ActionEvent event){
        changeMode(false);
        extendL.setVisible(true);
        mode = Mode.EXTEND_MODE;
    }
    @FXML
    private void penalize(ActionEvent event){
        changeMode(false);
        penaltyL.setVisible(true);
    }
    @FXML
    private void cancel(ActionEvent event){
        changeMode(true);
        returnL.setVisible(false);
        extendL.setVisible(false);
        penaltyL.setVisible(false);
        aDateTf.setEditable(false);
        bDateTf.setEditable(false);
    }
    private void changeMode(boolean b){
        checkB.setVisible(b);
        borrowB.setVisible(b);
        returnB.setVisible(b);
        extendB.setVisible(b);
        penalizeB.setVisible(b);
        updateB.setVisible(!b);
        cancelB.setVisible(!b);
        uDateTf.setVisible(!b);
    }
    public void setBlank() {
        bookIDTf.setText("");
        nameTf.setText("");
        categoryTf.setText("");
        authorTf.setText("");
        publisherTf.setText("");
        yearTf.setText("");
        shelfTf.setText("");
        codeTf.setText("");
        statusTf.setText("");
        readerIDTf.setText("");
        bDateTf.setText("");
        aDateTf.setText("");
        uDateTf.setText("");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(p->p.getValue().idProperty());
        bookIdCol.setCellValueFactory(p->p.getValue().bookIdProperty());
        readerIdCol.setCellValueFactory(p->p.getValue().readerIdProperty());
        bDateCol.setCellValueFactory(p->p.getValue().BDateProperty());
        aDateCol.setCellValueFactory(p->p.getValue().ADateProperty());
        eDateCol.setCellValueFactory(p->p.getValue().EDateProperty());
        rDateCol.setCellValueFactory(p->p.getValue().RDateProperty());

        setBlank();
        updateData();

        tTable.setItems(tickets);
    }

    private void updateData() {
        tickets.clear();
        try {
            String sql;
            sql = "select * from [Tickets]";
            ResultSet rs;
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                int readerId = rs.getInt(2);
                int bookId = rs.getInt(3);
                Ticket t = new Ticket(id, readerId, bookId);
                t.setBDate(rs.getString(4));
                t.setADate(rs.getString(5));
                t.setEDate(rs.getString(6));
                t.setRDate(rs.getString(7));
                tickets.add(t);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private enum Mode{
        BORROW_MODE, RETURN_MODE, EXTEND_MODE
    }
}
