package library.management.system.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerListAllUsers implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Library library = Library.getInstance();
    private UserManagementService userService = UserManagementService.getInstance();
    private ObservableList<Person> listPerson = FXCollections.observableArrayList();
    @FXML
    private ListView<Person> listAll;
    @FXML
    private AnchorPane listUsersPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Person> users = userService.getUsers();
        for (Person user : users) {
            listPerson.add(user);
        }
        listAll.setItems(listPerson);
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
            stage = (Stage) listUsersPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }
}
