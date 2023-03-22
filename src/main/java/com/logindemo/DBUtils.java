package com.logindemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DBUtils {

    public static void changeScence(ActionEvent event, String fxmlFile, String title, String username, String note){
        Parent root = null;
        if(username != null && note != null){
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();

                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username,note);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void signupUser(ActionEvent event, String username, String password, String note) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "123");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username!");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, note) VALUES (?, ?, ?)");
                psInsert.setString(1,username);
                psInsert.setString(2,password);
                psInsert.setString(3,note);
                psInsert.executeUpdate();

                changeScence(event, "logged-In.fxml", "Welcome", username, note);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (resultSet != null ){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psCheckUserExists != null ){
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psInsert != null ){
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null ){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "123");
            preparedStatement = connection.prepareStatement("SELECT password, note FROM users WHERE username = ?");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are in correct!");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievePassword = resultSet.getString("password");
                    String retrieveNote = resultSet.getString("note");
                    if(retrievePassword.equals(password)){
                        changeScence(event,"logged-In.fxml","Welcome", username, retrieveNote);
                    } else {
                        System.out.println("password did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are in correct!");
                        alert.show();
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null ){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null ){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null ){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
