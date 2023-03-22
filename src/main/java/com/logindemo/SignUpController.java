package com.logindemo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private Button button_register;
    @FXML
    private Button button_login;
    @FXML
    private RadioButton rb_note1;
    @FXML
    private RadioButton rb_note2;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        rb_note1.setToggleGroup(toggleGroup);
        rb_note2.setToggleGroup(toggleGroup);
        rb_note1.setSelected(true);

        button_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String note = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
                if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()){
                    DBUtils.signupUser(event,tf_username.getText(), tf_password.getText(), note);
                } else {
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all the information to register!");
                    alert.show();
                }
            }
        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScence(event, "hello-view.fxml", "log-in", null,null);
            }
        });
    }
}
