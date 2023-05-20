package library.management.system.common.services;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.Loan;
import library.management.system.common.entities.actors.Borrower;

import java.util.ArrayList;
import java.util.List;

public class LoanManagementService {

    private static LoanManagementService instance;
    private List<Loan> loans;

    private LoanManagementService() {
        this.loans = new ArrayList<>();
    }

    public static LoanManagementService getInstance() {
        if (instance == null) {
            instance = new LoanManagementService();
        }
        return instance;
    }

    public void setLoans(List<Loan> l) {
        loans = l;
    }

    public List<Loan> getAllLoans() {
        return loans;
    }

    public List<Loan> getAllLoanedBooksForUser(Borrower borrower) {
        List<Loan> loansForUser = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getBorrower().equals(borrower)) {
                loansForUser.add(loan);
            }
        }
        return loansForUser;
    }

    public List<Loan> getCurrentlyLoanedBooksForUser(Borrower borrower) {
        List<Loan> loansForUser = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getBorrower().equals(borrower) && (loan.getReceiver() == null || loan.getReturnDate() == null)) {
                loansForUser.add(loan);
            }
        }
        return loansForUser;
    }

    public List<Loan> getLoansForBook(Book book) {
        List<Loan> bookLoans = new ArrayList<>();
        if (book != null) {
            for (Loan loan : loans) {
                if (loan.getBook().equals(book)) {
                    bookLoans.add(loan);
                }
            }
        }
        return bookLoans;
    }

    public void issueBook(Loan loan) {
        loans.add(loan);
        loan.getBook().setIssued(true);
        loan.getBorrower().addLoan(loan);
    }

    public void removeLoan(Loan loan) {
        loan.getBorrower().removeLoan(loan);
        loans.remove(loan);
    }


    public Loan getLoan(Borrower borrower, Book book, boolean loanCompleted) {
        for (Loan loan :loans) {
            if (loan.getBook().equals(book) && loan.getBorrower().equals(borrower) && (loan.isFinePaid() == loanCompleted)) {
                return loan;
            }
        }
        return null;
    }

    public void returnBook(Loan loan) {
        loans.remove(loan);
        loan.getBorrower().completeLoan(loan.getBook(), loan.getReceiver());
    }

    public void printBorrowersFines(Borrower borrower) {
        List<Loan> usersLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (borrower.equals(loan.getBorrower())) {
                usersLoans.add(loan);
            }
        }
        if (usersLoans.size() > 0) {
            double finesSum = 0;
            System.out.println(System.lineSeparator() + "======== Following Loans are available for user "
                               + borrower.getFirstName() + " " + borrower.getLastName() + " ========");
            for (Loan loan : usersLoans) {
                if (!loan.isFinePaid()) {
                    loan.printInfo();
                    finesSum += loan.calculateFine();
                }
            }
            System.out.println(System.lineSeparator() + "-----------------------");
            System.out.println("Fine(s) sum is = $" + finesSum);
        }
    }

    public List<Loan> getBorrowersFines(Borrower borrower) {
        List<Loan> usersNotCompletedLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (borrower.equals(loan.getBorrower()) && !loan.isFinePaid()) {
                usersNotCompletedLoans.add(loan);
            }
        }
        return usersNotCompletedLoans;
    }

    public double getBorrowersFine(Borrower borrower, Book book) {
        Loan usersLoan = null;
        for (Loan loan : loans) {
            if (borrower.equals(loan.getBorrower()) && book.equals(loan.getBook())) {
                usersLoan = loan;
                break;
            }
        }

        if (usersLoan == null) {
            return 0;
        }
        return usersLoan.calculateFine();
    }

}
