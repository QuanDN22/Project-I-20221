package MyClass;

import MyMain.Main;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowTicket extends Ticket {
    Date BDate;
    Date ADate;



    public BorrowTicket(int readerId, int bookId, String BDate, String ADate){
        super(readerId, bookId);
        System.out.println(BDate + ": ");
        this.BDate = new Date(BDate);
        System.out.println(ADate + ": ");
        this.ADate = new Date(ADate);
    }
    public BorrowTicket(int id, int readerId, int bookId, String BDate, String ADate){
        super(id, readerId, bookId);
        System.out.println(BDate + ": ");
        this.BDate = new Date(BDate);
        System.out.println(ADate + ": ");
        this.ADate = new Date(ADate);
    }


    public String getBDate() {
        return BDate.get();
    }

    public SimpleStringProperty BDateProperty() {
        return BDate;
    }

    public void setBDate(String bDate) {
        this.BDate.set(bDate);
    }

    public String getADate() {
        return ADate.get();
    }

    public SimpleStringProperty ADateProperty() {
        return ADate;
    }

    public void setADate(String aDate) {
        this.ADate.set(aDate);
    }
    public boolean isValid() throws SQLException {
        return isValidAppointment() && super.isValid();
    }

    private boolean isValidAppointment(){
        System.out.println("Appointment:");
        int dif = BDate.getDateDif(ADate);
        System.out.println("BDate: "+BDate);
        System.out.println("ADate: "+ADate);
        System.out.println("dif: :"+dif);
        return dif >= 0 && dif <= 28;
    }

    @Override
    protected boolean isValidBookID() throws SQLException {
        System.out.println("Book ID:");
        ResultSet rs = Main.statement.executeQuery(String.format("select status from [Books] where id = %d", getBookId()));
        boolean b = false;
        if(rs.next()) {
            String s = rs.getString(1);
            System.out.println("status: "+ s);
            b = s.equals("available");
            System.out.println("isValidBookID: "+b);
        }
        return b;
    }
    @Override
    protected boolean isValidReader() throws SQLException {
        System.out.println("Reader ID:");
        ResultSet rs = Main.statement.executeQuery(String.format(
                "select ExpiryDate from [Users] where id = %d", getReaderId()));
        boolean b = false;
        if(rs.next()) {
            String s = rs.getString(1);
            System.out.println("Expiry: "+ s);
            Date d = new Date(s);
            b = (new Date(getADate()).getDateDif(d)) >= 0;
            System.out.println("isValidReaderID: "+b);
        }
        return b;
    }
}

