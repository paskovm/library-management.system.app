package library.management.system.common.services;

import library.management.system.common.entities.Book;

import java.util.ArrayList;
import java.util.List;

public class BookManagementService {

    private static BookManagementService instance;
    private List<Book> books;

    private BookManagementService() {
        this.books = new ArrayList<>();
    }

    public void setBooks(List<Book> bk) {
        books = bk;
    }

    public static BookManagementService getInstance() {
        if (instance == null) {
            instance = new BookManagementService();
        }
        return instance;
    }

    public List<Book> getAllBooksInLibrary() {
        return books;
    }

    public Book getBookById(int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }
        return null;
    }

    public Book getBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> getBooksByAuthor(String author) {
        List<Book> filteredBooksByAuthor = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equals(author)) {
                filteredBooksByAuthor.add(book);
            }
        }
        return filteredBooksByAuthor;
    }

    public List<Book> getBooksByGenre(String genre) {
        List<Book> filteredByGenreBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getGenre().equals(genre)) {
                filteredByGenreBooks.add(book);
            }
        }
        return filteredByGenreBooks;
    }

    public void addBook(Book book) {
        if (books.contains(book)) {
            System.out.println("The book you are trying to add is already added to the library!\nCheck its details and update it if needed.");
            return;
        }
        books.add(book);
        System.out.println("Book successfully added to library.");
    }

    public void removeBook(Book book) {
        if (!books.contains(book)) {
            System.out.println("The book you are trying to remove is not present in the library!");
            return;
        }
        books.remove(book);
        System.out.println("Book removed successfully from library!");
    }

    public void updateBook(Book book, String title, String author, String genre) {
        if (!books.contains(book)) {
            System.out.println("The book you are trying to update is not in the library!");
            return;
        }

        if (title != null && !title.isBlank() && !title.isEmpty()) {
            book.setTitle(title);
            System.out.println("The books title is successfully updated!");
        }

        if (author != null && !author.isBlank() && !author.isEmpty()) {
            book.setAuthor(author);
            System.out.println("The books author is successfully updated!");
        }

        if (genre != null && !genre.isBlank() && !genre.isEmpty()) {
            book.setGenre(genre);
            System.out.println("The books genre is successfully updated!");
        }
    }
}
