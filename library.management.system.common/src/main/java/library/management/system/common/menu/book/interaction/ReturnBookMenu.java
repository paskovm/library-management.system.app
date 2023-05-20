package library.management.system.common.menu.book.interaction;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.Library;
import library.management.system.common.entities.Loan;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.menu.help.AddUpdateCheckUserMenu;
import library.management.system.common.services.LoanManagementService;
import library.management.system.common.utils.DatabaseInteractions;

public class ReturnBookMenu extends AddUpdateCheckUserMenu {

    private Library library = Library.getInstance();
    private LoanManagementService loanService = LoanManagementService.getInstance();

    @Override
    public void start() {
        printMenuHeader();
        Person user = getUser();
        if (user != null) {
            if (user instanceof Borrower) {
                Book book = getBook();

                if (book != null) {
                    Loan loan = loanService.getLoan((Borrower) user, book, false);

                    if (loan != null) {
                        int result = DatabaseInteractions.returnBook(loan);
                        if (result > 0) {
                            loanService.returnBook(loan);
                            System.out.println("Books loan completed!");

                        } else {
                            System.out.println("ERROR! Not able to complete loan due to db error.");
                        }

                    } else {
                        System.out.println("There is no loan registered in the system for user: " + user.getFirstName() + " " + user.getLastName() + " and book: " + book.getTitle());
                    }
                }

            } else {
                System.out.println("The user you provided is not borrower!");
                user.printInfo();
            }
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("==============  Return a Book Portal  ==============");
        System.out.println("----------------------------------------------------");
    }
}
