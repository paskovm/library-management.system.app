package library.management.system.common.menu.book.interaction;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.Library;
import library.management.system.common.entities.Loan;
import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Person;
import library.management.system.common.entities.actors.Staff;
import library.management.system.common.menu.help.AddUpdateCheckUserMenu;
import library.management.system.common.services.LoanManagementService;

import java.time.LocalDate;

public class RenewBookMenu extends AddUpdateCheckUserMenu {

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
                        loan.setIssuer((Staff) library.getLoggedInPerson());
                        loan.setIssueDate(LocalDate.now());
                        loan.getBorrower().updateLoan(loan.getBook(), loan.getIssuer());

                        System.out.println("Loan extended successfully!");

                    } else {
                        System.out.println("There is no such loan registered in the system!");
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
        System.out.println("===============  Renew a Book Portal  ==============");
        System.out.println("----------------------------------------------------");
    }
}
