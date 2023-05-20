package library.management.system.common.menu.user.interaction;

import library.management.system.common.entities.Library;
import library.management.system.common.menu.help.AddUpdateCheckUserMenu;

public class AddBorrowerMenu extends AddUpdateCheckUserMenu {

    private Library library = Library.getInstance();

    @Override
    public void start() {
        printMenuHeader();
        createUser("Borrower");

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("=============  Add New Borrower Portal  ============");
        System.out.println("----------------------------------------------------");
    }
}
