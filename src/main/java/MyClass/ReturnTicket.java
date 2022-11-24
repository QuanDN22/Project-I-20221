package MyClass;

import javafx.beans.property.SimpleStringProperty;

public class ReturnTicket extends Ticket{
    Date RDate;
    public ReturnTicket( int readerId, int bookId, String RDate){
        super(readerId, bookId);
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
    public boolean isValid(){
        return true;
    }
}
