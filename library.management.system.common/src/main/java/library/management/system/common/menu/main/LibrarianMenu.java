package library.management.system.common.menu.main;


import library.management.system.common.entities.Library;
import library.management.system.common.menu.Menu;
import library.management.system.common.menu.book.interaction.*;
import library.management.system.common.menu.user.interaction.*;

import java.util.Scanner;


public class LibrarianMenu implements Menu {

    private Library library = Library.getInstance();

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
                        menuToNavigate = new CheckBorrowerInfoMenu();
                        break MAIN;
                    case 4:
                        menuToNavigate = new CheckBorrowerFineMenu();
                        break MAIN;
                    case 5:
                        menuToNavigate = new CheckBookHoldReqMenu();
                        break MAIN;
                    case 6:
                        menuToNavigate = new IssueBookMenu();
                        break MAIN;
                    case 7:
                        menuToNavigate = new ReturnBookMenu();
                        break MAIN;
                    case 8:
                        menuToNavigate = new RenewBookMenu();
                        break MAIN;
                    case 9:
                        menuToNavigate = new AddBorrowerMenu();
                        break MAIN;
                    case 10:
                        menuToNavigate = new UpdateBorrowerInfoMenu();
                        break MAIN;
                    case 11:
                        menuToNavigate = new AddNewBookMenu();
                        break MAIN;
                    case 12:
                        menuToNavigate = new RemoveBookMenu();
                        break MAIN;
                    case 13:
                        menuToNavigate = new UpdateBookInfoMenu();
                        break MAIN;
                    case 14:
                        menuToNavigate = new CheckClerkInfoMenu();
                        break MAIN;
                    case 15:
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
                           "----------------------------------------------------");
        System.out.println("=========== Welcome to Librarians Portal ==========");
        System.out.println("----------------------------------------------------");
        System.out.println("Following Functionalities are available: " + System.lineSeparator());
        System.out.println("1- Search a Book");
        System.out.println("2- Place a Book on hold");
        System.out.println("3- Check Personal Info of Borrower");
        System.out.println("4- Check Total Fine of Borrower");
        System.out.println("5- Check Hold Requests Queue of a Book");
        System.out.println("6- Check out a Book(Borrow a Book)");
        System.out.println("7- Check in a Book(Return a Book)");
        System.out.println("8- Renew a Book");
        System.out.println("9- Add a new Borrower");
        System.out.println("10- Update a Borrower's Info");
        System.out.println("11- Add new Book");
        System.out.println("12- Remove a Book");
        System.out.println("13- Change a Book's Info");
        System.out.println("14- Check Personal Info of Clerk");
        System.out.println("15- Logout");
        System.out.println("----------------------------------------------------");
    }
}
