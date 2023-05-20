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
import java.util.Objects;

public class ControllerBorrowerMenu {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private AnchorPane borrowerMenuPane;

    public void searchBook(ActionEvent event) throws IOException {
        String resource = "searchBook.fxml";
        navigateTo(event, resource);
    }

    public void createOnHoldReq(ActionEvent event) throws IOException {
        String resource = "createOnHoldReq.fxml";
        navigateTo(event, resource);
    }

    public void getUsersInfo(ActionEvent event) throws IOException {
        String resource = "checkUsersInfo.fxml";
        navigateTo(event, resource);
    }

    public void getBorrowersFines(ActionEvent event) throws IOException {
        String resource = "borrowersFines.fxml";
        navigateTo(event, resource);
    }

    public void getBookHoldRequests(ActionEvent event) throws IOException {
        String resource = "listHoldReqOfBook.fxml";
        navigateTo(event, resource);
    }

    public void logOut(ActionEvent event) throws IOException, InterruptedException {
        Thread.sleep(500);

        String resource = "mainMenu.fxml";
        navigateTo(event, resource);
    }

    private void navigateTo(ActionEvent event, String resource) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
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

    public void exit() throws InterruptedException, IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) borrowerMenuPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }

}
