package library.management.system.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.management.system.common.entities.Book;
import library.management.system.common.entities.HoldRequest;
import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Clerk;
import library.management.system.common.entities.actors.Librarian;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.services.BookManagementService;
import library.management.system.common.services.HoldRequestManagementService;
import library.management.system.common.services.UserManagementService;
import library.management.system.common.utils.DatabaseInteractions;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCreateOnHoldReq implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Book bookToRequest;
    private Borrower borrower;
    private Library library = Library.getInstance();
    private BookManagementService bookService = BookManagementService.getInstance();
    private UserManagementService userService = UserManagementService.getInstance();
    private HoldRequestManagementService holdReqService = HoldRequestManagementService.getInstance();
    @FXML
    private TextField bookValue;
    @FXML
    private TextArea bookResult;
    @FXML
    private Label userLabel;
    @FXML
    private TextField userValue;
    @FXML
    private Label userDetailsLabel;
    @FXML
    private TextArea userResult;
    @FXML
    private Button searchUserButton;
    @FXML
    private Button resetUserButton;
    @FXML
    private AnchorPane createOnHoldPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (library.getLoggedInPerson() != null) {
            if (library.getLoggedInPerson() instanceof Borrower) {
                userLabel.setVisible(false);
                userValue.setVisible(false);
                userDetailsLabel.setVisible(false);
                userResult.setVisible(false);
                searchUserButton.setVisible(false);
                resetUserButton.setVisible(false);

                borrower = (Borrower) library.getLoggedInPerson();
            }
        }
    }

    public void searchBook() {
        Alert error = new Alert(Alert.AlertType.ERROR);
        if (bookValue.getText() == null || bookValue.getText().isEmpty() || bookValue.getText().isBlank()) {
            error.setTitle("ERROR");
            error.setHeaderText("No input info provided!");
            error.showAndWait();
        } else {
            String input = bookValue.getText();
            int id = -1;
            Book book = null;

            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
            }

            if (id > 0) {
                book = bookService.getBookById(id);
            } else {
                book = bookService.getBookByTitle(input);
            }

            if (book != null) {
                bookResult.setText("Title: " + book.getTitle() + System.lineSeparator() +
                        "Author: " + book.getAuthor());
                bookToRequest = book;

            } else {
                error.setTitle("ERROR");
                error.setHeaderText("Can't find book with the provided info!");
                error.showAndWait();

                clearBookFields();
            }
        }

        bookValue.clear();
    }

    public void resetBook() {
        clearBookFields();
    }

    public void searchUser() {
        String providedUserInfo = userValue.getText().trim();
        if (providedUserInfo.isEmpty() || providedUserInfo.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("The provided users information must not be empty or blank!");

            alert.showAndWait();
            clearUserFields();

        } else {
            Person user = null;
            if (providedUserInfo.matches("^[A-Za-z].*\\s[A-Za-z].*")) { // this must be users full name
                user = userService.getUserByFullName(providedUserInfo);

            } else if (providedUserInfo.contains("@")) { // this must be users email address
                user = userService.getUserByEmail(providedUserInfo);

            } else { // this must be the users id
                try {
                    int id = Integer.parseInt(providedUserInfo);
                    user = userService.getUserById(id);
                } catch (NumberFormatException e) {
                }
            }

            if (user == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("There is no user with id, name or email: \"" + providedUserInfo + "\" registered into the system.");

                alert.showAndWait();
                clearUserFields();
            } else {
                if (user instanceof Borrower) {
                    userResult.setText("ID: " + user.getId() + System.lineSeparator() +
                            "Name: " + user.getFirstName() + " " + user.getLastName());
                    borrower = (Borrower) user;

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("The provided user is not Borrower!");
                    alert.showAndWait();
                    clearUserFields();

                    clearUserFields();
                }
            }
        }

        userValue.clear();
    }

    public void resetUser() {
        clearUserFields();
    }

    public void confirmHoldRequest() {
        if (bookToRequest != null && borrower != null) {
            HoldRequest holdRequest = new HoldRequest(borrower, bookToRequest);
            int result = DatabaseInteractions.createHoldRequest(holdRequest);

            if (result > 0) {
                holdReqService.createHoldRequest(holdRequest);

                Alert confirmationMsg = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationMsg.setTitle("INFO");
                confirmationMsg.setHeaderText("Hold request successfully created!");
                confirmationMsg.showAndWait();
            } else {

                Alert errorMsg = new Alert(Alert.AlertType.ERROR);
                errorMsg.setTitle("ERROR");
                errorMsg.setHeaderText("Problem appeared during the db update. Please contact the system administrator!");
                errorMsg.showAndWait();
            }

            clearBookFields();
            if (!(library.getLoggedInPerson() instanceof Borrower)) {
                clearUserFields();
            }
        }
    }

    private void clearBookFields() {
        bookValue.clear();
        bookResult.clear();
        bookToRequest = null;
    }

    private void clearUserFields() {
        userValue.clear();
        userResult.clear();
        borrower = null;
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
            stage = (Stage) createOnHoldPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }

}
