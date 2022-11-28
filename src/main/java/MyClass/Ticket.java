package MyClass;

import MyMain.Main;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Ticket {
    SimpleIntegerProperty id;
    SimpleIntegerProperty readerId;
    SimpleIntegerProperty bookId;

    public Ticket(int readerId, int bookId){
        this.readerId = new SimpleIntegerProperty(readerId);
        this.bookId = new SimpleIntegerProperty(bookId);
    }
    public Ticket(int id, int readerId, int bookId){
        this.id = new SimpleIntegerProperty(id);
        this.readerId = new SimpleIntegerProperty(readerId);
        this.bookId = new SimpleIntegerProperty(bookId);
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

    protected boolean isValid() throws SQLException{
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
}
