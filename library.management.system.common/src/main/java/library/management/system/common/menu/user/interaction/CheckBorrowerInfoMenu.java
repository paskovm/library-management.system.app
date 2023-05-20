package library.management.system.common.menu.user.interaction;

import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.menu.help.AddUpdateCheckUserMenu;

public class CheckBorrowerInfoMenu extends AddUpdateCheckUserMenu {

    private Library library = Library.getInstance();

    @Override
    public void start() {
        printMenuHeader();
        Person user = getUser();
        if (user != null) {
            if (user instanceof Borrower) {
                user.printInfo();
            } else {
                System.out.println("The user you provided is not borrower!");
            }
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("==============  Borrowers Info Portal  =============");
        System.out.println("----------------------------------------------------");
    }
}
