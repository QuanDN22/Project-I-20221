package MyClass;

import javafx.beans.property.SimpleStringProperty;

public class Date extends SimpleStringProperty {

    public Date(String date){
        super(date);
        System.out.println(getValue());
    }
    private int getYear(){
        int year;
        try{
            year = Integer.parseInt(this.getValue().substring(0, 4));
            if(year < 1000 || year > 9999) throw new NumberFormatException();
        } catch (NumberFormatException e){
            return -1;
        }
        return 0;
    }
    private int getMonth(){
        int month;
        try{
            month = Integer.parseInt(this.getValue().substring(5, 7));
            if(month < 1 || month > 12) throw new NumberFormatException();
        } catch (NumberFormatException e){
            return -1;
        }
        return month;
    }
    private int getDay(){
        int day;
        try{
            day = Integer.parseInt(this.getValue().substring(8, 10));
        } catch (NumberFormatException e){
            return -1;
        }
        return day;
    }
    public boolean isValid(){
        System.out.println("valid: "+ this.getValue());
        if(this.getValue().contentEquals("0000-00-00")) return true;
        if(this.getValue().length() != 10) return false;
        String[] ss  = this.getValue().split("-", -1);
        if(ss.length != 3) return false;
        return getMonth() > 0 && isADayOfMonth() && getYear() > 0;
    }
    private boolean isADayOfMonth(){
        int[] day = {0,31,28,31,30,31,30,31,31,30,31,30,31};
        if(getYear() % 4 == 0 && getYear() % 100 != 0) day[2] = 29;
        return getDay() <= day[getMonth()] && getDay() > 0;
    }
    public int getDateDif(Date date){
        int dif = 0;
        if(!date.isValid() || !this.isValid()){
            System.out.println("invalid date");
            return -1;
        }
        if(this.getValue().compareTo(date.getValue()) > 0){
            System.out.println(this.getValue().compareTo(date.getValue()));
            System.out.println("invalid appointment");
            return -1;
        }
        int[] day = {0,31,28,31,30,31,30,31,31,30,31,30,31,30,31};
        for(int i = this.getYear(); i < date.getYear(); i++){
            if(getYear() % 4 == 0 && getYear() % 100 != 0) dif += 366;
            else dif += 365;
        }
        for(int i = this.getMonth(); i < date.getMonth(); i++){
            dif += day[i];
        }
        System.out.println("this: "+getDay());
        System.out.println("date: "+date.getDay());
        dif += date.getDay() - this.getDay();
        return dif+1;
    }
}
