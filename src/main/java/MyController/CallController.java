package MyController;

import MyClass.Title;
import MyClass.BorrowTicket;
import MyClass.ReturnTicket;
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
    private TextField bBookIdTf, bReaderIdTf, bDateTf, aDateTf, rBookIdTf, rDateTf;
    @FXML
    private Button rCheckB, borrowB, bBackB, rBackB, returnB, bCheckB;
    @FXML
    private Label bNameL, bCategoryL, bAuthorL, bPublisherL, bYearL, bShelfL, bStatusL, bCodeL,
            rNameL, rCategoryL, rAuthorL, rPublisherL, rYearL, bDateL, aDateL, rShelfL, rCodeL, rStatusL, rReaderIdL;
    @FXML
    private TableView<BorrowTicket> bTable;
    @FXML
    private TableView<ReturnTicket> rTable;

    @FXML
    private TableColumn<Ticket, Number> bIdCol, rIdCol, rBookIdCol, bBookIdCol, bReaderIdCol, rReaderIdCol;
    @FXML
    private TableColumn<BorrowTicket, String> bDateCol, aDateCol;
    @FXML
    private TableColumn<ReturnTicket, String> rDateCol;

    private static final ObservableList<BorrowTicket> bTickets = FXCollections.observableArrayList();
    private final static ObservableList<ReturnTicket> rTickets = FXCollections.observableArrayList();

    @FXML
    private void back(ActionEvent event){
        MenuController.stages[2].close();
    }
    @FXML
    private void returnBook(ActionEvent event){
        int bookId;
        try{
            bookId = Integer.parseInt(rBookIdTf.getText());
        }catch (NumberFormatException e){
            bookId = 0;
        }
        int readerId;
        try{
            readerId = Integer.parseInt(rReaderIdL.getText());
        }catch (NumberFormatException e){
            readerId = 0;
        }
        String rDate = rDateTf.getText();
        ReturnTicket ticket = new ReturnTicket(readerId, bookId, rDate);
        try {
            if(ticket.isValid()){
                ResultSet rs = Main.statement.executeQuery(String.format(
                        "select top 1 ReaderID from [BorrowTickets] where BookId = %d order by id desc", bookId)
                );

                if(rs.next()) {
                    int rdId = rs.getInt(1);
                    String sql = String.format(
                            "insert into [ReturnTickets] values(%d, %d,'%s')", rdId, bookId, rDate);
                    System.out.println(sql);
                    Main.statement.executeUpdate(sql);

                    Main.statement.executeUpdate(String.format(
                            "update [Books] set status = 'available' where id = %d", bookId)
                    );
                    rs = Main.statement.executeQuery("select top 1 id from [BorrowTickets]" +
                            "order by id desc");
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        rTickets.add(new ReturnTicket(id, rdId, bookId, rDate));
                    }
                }
                rBookIdTf.setText("");
            } else {
                System.out.println("Invalid!");
//            status.setText("Register unsuccessfully!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    private void borrow(ActionEvent event){
        int bookId;
        try{
            bookId = Integer.parseInt(bBookIdTf.getText());
        }catch (NumberFormatException e){
            bookId = 0;
        }
        int readerId;
        try{
            readerId = Integer.parseInt(bReaderIdTf.getText());
        }catch (NumberFormatException e){
            readerId = 0;
        }
        String bDate = bDateTf.getText();
        String aDate = aDateTf.getText();

        try {
            if(new BorrowTicket(readerId, bookId, bDate, aDate).isValid()){
                String sql = String.format(
                        "insert into [BorrowTickets] values(%d,%d,'%s','%s')", readerId, bookId, bDate, aDate);
                System.out.println(sql);
                Main.statement.executeUpdate(sql);

                Main.statement.executeUpdate(String.format(
                        "update [Books] set status = 'borrowed' where id = %d", bookId)
                );
                ResultSet rs = Main.statement.executeQuery("select top 1 id from [BorrowTickets]" +
                        "order by id desc");
                if(rs.next()) {
                    int id = rs.getInt(1);
                    bTickets.add(new BorrowTicket(id, readerId, bookId, bDate, aDate));
                }
                bBookIdTf.setText("");
            } else {
                System.out.println("Invalid!");
//            status.setText("Register unsuccessfully!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    private void rCheck(){
        String id = rBookIdTf.getText();
        try {
            ResultSet rs = Main.statement.executeQuery(String.format(
                    "select * from [Titles] t left join [Books] b on t.code = b.code where id = %s", id)
            );
            if (rs.next()) {
                rNameL.setText(rs.getString(2));
                rCategoryL.setText(rs.getString(3));
                rAuthorL.setText(rs.getString(4));
                rPublisherL.setText(rs.getString(5));
                rYearL.setText(rs.getString(6));
                rShelfL.setText(rs.getString(7));
                rCodeL.setText(rs.getString(9));
                rStatusL.setText(rs.getString(10));
                if(rStatusL.getText().equals("borrowed")) {
                    rs = Main.statement.executeQuery(String.format(
                            "select top 1 ReaderID, BorrowDate, AppointmentDate" +
                                    " from [BorrowTickets] where bookId = %s order by id desc", id)
                    );
                    if (rs.next()) {
                        rReaderIdL.setText(String.valueOf(rs.getInt(1)));
                        bDateL.setText(rs.getString(2));
                        aDateL.setText(rs.getString(3));
                    }
                }
            } else {
                rNameL.setText("book ID 's not found!");
                rCategoryL.setText("");
                rAuthorL.setText("");
                rPublisherL.setText("");
                rYearL.setText("");
                rShelfL.setText("");
                rCodeL.setText("");
                rStatusL.setText("");
                rReaderIdL.setText("");
                bDateL.setText("");
                aDateL.setText("");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void bCheck(){
        String id = bBookIdTf.getText();
        try {
            ResultSet rs = Main.statement.executeQuery(String.format(
                    "select * from [Titles] t left join [Books] b on t.code = b.code where id = %s", id)
            );
            if (rs.next()) {
                bNameL.setText(rs.getString(2));
                bCategoryL.setText(rs.getString(3));
                bAuthorL.setText(rs.getString(4));
                bPublisherL.setText(rs.getString(5));
                bYearL.setText(rs.getString(6));
                bShelfL.setText(rs.getString(7));
                bCodeL.setText(rs.getString(9));
                bStatusL.setText(rs.getString(10));
            } else {
                bNameL.setText("book ID 's not found!");
                bCategoryL.setText("");
                bAuthorL.setText("");
                bPublisherL.setText("");
                bYearL.setText("");
                bShelfL.setText("");
                bCodeL.setText("");
                bStatusL.setText("");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bIdCol.setCellValueFactory(p->p.getValue().idProperty());
        bBookIdCol.setCellValueFactory(p -> p.getValue().bookIdProperty());
        bReaderIdCol.setCellValueFactory(p->p.getValue().readerIdProperty());
        bDateCol.setCellValueFactory(p->p.getValue().BDateProperty());
        aDateCol.setCellValueFactory(p->p.getValue().ADateProperty());

        rIdCol.setCellValueFactory(p->p.getValue().idProperty());
        rBookIdCol.setCellValueFactory(p->p.getValue().bookIdProperty());
        rReaderIdCol.setCellValueFactory(p->p.getValue().readerIdProperty());
        rDateCol.setCellValueFactory(p->p.getValue().RDateProperty());


        try {
            String sql;
            sql = "select * from [BorrowTickets]";
            ResultSet rs;
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                int readerId = rs.getInt(2);
                int bookId = rs.getInt(3);
                String bDate = rs.getString(4);
                String aDate = rs.getString(5);
                bTickets.add(new BorrowTicket(id, readerId, bookId, bDate, aDate));
            }
            sql = "select * from [ReturnTickets]";
            rs = Main.statement.executeQuery(sql);
            while (rs.next()){
                int id = rs.getInt(1);
                int readerId = rs.getInt(2);
                int bookId = rs.getInt(3);
                String rDate = rs.getString(4);
                rTickets.add(new ReturnTicket( id, readerId, bookId,  rDate));
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        bTable.setItems(bTickets);
        rTable.setItems(rTickets);
    }
}
