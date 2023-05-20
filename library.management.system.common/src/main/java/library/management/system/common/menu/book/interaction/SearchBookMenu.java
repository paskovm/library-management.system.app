package library.management.system.common.menu.book.interaction;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.Library;
import library.management.system.common.menu.help.AddUpdateCheckBookMenu;

public class SearchBookMenu extends AddUpdateCheckBookMenu {

    private Library library = Library.getInstance();

    @Override
    public void start() {
        printMenuHeader();

        Book book = getBook();
        if (book != null) {
            book.printInfo();
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("==============   Search a Book Portal ==============");
        System.out.println("----------------------------------------------------");
    }
}
