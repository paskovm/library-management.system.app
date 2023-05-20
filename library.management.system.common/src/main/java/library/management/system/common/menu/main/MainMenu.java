package library.management.system.common.menu.main;

import library.management.system.common.entities.Library;
import library.management.system.common.menu.Menu;

import java.util.Scanner;

public class MainMenu implements Menu {

    private Library library = Library.getInstance();

    @Override
    public void start() {
        library.setMainMenu(this);

        printMenuHeader();
        Menu menuToNavigate;

        MAIN:
        while (true) {
            System.out.println("Please, enter your choice: ");
            Scanner scanner = new Scanner(System.in);
            try {
                Integer choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        menuToNavigate = new LogInMenu();
                        break MAIN;
                    case 2:
                        System.out.println("Exiting the program ....");
                        System.exit(0);
                    default:
                        System.out.println("Wrong choice provided: " + choice + ". Please, try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("The choice you are providing must be a number between 1-2. Please, try again.");
            }
        }
        menuToNavigate.start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println("----------------------------------------------------");
        System.out.println("====  Welcome to the Library Management System  ====");
        System.out.println("----------------------------------------------------");
        System.out.println("The following functionalities are available:" + System.lineSeparator());
        System.out.println("1 - Login");
        System.out.println("2 - Exit");
        System.out.println("----------------------------------------------------");
    }
}
