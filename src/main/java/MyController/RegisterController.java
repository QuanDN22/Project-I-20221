package MyController;

import MyMain.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.sql.SQLException;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDateTime now = LocalDateTime.now();


    @FXML
    private TextField fullName, idCard, phone, add, birth;


    private static SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
    private static SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Khi gõ enter sẽ chuyển sang nhập pass
        fullName.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                idCard.requestFocus();
            }
        });
        idCard.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                birth.requestFocus();
            }
        });
        birth.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                phone.requestFocus();
            }
        });
        phone.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                add.requestFocus();
            }
        });
    }

    private void register() throws SQLException{
        try {
            String sql = "INSERT INTO [dbo].[Users] " + "VALUES ('" + null + "', '" + null +"', N'"
                    + fullName.getText() +"', '" + formatDate(birth.getText()) + "', '" + idCard.getText() + "', '" + phone.getText()
                    +"', '" + updateYearExpiry(dtf.format(now) +"") + "', N'" + add.getText() +"');";
            Main.statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Thêm dữ liệu thất bại :" + e);
            throw e;
        }
    }

    // Kiểm tra xem có ô trống nào chưa được điền không
    private int checkIsNull() {
        if (fullName.getText().isEmpty() || idCard.getText().isEmpty() || phone.getText().isEmpty()
                || add.getText().isEmpty() || birth.getText().isEmpty()) {
            return 0;
        }
        return 1;
    }

    private void clearText() {
        fullName.clear();
        birth.clear();
        idCard.clear();
        phone.clear();
        add.clear();
    }

    // Khi bấm vào Continue sẽ đăng kí tài khoản
    @FXML
    private void Continue(ActionEvent event) throws SQLException {
        if (checkIsNull() == 0) {
            String text = "Enter complete information!";
            showAlertErrorSignup(text, "Warning", "Registration failed");
        } else {
            if(isValidFormat("dd/MM/yyyy", birth.getText(), Locale.ENGLISH) == false) {
                String text = "Birth date is in the wrong format. Retype!";
                showAlertErrorSignup(text, "Warning", null);
            }
            else {
                register();
                String text = "Registration successful!";
                showAlertSuccessSignup(text);
                System.out.println("\nĐăng kí thành công!\n");
                clearText();
                LoginController.changeScene(event, "/MyFXML/Menu.fxml", "Library Project");
            }
        }
    }

    // Sự kiện quay về màn hình đăng kí
    @FXML
    private void Back(ActionEvent event) {
        clearText();
        LoginController.changeScene(event, "/MyFXML/Menu.fxml", "Library Project");
    }

    // Tạo 1 thông báo alert khi nhập thiếu thông tin
    private void showAlertErrorSignup(String text, String title, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(close);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == close) {
            alert.close();
        }
    }

    // Tạo 1 thông báo alert khi đăng nhập thành công
    private void showAlertSuccessSignup(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(text);

        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(close);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == close) {
            alert.close();
        }
    }

    // convert từ dạng "dd/mm/yyyy" về dạng "yyyy-mm-dd"
    private String formatDate(String inDate) {
        String outDate = "";
        if (inDate != null) {
            try {
                Date date = inSDF.parse(inDate);
                outDate = outSDF.format(date);
            } catch (ParseException ex) {
            }
        }
        return outDate;
    }

    // Kiểm tra định dạng ngày tháng năm
    public static boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }
        return false;
    }

    // Update ngày hết hạn
    public static String updateYearExpiry(String date) {
        if (!date.isEmpty()) {
            String expiryDate = "";
            String[] parts = date.split("/");
            int leng = parts.length - 1;

            for (int i = 0; i < parts.length; i++) {
                if (i != leng) {
                    expiryDate += (parts[i] + "/");
                } else {
                    int year = Integer.parseInt(parts[i]) + 5;
                    expiryDate += String.valueOf(year);
                }

            }
            return expiryDate;
        }
        else {
            return null;
        }
    }
}
