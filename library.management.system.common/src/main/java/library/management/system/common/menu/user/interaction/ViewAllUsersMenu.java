package library.management.system.common.menu.user.interaction;

import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.menu.Menu;
import library.management.system.common.services.UserManagementService;

import java.util.List;

public class ViewAllUsersMenu implements Menu {

    private Library library = Library.getInstance();
    private UserManagementService userService = UserManagementService.getInstance();

    @Override
    public void start() {
        printMenuHeader();

        List<Person> users = userService.getUsers();
        for (Person user : users) {
            user.printInfo();
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("=========  All Users Registered In Library  ========");
        System.out.println("----------------------------------------------------");
    }
}
