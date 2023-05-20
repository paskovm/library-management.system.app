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
import library.management.system.common.entities.Library;
import library.management.system.common.entities.Loan;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Clerk;
import library.management.system.common.entities.actors.Librarian;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.services.LoanManagementService;
import library.management.system.common.services.UserManagementService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerBorrowersFines implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Library library = Library.getInstance();
    private UserManagementService userService = UserManagementService.getInstance();
    private LoanManagementService loanService = LoanManagementService.getInstance();
    @FXML
    private TextArea usersInfo;
    @FXML
    private Label userInputLabel;
    @FXML
    private TextField userInput;
    @FXML
    private Button submitButton;
    @FXML
    private Button resetButton;
    @FXML
    private TextArea finesInfo;
    @FXML
    private AnchorPane borrowerFinePane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Person loggedInUser = library.getLoggedInPerson();
        if (loggedInUser instanceof Borrower) {
            userInputLabel.setVisible(false);
            userInput.setVisible(false);
            submitButton.setVisible(false);
            resetButton.setVisible(false);

            usersInfo.setText("ID: " + loggedInUser.getId() + System.lineSeparator() +
                    "Name: " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName());

            displayUsersFines((Borrower) loggedInUser);
        }
    }

    public void submitUserSearch() {
        String providedUsersInfo = userInput.getText().trim();
        if (providedUsersInfo.isEmpty() || providedUsersInfo.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("The provided users information must not be empty or blank!");

            alert.showAndWait();
            clearUserToUpdate();

        } else {
            Person user = null;

            if (providedUsersInfo.matches("^[A-Za-z].*\\s[A-Za-z].*")) { // this must be users full name
                user = userService.getUserByFullName(providedUsersInfo);

            } else if (providedUsersInfo.contains("@")) { // this must be users email address
                user = userService.getUserByEmail(providedUsersInfo);

            } else { // this must be the users id
                try {
                    int id = Integer.parseInt(providedUsersInfo);
                    user = userService.getUserById(id);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            if (user == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("There is no user with id, name or email: \"" + providedUsersInfo + "\" registered into the system.");

                alert.showAndWait();
                clearUserToUpdate();
            } else {
                if (user instanceof Librarian || user instanceof Clerk) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING");
                    alert.setHeaderText("The provided user is not Borrower. Please, try again!");

                    alert.showAndWait();
                    clearUserToUpdate();

                } else {

                    displayUsersFines((Borrower) user);
                }
            }
        }
    }

    private void displayUsersFines(Borrower user) {
        usersInfo.setText("ID: " + user.getId() + System.lineSeparator() + "Name: " + user.getFirstName() + " " + user.getLastName());
        List<Loan> notCompletedLoans = loanService.getBorrowersFines((Borrower) user);

        if (notCompletedLoans.size() > 0) {
            String allInfo = "";
            double finesSum = 0;

            for (Loan loan : notCompletedLoans) {
                allInfo += "Book: " + loan.getBook().getTitle() + System.lineSeparator() +
                        "Loaned date: " + loan.getIssueDate() + System.lineSeparator() +
                        "Fine: " + loan.getBorrowersFine() + "\n\n";
                finesSum += loan.getBorrowersFine();
            }

            allInfo += "All fines: " + finesSum;
            finesInfo.setText(allInfo);

        } else {
            finesInfo.setText("The user has no active loans. " + System.lineSeparator() + "No fines to pay!");
        }
    }

    public void clearUserToUpdate() {
        usersInfo.clear();
        userInput.clear();
        finesInfo.clear();
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
            stage = (Stage) borrowerFinePane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }
}
