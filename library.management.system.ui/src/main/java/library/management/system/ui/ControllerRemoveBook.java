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
import library.management.system.common.entities.Book;
import library.management.system.common.entities.HoldRequest;
import library.management.system.common.entities.Library;
import library.management.system.common.entities.Loan;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Clerk;
import library.management.system.common.entities.actors.Librarian;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.services.BookManagementService;
import library.management.system.common.services.HoldRequestManagementService;
import library.management.system.common.services.LoanManagementService;
import library.management.system.common.utils.DatabaseInteractions;

import java.io.IOException;
import java.util.ListIterator;
import java.util.Optional;

public class ControllerRemoveBook {

    @FXML
    private TextField input;
    @FXML
    private TextArea result;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Library library = Library.getInstance();
    private BookManagementService bookService = BookManagementService.getInstance();
    private Book bookToBeDeleted;
    private LoanManagementService loanService = LoanManagementService.getInstance();
    private HoldRequestManagementService holdRequestService = HoldRequestManagementService.getInstance();
    @FXML
    private AnchorPane removeBookPane;


    public void search() {
        String userInput = input.getText();
        int id = -1;
        Book book;

        try {
            id = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
        }

        if (id > 0) {
            book = bookService.getBookById(id);
        } else {
            book = bookService.getBookByTitle(userInput);
        }

        if (book != null) {
            bookToBeDeleted = book;
            result.setText(book.toString());

        } else {
            Alert noSuchBookError = new Alert(Alert.AlertType.ERROR);
            noSuchBookError.setTitle("ERROR");
            noSuchBookError.setHeaderText("No book in the library matches the provided from you input!");
            noSuchBookError.showAndWait();

            reset();
        }
    }

    public void delete() {
        if (bookToBeDeleted != null) {
            Alert deleteBookWarning = new Alert(Alert.AlertType.WARNING);
            deleteBookWarning.setTitle("ATTENTION");
            deleteBookWarning.setHeaderText("Are you sure you want to delete the book!");
            Optional<ButtonType> answer = deleteBookWarning.showAndWait();

            if (answer.get().equals(ButtonType.OK)) {
                int result = DatabaseInteractions.removeBook(bookToBeDeleted.getBookId());

                if (result < 0) { // if no book id is returned that means it is successfully deleted
                    ListIterator<Loan> loans = loanService.getLoansForBook(bookToBeDeleted).listIterator();
                    while (loans.hasNext()) {
                        loanService.removeLoan(loans.next());
                    }

                    ListIterator<HoldRequest> holdRequests = holdRequestService.getHoldRequestsForBook(bookToBeDeleted).listIterator();
                    while (holdRequests.hasNext()) {
                        holdRequestService.removeHoldRequest(holdRequests.next());
                    }

                    bookService.removeBook(bookToBeDeleted);

                    Alert successMessage = new Alert(Alert.AlertType.CONFIRMATION);
                    successMessage.setTitle("SUCCESS");
                    successMessage.setHeaderText("The book removed from the system!");
                    successMessage.showAndWait();

                    reset();

                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("ERROR");
                    error.setHeaderText("Please contact the system administrator!");
                    error.showAndWait();
                }
            }

        }
    }

    public void reset() {
        input.clear();
        result.clear();
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
            stage = (Stage) removeBookPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }
}
