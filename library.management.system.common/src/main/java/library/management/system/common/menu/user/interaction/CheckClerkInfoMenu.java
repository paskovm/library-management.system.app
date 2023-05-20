package library.management.system.common.menu.user.interaction;

import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Clerk;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.menu.help.AddUpdateCheckUserMenu;

public class CheckClerkInfoMenu extends AddUpdateCheckUserMenu {

    private Library library = Library.getInstance();

    @Override
    public void start() {
        printMenuHeader();
        Person user = getUser();

        if (user != null) {
            if (user instanceof Clerk) {
                user.printInfo();
            } else {
                System.out.println("User you requested to verify is not a clerk!");
            }
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("================  Clerk Info Portal  ===============");
        System.out.println("----------------------------------------------------");
    }
}
