package library.management.system.common.menu.book.interaction;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.Library;
import library.management.system.common.menu.help.AddUpdateCheckBookMenu;
import library.management.system.common.services.BookManagementService;

import java.util.List;

public class ViewAllBooksMenu extends AddUpdateCheckBookMenu {

    private Library library = Library.getInstance();
    private BookManagementService bookManagementService = BookManagementService.getInstance();

    @Override
    public void start() {
        printMenuHeader();

        List<Book> books = bookManagementService.getAllBooksInLibrary();
        for (Book book : books)
            book.printInfo();

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("==============  All Books in Library  ==============");
        System.out.println("----------------------------------------------------");
    }
}
