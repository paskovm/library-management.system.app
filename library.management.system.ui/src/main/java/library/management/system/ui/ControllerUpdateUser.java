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
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Clerk;
import library.management.system.common.entities.actors.Librarian;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.services.UserManagementService;
import library.management.system.common.utils.DatabaseInteractions;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerUpdateUser implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Library library = Library.getInstance();
    private UserManagementService userService = UserManagementService.getInstance();
    @FXML
    private Label updateUserTopLabel;
    @FXML
    private TextArea userToUpdate;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField address;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private Label salary;
    @FXML
    private TextField salaryValue;
    @FXML
    private Label office;
    @FXML
    private TextField officeNumberValue;
    @FXML
    private Label desk;
    @FXML
    private TextField deskNumberValue;
    private Person user;
    private List<TextField> lisOfFields = new ArrayList<>();
    @FXML
    private AnchorPane updateUsersPane;

    public ControllerUpdateUser(Person user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (user != null) {
            userToUpdate.setText(user.toString());

            if (user instanceof Librarian) {
                updateUserTopLabel.setText("Update Librarian Info Portal");
                desk.setVisible(false);
                deskNumberValue.setVisible(false);
            } else if (user instanceof Clerk) {
                updateUserTopLabel.setText("Update Clerk Info Portal");
                office.setVisible(false);
                officeNumberValue.setVisible(false);
            } else if (user instanceof Borrower) {
                updateUserTopLabel.setText("Update Borrower Info Portal");
                desk.setVisible(false);
                deskNumberValue.setVisible(false);

                office.setVisible(false);
                officeNumberValue.setVisible(false);

                salary.setVisible(false);
                salaryValue.setVisible(false);
            }
        }

        lisOfFields.add(firstName);
        lisOfFields.add(lastName);
        lisOfFields.add(address);
        lisOfFields.add(phoneNumber);
        lisOfFields.add(email);
        lisOfFields.add(password);
        lisOfFields.add(salaryValue);
        lisOfFields.add(officeNumberValue);
        lisOfFields.add(deskNumberValue);

    }

    public void submitUpdate(ActionEvent event) {
        String fname = firstName.getText();
        String lname = lastName.getText();
        String addr = address.getText();
        String emil = email.getText();
        String pass = password.getText();

        try {
            long phNum = phoneNumber.getText().length() > 0 ? Long.parseLong(phoneNumber.getText()) : 0;
            double sal = salaryValue.getText().length() > 0 ? Double.parseDouble(salaryValue.getText()) : 0;
            int dsk = deskNumberValue.getText().length() > 0 ? Integer.parseInt(deskNumberValue.getText()) : 0;
            int off = officeNumberValue.getText().length() > 0 ? Integer.parseInt(officeNumberValue.getText()) : 0;


            if ((fname != null && !fname.isEmpty() && !fname.isEmpty()) ||
                    (lname != null && !lname.isBlank() && !lname.isEmpty()) ||
                    (addr != null && !addr.isBlank() && !addr.isEmpty()) ||
                    phNum > 0 ||
                    (emil != null && !emil.isBlank() && !emil.isEmpty()) ||
                    (pass != null && !pass.isBlank() && !pass.isEmpty()) ||
                    sal > 0 || dsk > 0 || off > 0) {

                boolean continueWithUpdate = true;
                if (user instanceof Clerk && dsk > 0) {
                    if (userService.checkClerksDeskOccupied(dsk)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("The provided desk is already occupied! Please, provide new desk number and Update the user record.");

                        alert.showAndWait();
                        deskNumberValue.clear();
                        continueWithUpdate = false;
                    }
                }

                if (continueWithUpdate) {
                    int result = DatabaseInteractions.updatePerson(user.getId(), fname, lname, addr, phNum, emil, pass, off, dsk, sal);

                    if (result > 0) {
                        userService.updateUser(user, fname, lname, addr, phNum, emil, pass, sal, off, dsk);
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("User updated successfully!" + user.toString());

                    alert.showAndWait();

                    for (TextField field : lisOfFields)
                        field.clear();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("There is nothing to be updated!");

                alert.showAndWait();
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Invalid data provided!");

            error.showAndWait();
        }
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
            stage = (Stage) updateUsersPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }

}
