package library.management.system.common.menu.main;

import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.menu.Menu;
import library.management.system.common.menu.book.interaction.CheckBookHoldReqMenu;
import library.management.system.common.menu.book.interaction.PlaceBookOnHoldMenu;
import library.management.system.common.menu.book.interaction.SearchBookMenu;
import library.management.system.common.services.LoanManagementService;

import java.util.Scanner;

public class BorrowerMenu implements Menu {

    private Library library = Library.getInstance();
    private LoanManagementService loanService = LoanManagementService.getInstance();

    @Override
    public void start() {
        printMenuHeader();
        library.setMainMenu(this);
        Menu menuToNavigate;

        Scanner scanner = new Scanner(System.in);

        MAIN:
        while (true) {
            try {
                System.out.println("Please, enter your choice: ");
                Integer choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        menuToNavigate = new SearchBookMenu();
                        break MAIN;
                    case 2:
                        menuToNavigate = new PlaceBookOnHoldMenu();
                        break MAIN;
                    case 3:
                        library.getLoggedInPerson().printInfo();
                        menuToNavigate = this;
                        break MAIN;
                    case 4:
                        loanService.printBorrowersFines((Borrower) library.getLoggedInPerson());
                        menuToNavigate = this;
                        break MAIN;
                    case 5:
                        menuToNavigate = new CheckBookHoldReqMenu();
                        break MAIN;
                    case 6:
                        library.setLoggedInPerson(null);
                        System.out.println("Logging out ...." + System.lineSeparator());
                        Thread.sleep(500);
                        menuToNavigate = new MainMenu();
                        break MAIN;
                    default:
                        System.out.println("Wrong choice selected! You need to enter number between 1-15.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Wrong choice provided! Please, try again.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        menuToNavigate.start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                           "--------------------------------------------------------");
        System.out.println("=============  Welcome to Borrowers Portal  ============");
        System.out.println("--------------------------------------------------------");
        System.out.println("Following Functionalities are available: " + System.lineSeparator());
        System.out.println("1- Search a Book");
        System.out.println("2- Place a Book on hold");
        System.out.println("3- Check Personal Info of Borrower");
        System.out.println("4- Check Total Fine of Borrower");
        System.out.println("5- Check Hold Requests Queue of a Book");
        System.out.println("6- Logout");
        System.out.println("--------------------------------------------------------");
    }
}
