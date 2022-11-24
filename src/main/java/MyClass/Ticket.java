package MyClass;

import javafx.beans.property.SimpleIntegerProperty;

public abstract class Ticket {
    SimpleIntegerProperty id;
    SimpleIntegerProperty readerId;
    SimpleIntegerProperty bookId;

    public Ticket(int readerId, int bookId){
        this.readerId = new SimpleIntegerProperty(readerId);
        this.bookId = new SimpleIntegerProperty(bookId);

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

    public abstract boolean isValid();
    protected boolean isValidBookID(){
        System.out.println(getBookId());
        return true;
    }
    protected boolean isValidReader(){
        System.out.println(getReaderId());
        return true;
    }
}
