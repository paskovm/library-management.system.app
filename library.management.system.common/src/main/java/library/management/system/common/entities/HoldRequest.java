package library.management.system.common.entities;

import library.management.system.common.entities.actors.Borrower;

import java.time.LocalDate;

public class HoldRequest {

    private Borrower borrower;
    private Book book;
    private LocalDate requestDate;

    public HoldRequest(Borrower borrower, Book book) {
        this.borrower = borrower;
        this.book = book;
        this.requestDate = LocalDate.now();
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj instanceof HoldRequest) {
            HoldRequest holdRequest = (HoldRequest) obj;
            return holdRequest.getBorrower().equals(this.borrower) && holdRequest.getBook().equals(this.book);
        }
        return false;
    }

    public void printInfo() {
        System.out.println("\nHold Request details: ");
        System.out.println("Borrower: " + borrower.getFirstName() + " " + borrower.getLastName() +
                           ", with ID " + borrower.getId());
        System.out.println("Book: " + book.getTitle());
        System.out.println("Request date: " + requestDate);
    }

    @Override
    public String toString() {
        return "Hold Request details: " + System.lineSeparator() +
               "Borrower: " + borrower.getFirstName() + " " + borrower.getLastName() +
               ", with ID " + borrower.getId() + System.lineSeparator() +
               "Book: " + book.getTitle() + System.lineSeparator() +
               "Request date: " + requestDate;
    }
}
