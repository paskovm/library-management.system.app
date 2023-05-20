package library.management.system.common.menu.book.interaction;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.Library;
import library.management.system.common.entities.Loan;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.entities.actors.Staff;
import library.management.system.common.menu.help.AddUpdateCheckUserMenu;
import library.management.system.common.services.LoanManagementService;

public class IssueBookMenu extends AddUpdateCheckUserMenu {

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
                    if (!book.isIssued()) {
                        Staff staff = (Staff) library.getLoggedInPerson();
                        Loan loan = new Loan((Borrower) user, book, staff);
                        loanService.issueBook(loan);
                        System.out.println("Book successfully loaned.");
                        loan.printInfo();

                    } else {
                        System.out.println("The book is already loaned!");
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
        System.out.println("===============  Loan a Book Portal  ===============");
        System.out.println("----------------------------------------------------");
    }
}
