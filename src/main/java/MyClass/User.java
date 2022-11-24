package MyClass;

import MyController.LoginController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class User {
    SimpleIntegerProperty id;
    SimpleStringProperty username;

    SimpleStringProperty password;
    public User(){

    }
    public User(int readerId) {
        this.id = new SimpleIntegerProperty(readerId);
    }
    public User(String username, String password) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }
    public User(int id, String username, String password) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
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

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }
    public boolean isValidAccount(){
        String username1 = this.getUsername();
        String password1 = this.getPassword();
        if(username1.length() < 6 || password1.length() < 6) return false;
        for (Character c :username1.toCharArray()){
            if(!Character.isLetterOrDigit(c)) return false;
        }
        for (Character c :password1.toCharArray()){
            if(!Character.isLetterOrDigit(c)) return false;
        }
        for(User u : LoginController.USERS)
            if(u.equals(this)) {
                return false;
            }
        return true;
    }
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        System.out.println(user.getUsername()+ "" + Objects.equals(this.getUsername(), user.getUsername()));
        return Objects.equals(this.getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
