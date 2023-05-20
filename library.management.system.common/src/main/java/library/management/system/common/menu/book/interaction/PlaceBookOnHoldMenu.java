package library.management.system.common.menu.book.interaction;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.HoldRequest;
import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.menu.help.AddUpdateCheckUserMenu;
import library.management.system.common.services.HoldRequestManagementService;

public class PlaceBookOnHoldMenu extends AddUpdateCheckUserMenu {

    private Library library = Library.getInstance();
    private HoldRequestManagementService holdRequestService = HoldRequestManagementService.getInstance();

    @Override
    public void start() {
        printMenuHeader();
        Person user;
        Person loggedInPerson = library.getLoggedInPerson();

        if (loggedInPerson instanceof Borrower) {
            user = loggedInPerson;
        } else {
            user = getUser();
        }

        if (user != null) {
            if (user instanceof Borrower) {
                Book book = getBook();
                if (book != null) {
                    HoldRequest holdRequest = new HoldRequest((Borrower) user, book);
                    holdRequestService.createHoldRequest(holdRequest);
                }
            } else {
                System.out.println("The user who requested the hold request is not registered as borrower in the system!");
            }
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("===========  Book On Hold Request Portal  ==========");
        System.out.println("----------------------------------------------------");
    }
}
