package MyClass;

import MyController.BookController;
import MyMain.Main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Book {

    private SimpleStringProperty code;
    private SimpleStringProperty name;
    private SimpleStringProperty author;
    private SimpleStringProperty category;
    private SimpleStringProperty publisher;
    private SimpleIntegerProperty year;
    private SimpleIntegerProperty amount;
    private SimpleIntegerProperty available;
    private SimpleStringProperty shelf;

    public Book() {

    }
    public boolean isNew(){
       for(Book b : BookController.bookMap.values()){
           if(b.getCode().contentEquals(this.getCode())) return false;
       }
       return true;
   }
   public void setBook(@NotNull Book book){
        this.code = book.code;
        this.name = book.name;
        this.category = book.category;
        this.author = book.author;
        this.publisher = book.publisher;
        this.year = book.year;
        this.amount = book.amount;
        this.available = book.available;
        this.shelf = book.shelf;
   }
    public Book(String ce, String n, String cy, String a, String p, int y, int at, int ae, String s) {
        this.code = new SimpleStringProperty(ce);
        this.name = new SimpleStringProperty(n);
        this.category = new SimpleStringProperty(cy);
        this.author = new SimpleStringProperty(a);
        this.publisher = new SimpleStringProperty(p);
        this.year = new SimpleIntegerProperty(y);
        this.amount = new SimpleIntegerProperty(at);
        this.available = new SimpleIntegerProperty(ae);
        this.shelf = new SimpleStringProperty(s);
    }

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAuthor() {
        return author.get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getPublisher() {
        return publisher.get();
    }

    public SimpleStringProperty publisherProperty() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public int getYear() {
        return year.get();
    }

    public SimpleIntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public int getAmount() {
        return amount.get();
    }

    public SimpleIntegerProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public int getAvailable() {
        return available.get();
    }

    public SimpleIntegerProperty availableProperty() {
        return available;
    }

    public void setAvailable(int available) {
        this.available.set(available);
    }

    public String getShelf() {
        return shelf.get();
    }

    public SimpleStringProperty shelfProperty() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf.set(shelf);
    }

    public boolean isValid() {
        return getYear() > 0 && isValidCode() && isValidAmount() && isValidShelf() && isValid(getName())
                && isValid(getCategory()) && isValid(getAuthor()) && isValid(getPublisher());
    }
    private boolean isValidCode(){
        if(getCode().length() != 6) return false;
        char[] chars = getCode().toCharArray();
        if(!Character.isLetter(chars[0]) || !Character.isLetter(chars[1])) return false;
        return Character.isDigit(chars[2]) && Character.isDigit(chars[3])
                && Character.isDigit(chars[4]) && Character.isDigit(chars[5]);
    }
    private boolean isValidAmount(){
        return getAvailable() >= 0 && getAmount() >= getAvailable();
    }
    private boolean isValidShelf(){
        if(getShelf().contentEquals("0-0-0-0")) return true;
        String[] str = getShelf().split("-", -1);
        if(str.length != 4) return false;
        try {
            int floor = Integer.parseInt(str[0]);
            if(floor <= 0 || floor > Main.NumberOfFloors) return false;
            int room = Integer.parseInt(str[1]);
            if(room <= 0 || room > Main.NumberOfRooms) return false;
            int bookCage = Integer.parseInt(str[2]);
            if(bookCage <= 0 || bookCage > Main.NumberOfBookCases) return false;
            int bookShelf = Integer.parseInt(str[3]);
            if (bookShelf <= 0 || bookShelf > Main.NumberOfBookShelves) return false;
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }
    private boolean isValid(String name){
        if(name.length() > 40 || name.length() == 0) return false;
        char[] chars = name.toCharArray();
        for(Character c : chars){
            if(!(Character.isLetterOrDigit(c) || Character.isWhitespace(c) )) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        System.out.println(book.getCode());
        return Objects.equals(this.getCode(), book.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
