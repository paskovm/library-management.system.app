package library.management.system.common.menu.book.interaction;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.HoldRequest;
import library.management.system.common.entities.Library;
import library.management.system.common.menu.help.AddUpdateCheckBookMenu;
import library.management.system.common.services.HoldRequestManagementService;
import library.management.system.common.utils.HoldReqComparator;

import java.util.Collections;
import java.util.List;

public class CheckBookHoldReqMenu extends AddUpdateCheckBookMenu {

    private Library library = Library.getInstance();
    private HoldRequestManagementService holdRequestService = HoldRequestManagementService.getInstance();

    @Override
    public void start() {
        printMenuHeader();
        Book book = getBook();

        if (book != null) {
            List<HoldRequest> holdRequests = holdRequestService.getHoldRequestsForBook(book);
            if (holdRequests.size() > 0) {
                Collections.sort(holdRequests, new HoldReqComparator());

                for (HoldRequest holdRequest : holdRequests) {
                    holdRequest.printInfo();
                }
            } else {
                System.out.println("No hold requests available for book \'" + book.getTitle() + "\'.");
            }
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                "----------------------------------------------------");
        System.out.println("=============  Book Hold Requests Info  ============");
        System.out.println("----------------------------------------------------");
    }
}
