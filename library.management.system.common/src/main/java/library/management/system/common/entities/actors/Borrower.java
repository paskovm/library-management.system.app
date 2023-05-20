package library.management.system.common.entities.actors;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.HoldRequest;
import library.management.system.common.entities.Loan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Borrower extends Person {

    private List<Loan> loans;
    private List<HoldRequest> holdRequests;

    public Borrower(int id, String firstName, String lastName, String address, long phoneNumber, String email, String password) {
        super(id, firstName, lastName, address, phoneNumber, email, password);
        this.loans = new ArrayList<>();
        this.holdRequests = new ArrayList<>();
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void removeLoan(Loan loan) {
        loans.remove(loan);
    }

    public void addHoldRequest(HoldRequest holdRequest) {
        holdRequests.add(holdRequest);
    }

    public void removeHoldRequest(HoldRequest holdRequest) {
        holdRequests.remove(holdRequest);
    }

    public List<Loan> getAllLoans() {
        return loans;
    }

    public List<Loan> getNotCompletedLoans() {
        List<Loan> notCompletedLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getReceiver() == null || loan.getReturnDate() == null) {
                notCompletedLoans.add(loan);
            }
        }
        return notCompletedLoans;
    }

    public void completeLoan(Book book, Staff receiver) {
        for (Loan loan : loans) {
            if (loan.getBook().equals(book)) {
                loan.setReceiver(receiver);
                loan.setReturnDate(LocalDate.now());
                loan.setFinePaid(true);
                break;
            }
        }
    }

    public void updateLoan(Book book, Staff issuer) {
        for (Loan loan : loans) {
            if (loan.getBook().equals(book)) {
                loan.setIssuer(issuer);
                loan.setIssueDate(LocalDate.now());
                break;
            }
        }
    }

    public List<HoldRequest> getHoldRequests() {
        return holdRequests;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Loans available: " + (loans.size() > 0 ? "yes" : "no"));
        System.out.println("Hold requests available: " + (holdRequests.size() > 0 ? "yes" : "no"));
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() +
                "Loans available: " + (loans.size() > 0 ? "yes" : "no") + System.lineSeparator() +
                "Hold requests available: " + (holdRequests.size() > 0 ? "yes" : "no");
    }
}
