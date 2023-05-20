package library.management.system.common.services;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.HoldRequest;
import library.management.system.common.entities.actors.Borrower;

import java.util.ArrayList;
import java.util.List;

public class HoldRequestManagementService {

    private static HoldRequestManagementService instance;
    private static List<HoldRequest> holdRequests;

    private HoldRequestManagementService() {
        holdRequests = new ArrayList<>();

    }

    public static HoldRequestManagementService getInstance() {
        if (instance == null) {
            instance = new HoldRequestManagementService();
        }
        return instance;
    }

    public void setHoldRequests(List<HoldRequest> hr) {
        holdRequests = hr;
    }

    public List<HoldRequest> getHoldRequests() {
        return holdRequests;
    }

    public List<HoldRequest> getHoldRequestsForUser(Borrower borrower) {
        List<HoldRequest> holdRequestsForUser = new ArrayList<>();
        for (HoldRequest holdRequest : holdRequests) {
            if (holdRequest.getBorrower().equals(borrower)) {
                holdRequestsForUser.add(holdRequest);
            }
        }
        return holdRequestsForUser;
    }

    public List<HoldRequest> getHoldRequestsForBook(Book book) {
        List<HoldRequest> holdRequestsForBook = new ArrayList<>();
        for (HoldRequest holdRequest : holdRequests) {
            if (holdRequest.getBook().equals(book)) {
                holdRequestsForBook.add(holdRequest);
            }
        }
        return holdRequestsForBook;
    }

    public void createHoldRequest(HoldRequest holdRequest) {
        for (HoldRequest h : holdRequests) {
            if (h.equals(holdRequest)) {
                System.out.println("Hold Request already created for this book! Details:");
                h.printInfo();
                return;
            }
        }
        holdRequests.add(holdRequest);
        holdRequest.getBorrower().addHoldRequest(holdRequest);
        System.out.println("Hold request successfully created.");
        holdRequest.printInfo();
    }

    public void removeHoldRequest(HoldRequest holdRequest) {
        holdRequest.getBorrower().removeHoldRequest(holdRequest);
        holdRequests.remove(holdRequest);
    }
}
