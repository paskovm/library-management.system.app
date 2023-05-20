package library.management.system.ui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerMainMenu {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private AnchorPane mainPane;

    public void logIn(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("logInMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void about() {
        Stage popup = new Stage();

        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("LMS info");

        Label version = new Label("version: " + Main.VERSION);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(version);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 250, 200);
        popup.setScene(scene);
        popup.showAndWait();
    }

    public void exit() throws InterruptedException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) mainPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }
}