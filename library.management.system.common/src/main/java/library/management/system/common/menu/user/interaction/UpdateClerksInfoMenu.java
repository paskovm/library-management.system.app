package library.management.system.common.menu.user.interaction;

import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Clerk;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.menu.help.AddUpdateCheckUserMenu;

public class UpdateClerksInfoMenu extends AddUpdateCheckUserMenu {

    private Library library = Library.getInstance();

    @Override
    public void start() {
        printMenuHeader();
        Person clerk = getUser();

        if (clerk != null) {
            if (clerk instanceof Clerk) {
                updateUser(clerk);
            } else {
                System.out.println("The user you are trying to update is not clerk!");
                clerk.printInfo();
            }
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("============  Update Clerks Info Portal  ===========");
        System.out.println("----------------------------------------------------");
    }
}
