package library.management.system.common.entities;

import library.management.system.common.entities.actors.Borrower;
import library.management.system.common.entities.actors.Staff;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Loan {

    private Borrower borrower;
    private Book book;
    private Staff issuer;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private Staff receiver;
    private double borrowersFine;
    private boolean finePaid;

    public Loan(Borrower borrower, Book book, Staff issuer) {
        this.borrower = borrower;
        this.book = book;
        this.issuer = issuer;
        this.issueDate = LocalDate.now();
        this.finePaid = false;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public Book getBook() {
        return book;
    }

    public Staff getIssuer() {
        return issuer;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Staff getReceiver() {
        return receiver;
    }

    public double getBorrowersFine() {
        borrowersFine = calculateFine();
        return borrowersFine;
    }

    public boolean isFinePaid() {
        return finePaid;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setIssuer(Staff issuer) {
        this.issuer = issuer;
    }

    public void setReceiver(Staff receiver) {
        this.receiver = receiver;
    }

    public void setFinePaid(boolean finePaid) {
        this.borrowersFine = 0;
        this.finePaid = finePaid;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public double calculateFine() {
        int finePerDay = Library.FINE_PER_DAY;
        LocalDate returnDeadlineDate = issueDate.plus(Period.ofDays(Library.BOOK_RETURN_DEADLINE));
        LocalDate dateNow = LocalDate.now();
        if (dateNow.isAfter(returnDeadlineDate)) {
            return ChronoUnit.DAYS.between(returnDeadlineDate, dateNow) * finePerDay;
        }
        return 0;
    }

    public void printInfo() {
        System.out.println("\nLoan details: ");
        System.out.println("Borrower: " + borrower.getFirstName() + " " + borrower.getLastName());
        System.out.println("Book: \"" + book.getTitle() + "\", author: " + book.getAuthor());
        System.out.println("Issuer: " + issuer.getFirstName() + " " + issuer.getLastName());
        System.out.println("Issue date: " + issueDate);
        System.out.println("Receiver: " + (receiver != null ? receiver.getFirstName() + " " + receiver.getLastName() : "-"));
        System.out.println("Return date: " + (returnDate != null ? returnDate : "-"));
        System.out.println("Borrower's fine: " + calculateFine());
        System.out.println("Fine paid: " + (finePaid == true ? "yes" : "no"));
    }

}
