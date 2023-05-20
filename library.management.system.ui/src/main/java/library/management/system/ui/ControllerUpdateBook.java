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
import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Clerk;
import library.management.system.common.entities.actors.Librarian;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.services.BookManagementService;
import library.management.system.common.utils.DatabaseInteractions;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerUpdateBook implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField userInput;
    @FXML
    private TextArea result;
    @FXML
    private Label updateLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private TextField titleValue;
    @FXML
    private Label authorLabel;
    @FXML
    private TextField authorValue;
    @FXML
    private Label genreLabel;
    @FXML
    private TextField genreValue;
    @FXML
    private Button updateButton;
    private Book bookToUpdate;
    private Library library = Library.getInstance();
    private BookManagementService bookService = BookManagementService.getInstance();
    @FXML
    private AnchorPane updateBookPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideFields();
    }

    public void search() {
        Alert error = new Alert(Alert.AlertType.ERROR);
        if (userInput == null || userInput.getText().isEmpty() || userInput.getText().isBlank()) {
            error.setTitle("ERROR");
            error.setHeaderText("No input info provided!");
            error.showAndWait();
        } else {
            String input = userInput.getText();
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
                result.setText(book.toString());
                bookToUpdate = book;
                unhideFields();
            } else {
                result.setText("No such book found!");
            }
        }
        userInput.clear();
    }

    private void clearUpdateValues() {
        titleValue.clear();
        authorValue.clear();
        genreValue.clear();
    }

    public void reset() {
        userInput.clear();
        result.clear();
        hideFields();
    }

    public void update() {
        if (bookToUpdate == null) {
            return;
        }

        String title = titleValue.getText();
        String author = authorValue.getText();
        String genre = genreValue.getText();

        if ((title != null && !title.isEmpty() && !title.isBlank()) ||
            (author != null && !author.isEmpty() && !author.isBlank()) ||
            (genre != null && !genre.isEmpty() && !genre.isBlank())) {

            int result = DatabaseInteractions.updateBook(bookToUpdate, title, author,genre);

            if (result > 0) {
                bookService.updateBook(bookToUpdate, title, author, genre);

                Alert updateConfirmation = new Alert(Alert.AlertType.INFORMATION);
                updateConfirmation.setTitle("Book Updated!");
                updateConfirmation.setHeaderText(bookToUpdate.toString());
                updateConfirmation.showAndWait();

                reset();
                clearUpdateValues();

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("ERROR");
                error.setHeaderText("Problem appeared during the update to db!");
                error.showAndWait();
            }

        } else {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("INFO");
            info.setHeaderText("There is nothing to update!");
            info.showAndWait();
        }
    }

    private void hideFields() {
        updateLabel.setVisible(false);
        titleLabel.setVisible(false);
        titleValue.setVisible(false);
        authorLabel.setVisible(false);
        authorValue.setVisible(false);
        genreLabel.setVisible(false);
        genreValue.setVisible(false);
        updateButton.setVisible(false);
    }

    private void unhideFields() {
        updateLabel.setVisible(true);
        titleLabel.setVisible(true);
        titleValue.setVisible(true);
        authorLabel.setVisible(true);
        authorValue.setVisible(true);
        genreLabel.setVisible(true);
        genreValue.setVisible(true);
        updateButton.setVisible(true);
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
            stage = (Stage) updateBookPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }
}
