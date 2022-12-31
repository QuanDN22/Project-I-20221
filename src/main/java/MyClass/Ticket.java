package MyClass;

import MyMain.Main;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ticket {
    SimpleIntegerProperty id;
    SimpleIntegerProperty readerId;
    SimpleIntegerProperty bookId;
    Date BDate =  new Date("");
    Date ADate = new Date("");
    Date EDate = new Date("");
    Date RDate = new Date("");

    public Ticket(int readerId, int bookId){
        this.readerId = new SimpleIntegerProperty(readerId);
        this.bookId = new SimpleIntegerProperty(bookId);
    }
    public Ticket(int id, int readerId, int bookId){
        this.id = new SimpleIntegerProperty(id);
        this.readerId = new SimpleIntegerProperty(readerId);
        this.bookId = new SimpleIntegerProperty(bookId);
    }
    public Ticket(int readerId, int bookId, String bDate, String aDate){
        this.readerId = new SimpleIntegerProperty(readerId);
        this.bookId = new SimpleIntegerProperty(bookId);
        this.BDate = new Date(bDate);
        this.ADate = new Date(aDate);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getReaderId() {
        return readerId.get();
    }

    public SimpleIntegerProperty readerIdProperty() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId.set(readerId);
    }

    public int getBookId() {
        return bookId.get();
    }

    public SimpleIntegerProperty bookIdProperty() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId.set(bookId);
    }

    public String getBDate() {
        return BDate.get();
    }

    public Date BDateProperty() {
        return BDate;
    }

    public void setBDate(String BDate) {
        this.BDate.set(BDate);
    }

    public String getADate() {
        return ADate.get();
    }

    public Date ADateProperty() {
        return ADate;
    }

    public void setADate(String ADate) {
        this.ADate.set(ADate);
    }

    public String getEDate() {
        return EDate.get();
    }

    public Date EDateProperty() {
        return EDate;
    }

    public void setEDate(String EDate) {
        this.EDate.set(EDate);
    }

    public String getRDate() {
        return RDate.get();
    }

    public Date RDateProperty() {
        return RDate;
    }

    public void setRDate(String RDate) {
        this.RDate.set(RDate);
    }

    public boolean isValid() throws SQLException{
        return isValidBookID() && isValidReader();
    };
    protected boolean isValidBookID() throws SQLException {
       System.out.println(getBookId());
       return true;
    }
    protected boolean isValidReader() throws SQLException {
        System.out.println(getReaderId());
        return true;
    }
    public boolean isValidToBorrow() throws SQLException {
        System.out.println("Appointment:");
        int dif = BDate.getDateDif(ADate);
        System.out.println("BDate: "+BDate);
        System.out.println("ADate: "+ADate);
        System.out.println("dif: :"+dif);
        boolean d =  dif >= 0 && dif <= 14;

        System.out.println("Book ID:");
        ResultSet rs = Main.statement.executeQuery(String.format("select status from [Books] where id = %d", getBookId()));
        boolean b = false;
        if(rs.next()) {
            String s = rs.getString(1);
            System.out.println("status: "+ s);
            b = s.equals("available");
            System.out.println("isValidBookID: "+b);
        }

        System.out.println("Reader ID:");
        rs = Main.statement.executeQuery(String.format(
                "select ExpiryDate from [Users] where id = %d", getReaderId()));
        boolean r = false;
        if(rs.next()) {
            String s = rs.getString(1);
            System.out.println("Expiry: "+ s);
            Date dt = new Date(s);
            r = (new Date(getADate()).getDateDif(dt)) >= 0;
            System.out.println("isValidReaderID: "+r);
        }
        return d && b && r;
    }
    public boolean isValidToExtend() throws SQLException {
        System.out.println("Extend:");
        ResultSet rs = Main.statement.executeQuery(String.format(
                "select top 1 AppointmentDate from [Tickets] where BookId = %d order by id desc", getBookId()));
        boolean d = false;
        if(rs.next()) {
            String s = rs.getString(1);
            System.out.println("Appointment: "+ s);
            int df = new Date(s).getDateDif(new Date(getEDate()));
            d = df >= 0 && df <= 7;
            System.out.println("isValidExtendDate: "+d);
        }

        System.out.println("Book ID:");
        rs = Main.statement.executeQuery(String.format("select status from [Books] where id = %d", getBookId()));
        boolean b = false;
        if (rs.next()) {
            String s = rs.getString(1);
            System.out.println("status: " + s);
            b = s.equals("borrowed");
            System.out.println("isValidBookID: " + b);
        }

        System.out.println("Reader ID:");
        rs = Main.statement.executeQuery(String.format(
                "select ExpiryDate from [Users] where id = %d", getReaderId()));
        boolean r = false;
        if(rs.next()) {
            String s = rs.getString(1);
            System.out.println("Expiry: "+ s);
            Date dt = new Date(s);
            r = (new Date(getEDate()).getDateDif(dt)) >= 0;
            System.out.println("isValidReaderID: "+r);
        }

        return b && d && r;
    }

    public boolean isValidToReturn() throws SQLException {

        System.out.println("Book ID:");
        ResultSet rs = Main.statement.executeQuery(String.format("select status from [Books] where id = %d", getBookId()));
        boolean b = false;
        String st = "";
        if (rs.next()) {
            st = rs.getString(1);
            System.out.println("status: " + st);
            b = st.equals("borrowed")||st.equals("extended");
            System.out.println("isValidBookID: " + b);
        }

        System.out.println("Return:"+getBookId());
        rs = Main.statement.executeQuery(String.format(
                "select top 1 AppointmentDate, BorrowDate, ExtendDate " +
                        "from [Tickets] where BookId = %d order by id desc", getBookId()));
        boolean d = false;
        System.out.println("aaaaa");
        if(rs.next()) {
            String s = getRDate();
            String s1 = rs.getString(1);
            String s2 = rs.getString(2);

            System.out.println("Appointment: "+ s);
            if(st.equals("borrowed"))
                d = new Date(s).getDateDif(new Date(s1)) >= 0 && new Date(s2).getDateDif(new Date(s)) >= 0;
            if (st.equals("extended"))
                d = new Date(s).getDateDif(new Date(rs.getString(3))) >= 0 && new Date(s2).getDateDif(new Date(s)) >= 0;
            System.out.println("isValidReturnDate: "+d);
        }
        return b && d;
    }
}
