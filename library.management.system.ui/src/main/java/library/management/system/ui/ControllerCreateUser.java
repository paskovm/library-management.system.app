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
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerCreateUser implements Initializable {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField address;
    @FXML
    private TextField phone;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Label salaryLabel;
    @FXML
    private TextField salary;
    @FXML
    private Label officeLabel;
    @FXML
    private TextField office;
    @FXML
    private Label deskLabel;
    @FXML
    private TextField desk;
    @FXML
    private Label menuHeader;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private UserType userType;
    private Library library = Library.getInstance();
    private UserManagementService userService = UserManagementService.getInstance();
    private List<TextField> fields = new ArrayList<>();
    @FXML
    private AnchorPane createUserPane;


    public ControllerCreateUser(UserType userType) {
        this.userType = userType;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (userType == UserType.CLERK) {
            menuHeader.setText("Add New Clerk Portal");

            officeLabel.setVisible(false);
            office.setVisible(false);

        } else if (userType == UserType.BORROWER) {
            menuHeader.setText("Add New Borrower Portal");

            salaryLabel.setVisible(false);
            salary.setVisible(false);

            officeLabel.setVisible(false);
            office.setVisible(false);

            deskLabel.setVisible(false);
            desk.setVisible(false);

        } else if (userType == UserType.LIBRARIAN) {
            menuHeader.setText("Add New Librarian Portal");

            deskLabel.setVisible(false);
            desk.setVisible(false);
        }

        fields.add(firstName);
        fields.add(lastName);
        fields.add(address);
        fields.add(phone);
        fields.add(email);
        fields.add(password);
        fields.add(salary);
        fields.add(office);
        fields.add(desk);
    }

    public void submit(ActionEvent event) {

        if (isValidInfoProvided()) {
            if (!personAlreadyExists()) {
                int result = -1;
                Person user = null;

                if (userType == UserType.LIBRARIAN) {
                    Person librarian = librarianAlreadyExists();
                    if (librarian.getId() > 0) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("LIBRARIAN ALREADY REGISTERED IN THE SYSTEM!");
                        alert.setHeaderText("Do you want to replace the current Librarian?" + System.lineSeparator() +
                                "Current Librarian info: " + System.lineSeparator() + librarian);
                        Optional<ButtonType> choice = alert.showAndWait();

                        if (choice.get().equals(ButtonType.OK)) {
                            result = DatabaseInteractions.updatePerson(librarian.getId(), firstName.getText(), lastName.getText(), address.getText(), Long.parseLong(phone.getText()),
                                    email.getText(), password.getText(), Integer.parseInt(office.getText()), -1, Double.parseDouble(salary.getText()));
                            if (result > 0) {
                                userService.updateUser(librarian, firstName.getText(), lastName.getText(), address.getText(), Long.parseLong(phone.getText()),
                                        email.getText(), password.getText(), Double.parseDouble(salary.getText()), Integer.parseInt(office.getText()), -1);
                            }

                            Alert infoMessage = new Alert(Alert.AlertType.INFORMATION);
                            infoMessage.setTitle("Librarian updated successfully!");
                            infoMessage.setHeaderText(librarian.toString());
                            infoMessage.showAndWait();

                            for (TextField field : fields) {
                                field.clear();
                            }
                            return;
                        }
                    } else {
                        result = DatabaseInteractions.createPerson(firstName.getText(), lastName.getText(), address.getText(), Long.parseLong(phone.getText()),
                                email.getText(), password.getText(), Integer.parseInt(office.getText()), -1, Double.parseDouble(salary.getText()));
                    }

                } else if (userType == UserType.CLERK) {
                    result = DatabaseInteractions.createPerson(firstName.getText(), lastName.getText(), address.getText(), Long.parseLong(phone.getText()),
                            email.getText(), password.getText(), -1, Integer.parseInt(desk.getText()), Double.parseDouble(salary.getText()));
                } else {
                    result = DatabaseInteractions.createPerson(firstName.getText(), lastName.getText(), address.getText(), Long.parseLong(phone.getText()),
                            email.getText(), password.getText(), -1, -1, -1);
                }

                if (result > 0) { // The user must be created successfully and the result contains the user id
                    if (userType == UserType.LIBRARIAN) {
                        user = new Librarian(result, firstName.getText(), lastName.getText(), address.getText(), Long.parseLong(phone.getText()),
                                email.getText(), password.getText(), Double.parseDouble(salary.getText()), Integer.parseInt(office.getText()));

                    } else if (userType == UserType.CLERK) {
                        user = new Clerk(result, firstName.getText(), lastName.getText(), address.getText(), Long.parseLong(phone.getText()),
                                email.getText(), password.getText(), Double.parseDouble(salary.getText()), Integer.parseInt(desk.getText()));

                    } else {
                        user = new Borrower(result, firstName.getText(), lastName.getText(), address.getText(), Long.parseLong(phone.getText()),
                                email.getText(), password.getText());
                    }
                }

                if (user != null) {
                    userService.createUser(user);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("User created successfully!");
                    alert.setHeaderText(user.toString());
                    alert.showAndWait();

                    for (TextField field : fields) {
                        field.clear();
                    }
                }
            }
        }
    }

    private Person librarianAlreadyExists() {
        List<Person> users = userService.getUsers();
        for (Person user : users) {
            if (user instanceof Librarian) {
                return user;
            }
        }
        return null;
    }

    private boolean personAlreadyExists() {
        List<Person> users = userService.getUsers();
        for (Person user : users) {
            if (user.getFirstName().equals(firstName.getText()) && user.getLastName().equals(lastName.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User already exists!");
                alert.setHeaderText("Person with First Name: " + firstName.getText() + " and Last Name: " + lastName.getText() + " already exists!");
                alert.showAndWait();
                return true;
            }

            if (user.getEmail().equals(email.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User already exists!");
                alert.setHeaderText("Person with email address: " + email.getText() + " already exists!");
                alert.showAndWait();
                return true;
            }
        }
        return false;
    }

    private boolean isValidInfoProvided() {
        if (firstName.getText().isEmpty() || firstName.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have provided empty value for the users First Name");
            alert.setHeaderText("Please, provide users First Name!");
            alert.showAndWait();
            return false;
        }

        if (lastName.getText().isEmpty() || firstName.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have provided empty value for the users Last Name");
            alert.setHeaderText("Please, provide users Last Name!");
            alert.showAndWait();
            return false;
        }

        if (address.getText().isEmpty() || address.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have provided empty value for the users Address");
            alert.setHeaderText("Please, provide users Address!");
            alert.showAndWait();
            return false;
        }

        if (phone.getText().isEmpty() || phone.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have provided empty value for the users Phone number");
            alert.setHeaderText("Please, provide users Phone number!");
            alert.showAndWait();
            return false;
        } else {
            try {
                Long.parseLong(phone.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid number format provided");
                alert.setHeaderText("Please, provide users Phone number!");
                alert.showAndWait();
                return false;
            }
        }

        if (email.getText().isEmpty() || email.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have provided empty value for the users Email address");
            alert.setHeaderText("Please, provide users Email address!");
            alert.showAndWait();
            return false;
        } else if (!email.getText().contains("@")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid email address format provided");
            alert.setHeaderText("Please, provide users Email address!");
            alert.showAndWait();
            return false;
        }

        if (password.getText().isEmpty() || password.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have provided empty value for the users Password");
            alert.setHeaderText("Please, provide users Password!");
            alert.showAndWait();
            return false;
        }

        if (userType == UserType.LIBRARIAN || userType == UserType.CLERK) {

            if (salary.getText().isEmpty() || salary.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("You have provided empty value for the users Salary");
                alert.setHeaderText("Please, provide users Salary!");
                alert.showAndWait();
                return false;
            } else {
                try {
                    Double.parseDouble(salary.getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid number format provided");
                    alert.setHeaderText("Please, provide users Salary!");
                    alert.showAndWait();
                    return false;
                }
            }

            if (userType == UserType.LIBRARIAN) {
                if (office.getText().isEmpty() || office.getText().isBlank()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("You have provided empty value for the users Office number");
                    alert.setHeaderText("Please, provide users Office number!");
                    alert.showAndWait();
                    return false;
                } else {
                    try {
                        Integer.parseInt(office.getText());
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid number format provided");
                        alert.setHeaderText("Please, provide users Office number!");
                        alert.showAndWait();
                        return false;
                    }
                }
            }

            if (userType == UserType.CLERK) {
                if (desk.getText().isEmpty() || desk.getText().isBlank()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("You have provided empty value for the users Desk number");
                    alert.setHeaderText("Please, provide users Desk number!");
                    alert.showAndWait();
                    return false;
                } else {
                    try {
                        Integer.parseInt(desk.getText());
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid number format provided");
                        alert.setHeaderText("Please, provide users Desk number!");
                        alert.showAndWait();
                        return false;
                    }
                }

                if (userService.checkClerksDeskOccupied(Integer.parseInt(desk.getText()))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("You have provided Desk number which is already occupied");
                    alert.setHeaderText("Please, provide new Desk number!");
                    alert.showAndWait();
                    return false;
                }
            }
        }

        return true;
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
            stage = (Stage) createUserPane.getScene().getWindow();

            Thread.sleep(500);
            stage.close();
        }
    }
}
