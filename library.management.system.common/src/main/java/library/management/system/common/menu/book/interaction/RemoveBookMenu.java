package library.management.system.common.menu.book.interaction;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.HoldRequest;
import library.management.system.common.entities.Library;
import library.management.system.common.entities.Loan;
import library.management.system.common.menu.help.AddUpdateCheckBookMenu;
import library.management.system.common.services.BookManagementService;
import library.management.system.common.services.HoldRequestManagementService;
import library.management.system.common.services.LoanManagementService;
import library.management.system.common.utils.DatabaseInteractions;

import java.util.ListIterator;

public class RemoveBookMenu extends AddUpdateCheckBookMenu {

    private Library library = Library.getInstance();
    private BookManagementService bookService = BookManagementService.getInstance();
    private LoanManagementService loanService = LoanManagementService.getInstance();
    private HoldRequestManagementService holdRequestService = HoldRequestManagementService.getInstance();

    @Override
    public void start() {
        printMenuHeader();

        Book book = getBook();
        if (book != null) {
            int result = DatabaseInteractions.removeBook(book.getBookId());
            if (result < 0) { // if no book id is returned that means it is successfully deleted
                ListIterator<Loan> loans = loanService.getLoansForBook(book).listIterator();
                while (loans.hasNext()) {
                    loanService.removeLoan(loans.next());
                }

                ListIterator<HoldRequest> holdRequests = holdRequestService.getHoldRequestsForBook(book).listIterator();
                while (holdRequests.hasNext()) {
                    holdRequestService.removeHoldRequest(holdRequests.next());
                }

                bookService.removeBook(book);
            }
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                           "----------------------------------------------------");
        System.out.println("===============  Remove Book Portal  ==============");
        System.out.println("----------------------------------------------------");
    }
}
