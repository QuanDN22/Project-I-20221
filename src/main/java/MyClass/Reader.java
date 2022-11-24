package MyClass;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Reader extends User{
    SimpleStringProperty name;
    SimpleStringProperty idcard;
    SimpleStringProperty phone;
    Date expirydate;
    SimpleStringProperty address;
    Date birthdate;

    public Reader(int readerId) {
        super(readerId);
    }

    public String getIdcard() {
        return idcard.get();
    }

    public SimpleStringProperty idcardProperty() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard.set(idcard);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getExpirydate() {
        return expirydate.get();
    }

    public SimpleStringProperty expirydateProperty() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate.set(expirydate);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getBirthdate() {
        return birthdate.get();
    }

    public SimpleStringProperty birthdateProperty() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate.set(birthdate);
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




    public Reader(int id, String username, String password, String name, String birthdate, String idcard, String phone, String expirydate, String address) {
        super(id, username, password);
        this.name = new SimpleStringProperty(name);
        this.birthdate = new Date(birthdate);
        this.idcard = new SimpleStringProperty(idcard);
        this.phone = new SimpleStringProperty(phone);
        this.expirydate = new Date(expirydate);
        this.address = new SimpleStringProperty(address);
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

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
    public boolean equals(Reader reader){
        return this.getUsername().equals(reader.getUsername())
                && this.getPassword().equals(reader.getPassword());
    }

    public boolean isValid() {
        if(this.getName().length() > 50 || this.getAddress().length() > 50)return false;
        for(Character c : this.getName().toCharArray()){
            if(!(Character.isLetter(c) || Character.isWhitespace(c) )) return false;
        }
        return isValidPhoneNumber(getPhone()) && birthdate.isValid()
                && expirydate.isValid() && isValidIdCardNumber(getIdcard());
    }

    private boolean isValidPhoneNumber(String phoneNumber){
        System.out.println(phoneNumber);
        if(phoneNumber.contentEquals("0000-000-000")) return true;
        if(phoneNumber.charAt(0) != '0') return false;
        if(phoneNumber.length() != 12) return false;
        for(int i = 0; i < 12; i++){
            if(i == 4 || i == 8) {
                if(phoneNumber.charAt(i) != '-') return false;
            } else if('0' > phoneNumber.charAt(i) || '9' < phoneNumber.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    private boolean isValidIdCardNumber(String idCardNumber){
        System.out.println(idCardNumber);
        if(idCardNumber.contentEquals("00000-00000-0000")) return true;
        if(idCardNumber.length() != 16) {
            System.out.println("!16");
            return false;
        }
        for(int i = 0; i < 12; i++){
            if(i == 5 || i == 11) {
                if(idCardNumber.charAt(i) != '-') return false;
            } else if('0' > idCardNumber.charAt(i) || '9' < idCardNumber.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public void setReader(Reader reader) {
        this.name = reader.name;
        this.birthdate = reader.birthdate;
        this.idcard = reader.idcard;
        this.phone = reader.phone;
        this.expirydate = reader.expirydate;
        this.address = reader.address;
    }


}
