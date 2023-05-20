package library.management.system.common.menu.main;

import library.management.system.common.entities.Library;
import library.management.system.common.menu.Menu;
import library.management.system.common.menu.book.interaction.CheckIssuedBooksMenu;
import library.management.system.common.menu.book.interaction.ViewAllBooksMenu;
import library.management.system.common.menu.user.interaction.AddClerkMenu;
import library.management.system.common.menu.user.interaction.AddLibrarianMenu;
import library.management.system.common.menu.user.interaction.UpdateClerksInfoMenu;
import library.management.system.common.menu.user.interaction.ViewAllUsersMenu;

import java.util.Scanner;

public class AdminMenu implements Menu {

    private Library library = Library.getInstance();

    @Override
    public void start() {
        printMenuHeader();
        Menu menuToNavigate;

        library.setMainMenu(this);

        MAIN:
        while (true) {
            try {
                System.out.println("Please, enter your choice: ");
                Scanner scanner = new Scanner(System.in);
                Integer choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        menuToNavigate = new AddClerkMenu();
                        break MAIN;
                    case 2:
                        menuToNavigate = new UpdateClerksInfoMenu();
                        break MAIN;
                    case 3:
                        menuToNavigate = new AddLibrarianMenu();
                        break MAIN;
                    case 4:
                        menuToNavigate = new ViewAllUsersMenu();
                        break MAIN;
                    case 5:
                        menuToNavigate = new CheckIssuedBooksMenu();
                        break MAIN;
                    case 6:
                        menuToNavigate = new ViewAllBooksMenu();
                        break MAIN;
                    case 7:
                        System.out.println("Logging out ...." + System.lineSeparator());
                        Thread.sleep(500);
                        menuToNavigate = new MainMenu();
                        break MAIN;
                    default:
                        System.out.println("Wrong choice provided: " + choice + ". Please, try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("The choice you are providing must be number between 1-7. Please, try again.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        menuToNavigate.start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("=============  Welcome to Admins Portal  ===========");
        System.out.println("----------------------------------------------------");
        System.out.println("Following Functionalities are available: " + System.lineSeparator());

        System.out.println("1- Add Clerk");
        System.out.println("2- Update Clerks info");
        System.out.println("3- Add Librarian");
        System.out.println("4- View All registered Users in library");
        System.out.println("5- View Issued Books History");
        System.out.println("6- View All Books in Library");
        System.out.println("7- Logout");
        System.out.println("----------------------------------------------------");
    }
}
