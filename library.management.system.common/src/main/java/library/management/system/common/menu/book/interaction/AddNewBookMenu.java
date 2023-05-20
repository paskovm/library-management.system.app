package library.management.system.common.menu.book.interaction;

import library.management.system.common.entities.Library;
import library.management.system.common.menu.help.AddUpdateCheckBookMenu;

public class AddNewBookMenu extends AddUpdateCheckBookMenu {

    private Library library = Library.getInstance();

    @Override
    public void start() {
        printMenuHeader();
        createBook();

        library.getMainMenu().start();
    }

    @Override
    public void printMenuHeader() {
        System.out.println(System.lineSeparator() +
                            "----------------------------------------------------");
        System.out.println("===============  Add New Book Portal  ==============");
        System.out.println("----------------------------------------------------");
    }
}
