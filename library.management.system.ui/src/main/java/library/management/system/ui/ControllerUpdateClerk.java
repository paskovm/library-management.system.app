package library.management.system.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Clerk;
import library.management.system.common.entities.actors.Librarian;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.services.UserManagementService;

import java.io.IOException;

public class ControllerUpdateClerk {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Library library = Library.getInstance();
    private UserManagementService userService = UserManagementService.getInstance();
    @FXML
    private TextArea usersInfo;
    @FXML
    private TextField clerkDetails;
    private Person clerkToUpdate;
    @FXML
    private AnchorPane updateClerkPane;

    public void submitUserSearch() {
        String providedClerkInfo = clerkDetails.getText().trim();
        if (providedClerkInfo.isEmpty() || providedClerkInfo.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("The provided users information must not be empty or blank!");

            alert.showAndWait();
            usersInfo.clear();

        } else {
            Person user = null;
            if (providedClerkInfo.matches("^[A-Za-z].*\\s[A-Za-z].*")) { // this must be users full name
                user = userService.getUserByFullName(providedClerkInfo);

            } else if (providedClerkInfo.contains("@")) { // this must be users email address
                user = userService.getUserByEmail(providedClerkInfo);

            } else { // this must be the users id
                try {
                    int id = Integer.parseInt(providedClerkInfo);
                    user = userService.getUserById(id);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            if (user == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("There is no user with id, name or email: \"" + providedClerkInfo + "\" registered into the system.");

                alert.showAndWait();
                usersInfo.clear();
            } else {
                if (user instanceof Clerk) {
                    usersInfo.setText(user.toString());
                    clerkToUpdate = (Clerk) user;

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("The user you are trying to update is not clerk!");

                    alert.showAndWait();
                    usersInfo.clear();
                }
            }
        }
    }

    public void goToUpdate(ActionEvent event) throws IOException {
        if (clerkToUpdate != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateUser.fxml"));

            ControllerUpdateUser controllerUpdateUser = new ControllerUpdateUser(clerkToUpdate);
            loader.setController(controllerUpdateUser);

            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Before continuing to update user you must select one first!");

            alert.showAndWait();
        }
    }

    public void clearUserToUpdate() {
        usersInfo.clear();
        clerkDetails.clear();
    }

    public void goBack(ActionEvent event) throws IOException {
        String resource = "";
        Person user = library.getLoggedInPerson();

        if (user instanceof Librarian) {
            resource = "librarianMenu.fxml";
        } else if (user instanceof Clerk) {
            resource = "clerkMenu.fxml";
        } else if (user instanceof Borrower) {
            resource = "borrowerMenu.fxml";
        } else { // this must be the admin
            resource = "adminMenu.fxml";
        }

        if (!resource.isEmpty()) {
            root = FXMLLoader.load(getClass().getResource(resource));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
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
            stage = (Stage) updateClerkPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }

}
