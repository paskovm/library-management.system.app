package library.management.system.common.menu.user.interaction;

import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Librarian;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.menu.help.AddUpdateCheckUserMenu;
import library.management.system.common.services.UserManagementService;

import java.util.List;
import java.util.Scanner;

public class AddLibrarianMenu extends AddUpdateCheckUserMenu {

    private Library library = Library.getInstance();
    private UserManagementService userService = UserManagementService.getInstance();
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void start() {
        printMenuHeader();

        Librarian librarian = null;
        List<Person> users = userService.getUsers();
        for (Person user : users) {
            if (user instanceof Librarian) {
                librarian = (Librarian) user;
                break;
            }
        }

        if (librarian != null) {
            System.out.println("Librarian already exists!");
            librarian.printInfo();

            while (true) {
                System.out.println(System.lineSeparator() + "Do you want to update the librarian info? y/n: ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("y")) {
                    updateUser(librarian);
                    break;
                } else if (input.equalsIgnoreCase("n")) {
                    break;

                } else {
                    System.out.println("Wrong choice provided! Please, try again.");
                }
            }
        } else {
            createUser("Librarian");
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("==============  Add Librarian Portal  ==============");
        System.out.println("----------------------------------------------------");
    }
}
