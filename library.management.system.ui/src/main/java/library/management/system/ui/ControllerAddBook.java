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
import javafx.scene.control.TextField;
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
import java.util.List;

public class ControllerAddBook {

    @FXML
    private TextField title;
    @FXML
    private TextField author;
    @FXML
    private TextField genre;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private AnchorPane addBookPane;
    private Library library = Library.getInstance();
    private BookManagementService bookService = BookManagementService.getInstance();

    public void addBook() {
        String title = this.title.getText();
        String author = this.author.getText();
        String genre = this.genre.getText();

        if (title != null && !title.isEmpty() && !title.isBlank() &&
            author != null && !author.isEmpty() && !author.isBlank() &&
            genre != null && !genre.isEmpty() && !genre.isBlank()) {

            if (!bookExists(title, author)) {
                int id = DatabaseInteractions.createBook(title, author, genre);
                if (id > 0) {
                    Book book = new Book(id, title, author, genre);
                    bookService.addBook(book);

                    Alert infoMessage = new Alert(Alert.AlertType.INFORMATION);
                    infoMessage.setTitle("SUCCESS");
                    infoMessage.setHeaderText("Book added in the system!" + System.lineSeparator() + book);
                    infoMessage.showAndWait();

                    this.title.clear();
                    this.author.clear();
                    this.genre.clear();

                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("ERROR");
                    error.setHeaderText("Unknown error appeared. Please contact system administrator!");
                    error.showAndWait();
                }

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("ERROR");
                error.setHeaderText("Book with title " + title + " and author " + author + " already exists in the system!");
                error.showAndWait();
            }


        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("ERROR");
            error.setHeaderText("Empty value provided!");
            error.showAndWait();
        }
    }

    private boolean bookExists(String title, String author) {
        List<Book> books = bookService.getAllBooksInLibrary();
        for (Book book : books) {
            if (book.getTitle().equals(title) && book.getAuthor().equals(author)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        title.clear();
        author.clear();
        genre.clear();
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
            stage = (Stage) addBookPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }
}
