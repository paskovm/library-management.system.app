package library.management.system.common.menu.user.interaction;

import library.management.system.common.entities.Library;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.menu.help.AddUpdateCheckUserMenu;
import library.management.system.common.services.LoanManagementService;

public class CheckBorrowerFineMenu extends AddUpdateCheckUserMenu {

    private Library library = Library.getInstance();
    private LoanManagementService loanService = LoanManagementService.getInstance();

    @Override
    public void start() {
        printMenuHeader();

        Person user = getUser();

        if (user != null) {
            if (user instanceof Borrower) {
                loanService.printBorrowersFines((Borrower) user);
            } else {
                System.out.println("The provided user is not borrower!");
            }
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                "----------------------------------------------------");
        System.out.println("===============  Borrowers Fine Info  ==============");
        System.out.println("----------------------------------------------------");
    }
}
