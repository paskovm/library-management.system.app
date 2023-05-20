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

public class ControllerLogInMenu {

    private UserManagementService userService = UserManagementService.getInstance();
    private Library library = Library.getInstance();
    private static final String ADMIN_LOGIN_NAME = "admin";
    private static final String ADMIN_LOGIN_PASSWORD = "admin";
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private AnchorPane logInMenu;

    public void submit(ActionEvent event) throws IOException {
//        username.setText("glicciardi9@a8.net");
//        password.setText("aPxZI4vPn");

        StringBuilder fields = new StringBuilder();
        if (username.getText().isEmpty() || username.getText().isBlank()) {
            fields.append("username");
        }

        if (password.getText().isEmpty() || password.getText().isBlank()) {
            if (fields.length() > 0) {
                fields.append(", ");
            }
            fields.append("password");
        }

        if (fields.length() > 0) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("ERROR");
            errorAlert.setContentText("You provided empty value(s) for " + fields + ". Please, try again.");

            errorAlert.showAndWait();
            username.clear();
            password.clear();

        } else {
            if (isAdmin(username.getText(), password.getText())) {
                root = FXMLLoader.load(getClass().getResource("adminMenu.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } else {
                Person user = userService.getUserByEmail(username.getText());

                if (user == null) {
                    Alert noSuchUserAlert = new Alert(Alert.AlertType.INFORMATION);
                    noSuchUserAlert.setHeaderText("There is no registered user in the system with email: " +
                            username.getText() + " and the provided password.");
                    noSuchUserAlert.showAndWait();
                    username.clear();
                    password.clear();

                } else {
                    String resource = "";
                    if (user.getPassword().equals(password.getText())) {
                        if (user instanceof Librarian) {
                            resource = "librarianMenu.fxml";
                        } else if (user instanceof Clerk) {
                            resource = "clerkMenu.fxml";
                        } else if (user instanceof Borrower) {
                            resource = "borrowerMenu.fxml";
                        }
                    } else {
                        Alert wrongPasswordAlert = new Alert(Alert.AlertType.ERROR);
                        wrongPasswordAlert.setHeaderText("There is no registered user in the system with email: " +
                                username.getText() + " and the provided password.");

                        wrongPasswordAlert.showAndWait();
                        username.clear();
                        password.clear();
                    }

                    if (resource.length() > 0) {
                        library.setLoggedInPerson(user);

                        root = FXMLLoader.load(getClass().getResource(resource));
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }

                }


            }
        }
    }

    private static boolean isAdmin(String email, String password) {
        if (ADMIN_LOGIN_NAME.equals(email) && ADMIN_LOGIN_PASSWORD.equals(password)) {
            return true;
        }
        return false;
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
            stage = (Stage) logInMenu.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }
}
