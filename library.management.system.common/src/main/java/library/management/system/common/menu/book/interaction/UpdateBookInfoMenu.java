package library.management.system.common.menu.book.interaction;

import library.management.system.common.entities.Book;
import library.management.system.common.entities.Library;
import library.management.system.common.menu.help.AddUpdateCheckBookMenu;

public class UpdateBookInfoMenu extends AddUpdateCheckBookMenu {

    private Library library = Library.getInstance();

    @Override
    public void start() {
        printMenuHeader();

        Book book = getBook();
        if (book != null) {
            updateBookInfo(book);
        }

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("================  Update Book Portal  ==============");
        System.out.println("----------------------------------------------------");
    }
}
