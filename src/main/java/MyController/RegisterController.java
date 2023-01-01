package MyController;

import MyMain.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField fullName, userName, idCard, phone, add, birth, expiry;
    @FXML
    private PasswordField passWord;


    private static SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
    private static SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Khi gõ enter sẽ chuyển sang nhập pass
        fullName.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                userName.requestFocus();
            }
        });
        userName.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                passWord.requestFocus();
            }
        });
        passWord.setOnKeyPressed(keyEvent -> {
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
                expiry.requestFocus();
            }
        });
        expiry.setOnKeyPressed(keyEvent -> {
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
            String sql = "INSERT INTO [dbo].[Users] " + "VALUES ('" + userName.getText() + "', '" + passWord.getText() +"', N'"
                    + fullName.getText() +"', '" + formatDate(birth.getText()) + "', '" + idCard.getText() + "', '" + phone.getText()
                    +"', '" + formatDate(expiry.getText()) + "', N'" + add.getText() +"');";
            Main.statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Thêm dữ liệu thất bại :" + e);
            throw e;
        }
    }

    // Kiểm tra xem có ô trống nào chưa được điền không
    private int checkIsNull() {
        if (userName.getText().isEmpty() || passWord.getText().isEmpty() || fullName.getText().isEmpty()
                || idCard.getText().isEmpty() || phone.getText().isEmpty() || add.getText().isEmpty()
                || birth.getText().isEmpty() || expiry.getText().isEmpty()) {
            return 0;
        }
        return 1;
    }

    private void clearText() {
        fullName.clear();
        userName.clear();
        passWord.clear();
        birth.clear();
        idCard.clear();
        phone.clear();
        expiry.clear();
        add.clear();
    }

    // Khi bấm vào Continue sẽ đăng kí tài khoản
    @FXML
    private void Continue(ActionEvent event) throws SQLException {
        if (checkIsNull() == 0) {
            String text = "Nhập đầy đủ thông tin!";
            showAlertErrorSignup(text, "Cảnh báo", "Đăng kí thất bại");
        } else {
            if(isValidFormat("dd/MM/yyyy", birth.getText(), Locale.ENGLISH) == false) {
                String text = "Birth date sai định dạng 'ngày/tháng/năm'. Nhập lại!";
                showAlertErrorSignup(text, "Cảnh báo", null);
            }
            else if (isValidFormat("dd/MM/yyyy", expiry.getText(), Locale.ENGLISH) == false) {
                String text = "Expiry date sai định dạng 'ngày/tháng/năm'. Nhập lại!";
                showAlertErrorSignup(text, "Cảnh báo", null);
            }
            else {
                register();
                String text = "Đăng kí thành công!";
                showAlertSuccessSignup(text);
                System.out.println("\nĐăng kí thành công!\n");
                clearText();
                LoginController.changeScene(event, "/MyFXML/Login.fxml", "Library Project");
            }
        }
    }

    // Sự kiện quay về màn hình đăng kí
    @FXML
    private void Back(ActionEvent event) {
        clearText();
        LoginController.changeScene(event, "/MyFXML/Login.fxml", "Library Project");
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
}
