package library.management.system.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Clerk;
import library.management.system.common.entities.actors.Librarian;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.services.BookManagementService;
import library.management.system.common.services.HoldRequestManagementService;
import library.management.system.common.utils.HoldReqComparator;

import java.io.IOException;
import java.util.List;

public class ControllerListBookHoldReq {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Library library = Library.getInstance();
    private HoldRequestManagementService holdReqService = HoldRequestManagementService.getInstance();
    private BookManagementService bookService = BookManagementService.getInstance();
    private ObservableList<HoldRequest> holdRequestsList = FXCollections.observableArrayList();
    @FXML
    private TextField input;
    @FXML
    private ListView<HoldRequest> results;
    @FXML
    private AnchorPane listHoldReqPane;

    public void search() {
        results.getItems().clear();
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
            List<HoldRequest> holdRequests = holdReqService.getHoldRequestsForBook(book);

            if (holdRequests.size() > 0) {
                for (HoldRequest hr :holdRequests) {
                    holdRequestsList.add(hr);
                }
                holdRequestsList.sort(new HoldReqComparator());
                results.setItems(holdRequestsList);

            } else {
                results.setPlaceholder(new Label("There are no Hold Requests for the book \"" + book.getTitle() + "\"."));
            }

        } else {
            Alert noSuchBookError = new Alert(Alert.AlertType.ERROR);
            noSuchBookError.setTitle("ERROR");
            noSuchBookError.setHeaderText("No book in the library matches the provided from you input!");
            noSuchBookError.showAndWait();

            reset();
        }
    }

    public void reset() {
        input.clear();
        results.getItems().clear();
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
            stage = (Stage) listHoldReqPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }
}
