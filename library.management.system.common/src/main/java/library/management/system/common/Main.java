package library.management.system.common;

import library.management.system.common.menu.main.MainMenu;
import library.management.system.common.utils.PopulateLibrary;

public class Main {

    public static void main(String[] args) {
        PopulateLibrary.populate();
        
        new MainMenu().start();
    }
}
