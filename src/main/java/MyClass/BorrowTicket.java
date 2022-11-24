package MyClass;

import javafx.beans.property.SimpleStringProperty;

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
    public boolean isValid() {
        return isValidAppointment() && isValidBookID() && isValidReader();
    }

    private boolean isValidAppointment(){
        int dif = BDate.getDateDif(ADate);


        System.out.println("dif: :"+dif);
        return dif >= 0 && dif <= 28;
    }
}

