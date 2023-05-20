package library.management.system.common.menu.help;

import library.management.system.common.entities.Book;
import library.management.system.common.menu.Menu;
import library.management.system.common.services.BookManagementService;
import library.management.system.common.utils.DatabaseInteractions;

import java.util.Scanner;

public abstract class AddUpdateCheckBookMenu implements Menu {

    private BookManagementService bookService = BookManagementService.getInstance();

    protected Book getBook() {
        Scanner scanner = new Scanner(System.in);
        Book book = null;

        MAIN:
        while (true) {
            try {
                System.out.println("----------------------------------------------------");
                System.out.println("To search a book by id press 1, " + System.lineSeparator() +
                        "to search a book by title press 2." + System.lineSeparator());
                System.out.println("Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("Enter book id: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        book = bookService.getBookById(id);
                        if (book == null) {
                            System.out.println("No book with id \"" + id + "\" exists.");
                        }
                        break MAIN;

                    case 2:
                        System.out.println("Enter books title: ");
                        String title = scanner.nextLine();
                        book = bookService.getBookByTitle(title);
                        if (book == null) {
                            System.out.println("No book with title \"" + title + "\" exists.");
                        }
                        break MAIN;

                    default:
                        System.out.println("Wrong choice provided! Your choice must be a number between 1-2.");
                        break MAIN;
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong choice provided! Please, try again.");
            }
        }

        return book;
    }

    protected void createBook() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("----------------------------------------------------");
            System.out.println(System.lineSeparator() + "Please, enter books title: ");
            String title = scanner.nextLine();
            if (title.isBlank() || title.isEmpty()) {
                System.out.println("You can't provide empty value for title!");
                break;
            }

            System.out.println("Please, enter books author: ");
            String author = scanner.nextLine();
            if (author.isBlank() || author.isEmpty()) {
                System.out.println("You can't provide empty value for author!");
                break;
            }

            System.out.println("Please, enter books genre: ");
            String genre = scanner.nextLine();
            if (genre.isBlank() || genre.isEmpty()) {
                System.out.println("You can't provide empty value for genre!");
                break;
            }

            if (title == null || author == null || genre == null) {
                System.out.println("Problem detected! Empty value provided: ");
                System.out.println("title: " + title);
                System.out.println("author: " + author);
                System.out.println("genre: " + genre);
                System.out.println("The added book can't have empty value!");

            } else {
                int bookId = DatabaseInteractions.createBook(title, author, genre);
                if (bookId > 0) {
                    Book book = new Book(bookId, title, author, genre);
                    bookService.addBook(book);
                } else {
                    System.out.println("Can't create the book!");
                }
            }

            break;
        }
    }

    protected void updateBookInfo(Book book) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("----------------------------------------------------");
        System.out.println("Please, enter books new title or press enter to continue: ");
        String title = scanner.nextLine();

        System.out.println("Please, enter books new author or press enter to continue: ");
        String author = scanner.nextLine();

        System.out.println("Please, enter books new genre or press enter to continue: ");
        String genre = scanner.nextLine();

        if ((title != null && !title.isEmpty() && !title.isBlank()) ||
                (author != null && !author.isEmpty() && !author.isBlank()) ||
                (genre != null && !genre.isEmpty() && !genre.isBlank())) {

            int result = DatabaseInteractions.updateBook(book, title, author,genre);

            if (result > 0) {
                bookService.updateBook(book, title, author, genre);
            } else {
                System.out.println("Problem appeared during the update to db!");
            }

        } else {
            System.out.println("There is nothing to be updated!");
        }
    }

}
