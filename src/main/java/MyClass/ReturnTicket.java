package MyClass;

import MyMain.Main;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReturnTicket extends Ticket{
    Date RDate;
    public ReturnTicket( int readerId, int bookId, String RDate){
        super(readerId, bookId);
        this.RDate = new Date(RDate);
    }
    public ReturnTicket(int id, int readerId, int bookId, String RDate){
        super(id, readerId, bookId);
        this.RDate = new Date(RDate);
    }


    public String getRDate() {
        return RDate.get();
    }

    public SimpleStringProperty RDateProperty() {
        return RDate;
    }

    public void setRDate(String rDate) {
        this.RDate.set(rDate);
    }
    public boolean isValid() throws SQLException {
        return isValidReturnDate() && super.isValid();
    }
    protected boolean isValidReturnDate() throws SQLException {
        System.out.println("Return:");
        ResultSet rs = Main.statement.executeQuery(String.format(
                "select top 1 AppointmentDate from [BorrowTickets] where BookId = %d order by id desc", getBookId()));
        boolean b = false;
        if(rs.next()) {
            String s = rs.getString(1);
            System.out.println("Appointment: "+ s);
            Date d = new Date(s);
            b = (new Date(getRDate()).getDateDif(d)) >= 0;
            System.out.println("isValidReturnDate: "+b);
        }
        return b;
    }
    @Override
    protected boolean isValidBookID() throws SQLException {
        System.out.println("Book ID:");
        ResultSet rs = Main.statement.executeQuery(String.format("select status from [Books] where id = %d", getBookId()));
        boolean b = false;
        if (rs.next()) {
            String s = rs.getString(1);
            System.out.println("status: " + s);
            b = s.equals("borrowed");
            System.out.println("isValidBookID: " + b);
        }
        return b;
    }
}
