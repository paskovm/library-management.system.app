package library.management.system.common.entities;

import library.management.system.common.entities.actors.Person;
import library.management.system.common.menu.Menu;

public class Library {

    private String name;
    private static Library instance;
    public static final int FINE_PER_DAY = 2;
    public static final int BOOK_RETURN_DEADLINE = 3;

    private Person loggedInPerson;

    private Menu mainMenu;

    private Library() {
        this.name = "National Library";
    }

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public Menu getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(Menu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public Person getLoggedInPerson() {
        return loggedInPerson;
    }

    public void setLoggedInPerson(Person loggedInPerson) {
        this.loggedInPerson = loggedInPerson;
    }
}
