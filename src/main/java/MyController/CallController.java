package MyController;

import MyMain.Main;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CallController implements Initializable {
//
//    @FXML
//    private TextField bBookIdTf, bReaderIdTf, bDateTf, aDateTf, bAmountTf, rBookIdTf, rReaderIdTf, rAmountTf, rDateTf;
//    @FXML
//    private Button rCheckB, borrowB, bBackB, rBackB, returnB, bCheckB;
//    @FXML
//    private Label bNameL, bCategoryL, bAuthorL, bPublisherL, bYearL, bAmountL, rIdL,
//            rNameL, rCategoryL, rAuthorL, rPublisherL, rYearL, rAmountL, bDateL, aDateL;
//    @FXML
//    private TableView<BorrowTicket> bTable;
//    @FXML
//    private TableView<ReturnTicket> rTable;
//    @FXML
//    private TableColumn<Ticket, String> bBookIdCol, bStatusCol, rStatusCol, rBookIdCol;
//    @FXML
//    private TableColumn<Ticket, Number> bReaderIdCol, rReaderIdCol;
//    @FXML
//    private TableColumn<BorrowTicket, String> bDateCol, aDateCol, bCodeCol;
//    @FXML
//    private TableColumn<BorrowTicket, Number> bAmountCol;
//    @FXML
//    private TableColumn<ReturnTicket, String> rDateCol, rCodeCol;
//    @FXML
//    private TableColumn<ReturnTicket, Number> rAmountCol;
//
//    public Book book = new Book();
//    public static ObservableList<BorrowTicket> bTickets = FXCollections.observableArrayList();
//    public static ObservableList<ReturnTicket> rTickets = FXCollections.observableArrayList();
//
//    public void back(ActionEvent event){
//        Main.primaryStage.setScene(Main.menuScene);
//    }
//    public void returnBook(ActionEvent event){
//        String bookId = rBookIdTf.getText();
//        int readerId;
//        try{
//            readerId = Integer.parseInt(rReaderIdTf.getText());
//        }catch (NumberFormatException e){
//            readerId = 0;
//        }
//        int amount;
//        try{
//            amount = Integer.parseInt(rAmountL.getText());
//        }catch (NumberFormatException e){
//            amount = 0;
//        }
//        String rDate = rDateTf.getText();
//        int available = Integer.parseInt(rAmountL.getText());
//        ReturnTicket ticket = new ReturnTicket(readerId, bookId, amount, rDate);
//        if(ticket.isValid() && ticket.getAmount() <= available){
//            try {
//                String sql;
//                sql = String.format("insert into [ReturnTickets] (ReaderID, BookID, ReturnDate, Amount)"+
//                        " values(%d,'%s','%s',%d)", readerId, bookId, rDate, amount);
//                Main.statement.executeUpdate(sql);
//                rTickets.add(ticket);
//                sql = String.format("update [Books] set Available += %d where id = '%s'", amount, bookId);
//
//                Main.statement.executeUpdate(sql);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            System.out.println("Invalid!");
////            status.setText("Register unsuccessfully!");
//        }
//    }
//    public void borrow(ActionEvent event){
//        String bookId = bBookIdTf.getText();
//        int readerId;
//        try{
//            readerId = Integer.parseInt(bReaderIdTf.getText());
//        }catch (NumberFormatException e){
//            readerId = 0;
//        }
//        String bDate = bDateTf.getText();
//        String aDate = aDateTf.getText();
//        int amount;
//        try{
//            amount = Integer.parseInt(bAmountTf.getText());
//        }catch (NumberFormatException e){
//            amount = 0;
//        }
//
//        BorrowTicket ticket = new BorrowTicket(readerId, bookId, amount, bDate, aDate);
//        int available = Integer.parseInt(bAmountL.getText());
//        if(ticket.isValid() && ticket.getAmount() <= available){
//            try {
//                String sql;
//                sql = String.format("insert into [BorrowTickets] (ReaderID, BookID, BorrowDate, AppointmentDate, Amount)"+
//                        " values(%d,'%s','%s','%s',%d)", readerId, bookId, bDate, aDate, amount);
//                Main.statement.executeUpdate(sql);
//                bTickets.add(ticket);
//                sql = String.format("update [Books] set Available -= %d where id = '%s'", amount, bookId);
//
//                Main.statement.executeUpdate(sql);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            System.out.println("Invalid!");
////            status.setText("Register unsuccessfully!");
//      }
//    }
//    public void rCheck(){
//        String id = rBookIdTf.getText();
//        int id2 ;
//        try {
//            id2 = Integer.parseInt(rReaderIdTf.getText());
//        }catch (NumberFormatException e){
//            id2 = 0;
//        }
//        int checked = 0;
//        if(!id.contentEquals("")) {
//            for (Book b : BookController.bookMap.values()) {
//                if (b.getCode().contentEquals(id)) {
//                    checked = 1;
//                    rNameL.setText(b.getName());
//                    rCategoryL.setText(b.getCategory());
//                    rAuthorL.setText(b.getAuthor());
//                    rPublisherL.setText(b.getPublisher());
//                    rYearL.setText(String.valueOf(b.getYear()));
//
//                    break;
//                }
//            }
//            int count = 0;
//            for (BorrowTicket t : bTickets){
//                if(t.getBookId().contentEquals(id) && t.getReaderId() == id2){
//                    count += t.getAmount();
//                }
//            }
//            for (ReturnTicket t : rTickets){
//                if(t.getBookId().contentEquals(id) && t.getReaderId() == id2){
//                    count -= t.getAmount();
//                }
//            }
//            rAmountL.setText(String.valueOf(count));
//        }
//        if(checked == 0) System.out.println("Book ID is incorrect ");
//    }
//    public void bCheck(){
//        String id = bBookIdTf.getText();
//        int checked = 0;
//        if(!id.contentEquals(""))
//            for(Book b: BookController.bookMap.values()){
//                if(b.getCode().contentEquals(id)) {
//                    checked = 1;
//                    bNameL.setText(b.getName());
//                    bCategoryL.setText(b.getCategory());
//                    bAuthorL.setText(b.getAuthor());
//                    bPublisherL.setText(b.getPublisher());
//                    bYearL.setText(String.valueOf(b.getYear()));
//                    bAmountL.setText(String.valueOf(b.getAmount()));
//
//                    break;
//                }
//            }
//        if(checked == 0) System.out.println("Book ID is incorrect ");
//    }
//
//    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        bBookIdCol.setCellValueFactory(p -> p.getValue().bookIdProperty());
//        bReaderIdCol.setCellValueFactory(p->p.getValue().readerIdProperty());
//        bDateCol.setCellValueFactory(p->p.getValue().BDateProperty());
//        aDateCol.setCellValueFactory(p->p.getValue().ADateProperty());
//        bAmountCol.setCellValueFactory(p->p.getValue().amountProperty());
//
//        rBookIdCol.setCellValueFactory(p->p.getValue().bookIdProperty());
//        rReaderIdCol.setCellValueFactory(p->p.getValue().readerIdProperty());
//        rDateCol.setCellValueFactory(p->p.getValue().RDateProperty());
//        rAmountCol.setCellValueFactory(p->p.getValue().amountProperty());

        try {
            String sql;
            sql = "select * from [BorrowTickets]";
            ResultSet rs;
            rs = Main.statement.executeQuery(sql);
            while (rs.next()) {
                int readerId = rs.getInt(2);
                int bookId = rs.getInt(3);
                String bDate = rs.getString(4);
                String aDate = rs.getString(5);

//                bTickets.add(new BorrowTicket(readerId, bookId, bDate, aDate));
            }
            sql = "select * from [ReturnTickets]";
            rs = Main.statement.executeQuery(sql);
            while (rs.next()){
                int readerId = rs.getInt(2);
                int bookId = rs.getInt(3);
                String rDate = rs.getString(4);
                int amount = rs.getInt(5);

//                rTickets.add(new ReturnTicket( readerId, bookId,  rDate));
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        bTable.setItems(bTickets);
//        rTable.setItems(rTickets);
    }
}
